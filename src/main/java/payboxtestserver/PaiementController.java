package payboxtestserver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.paris.lutece.plugins.paybox.PayboxUtil;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.server.types.files.AttachedFile;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.TaskScheduler;
import io.micronaut.validation.Validated;
import io.micronaut.views.View;
import paybox.CodeErreur;

@Controller("/")
@Validated
public class PaiementController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaiementController.class);

	@Inject @Named(TaskExecutors.SCHEDULED) TaskScheduler taskScheduler;

	@Inject PrivateKey privateKey;

	@View("default")
	@Get ("/")
	public HttpResponse<Map<String, Object>> index() {
		return HttpResponse.ok(); 
	}

	@Get ("/key")
	public AttachedFile publicKey() {
		return new AttachedFile(new File("/work/pubkey.pem")); 
	}

	@View("pay")
	@Post(value = "/paiement", consumes = MediaType.APPLICATION_FORM_URLENCODED)
	public HttpResponse<Map<String, Object>> paiement(@Valid MessagePaiement values) {
		return traiterRequete(values);
	}

	@View("pay")
	@Get(value = "/paiement{?messagePaiement*}")
	public HttpResponse<Map<String, Object>> paiementEnGet(@Valid MessagePaiement messagePaiement) {
		return traiterRequete(messagePaiement);
	}

	/**
	 * Traitement de la requête de paiement
	 * @param values les données de la requêtes
	 * @return
	 */
	private HttpResponse<Map<String, Object>> traiterRequete(MessagePaiement values) {
		ParameterCommand parameterCommand = new ParameterCommand();
		Map<String,Object> parameters = new HashMap<>();


		List<Map<String, String>> erreurs = Arrays.stream(CodeErreur.values())
				.map(e -> {
					Map<String, String> map = new HashMap<>();
					map.put("value", e.getCode());
					map.put("name", e.name());
					map.put("selected", "");
					return map;
				})
				.collect(Collectors.toList());
		parameters.put("ERREURS", erreurs);
		parameters.put("values", values);
		parameters.put("refCmd", parameterCommand.getCmdRefField(values) + "=" + parameterCommand.getValue("R", values));
		parameters.put("formValues", parameterCommand.buildParameters(values));

		parameterCommand.getSignatureField(values).ifPresent(v -> parameters.put("K", v));
		parameterCommand.getMontantField(values).ifPresent(v -> parameters.put("M", v));
		parameterCommand.getTransactionField(values).ifPresent(v -> parameters.put("S", v));
		parameterCommand.getErrorField(values).ifPresent(v -> parameters.put("PBX_ERREUR", v));
		parameterCommand.getAuthorisationField(values).ifPresent(v -> parameters.put("PBX_AUTORISATION", v));
		

		LOGGER.error("ici  --- {}", values);

		return HttpResponse.ok(parameters);
	}

	@SuppressWarnings("unused")
	private Map<String, String> getRetour(Optional<String> listeRetour) {
		Map<String, String> retours = new HashMap<>();
		if (listeRetour.isPresent()) {
			// ref:R;trans:T;auto:A;tarif:M;abonnement:B;pays:Y;erreur:E
			String[] parameters = listeRetour.get().split(";");

			for (String param : parameters) {
				String[] paramRetour = param.split(":");
				if (paramRetour.length != 2) {
					LOGGER.error("Fail to parse PBX_RETOUR {}", param);
					retours.clear();
					retours.put("ERROR_ON_PARAM", param);
				}

				LOGGER.debug("param {} {}", paramRetour[0], paramRetour[1]);
				retours.put(paramRetour[0], paramRetour[1]);
			}
		}
		return retours;
	}

	/**
	 * Utilisation de JSON car sinon comportement bidons
	 * @param PBX_REPONDRE_A
	 * @param PBX_ERREUR
	 * @return
	 */
	@Post(value = "/ipn")
	public String ipn(@Body HashMap<String,String> parameters) {
		LOGGER.info("nouvelle demande IPN : \"{}\" ", parameters.get("PBX_REPONDRE_A"));
		LOGGER.debug("Demande IPN {}", parameters);
		String urlReponse = parameters.get("PBX_REPONDRE_A");
		if (urlReponse == null || urlReponse.isEmpty()) {
			String errorMsg = "Demande IPN URL de réponse absente (PBX_REPONDRE_A est obligatoire)";
			LOGGER.error(errorMsg);
			return errorMsg;
		}

		URL urlIPN;
		String beforeRequestParameters = "?";
		try {
			LOGGER.error("URL {}", urlReponse);
			String[] parsedURL = urlReponse.split("\\?",2);
			urlIPN = new URL(parsedURL[0] + (parsedURL[0].endsWith("/") ? "" : "/")); // ajout d'un / en fin si absent (BUG suppression de la fin de l'url)
			
			if (parsedURL.length >= 2 && parsedURL[1] != null && !parsedURL[1].isEmpty()) {
				beforeRequestParameters += parsedURL[1] + "&";
			}
		} catch (MalformedURLException e) {
			LOGGER.error("Error --- IPN URL", e);
			return "Error";
		}
		
		StringBuilder ipnParameters = new StringBuilder();
		boolean first=true;
		
		Set<String> excluded = Set.of("PBX_REPONDRE_A", "delay", "K");
		for (Entry<String, String> paramValueEntry : parameters.entrySet()) {
			if (excluded.contains(paramValueEntry.getKey())) {
				continue;
			}
			if (!first) {
				ipnParameters.append("&");
			}
			ipnParameters.append(paramValueEntry.getKey());
			ipnParameters.append("=");
			
			try {
				LOGGER.info("=>{}<=", paramValueEntry.getValue());
				ipnParameters.append(URLEncoder.encode(paramValueEntry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				LOGGER.error("Erreur : Impossible d'encoder les paramètres de la requette", ex);
				return "Erreur : Impossible d'encoder les paramètres de la requette";
			}
			
			first = false;
		}

		String signK = parameters.get("K");
		if (signK != null && !signK.isEmpty()) {
			String signature;
			try {
				signature = PayboxUtil.sign(ipnParameters.toString(), privateKey);
				LOGGER.info("SIGANTURE OF {} = {}", ipnParameters.toString(), signature);
				signature = URLEncoder.encode(signature, StandardCharsets.UTF_8.name());
			} catch (InvalidKeyException | SignatureException | UnsupportedEncodingException
					| NoSuchAlgorithmException e) {
				LOGGER.error("Impossible de signer la requete", e);
				return "Erreur : Impossible de signer la requete IPN";
			}
			ipnParameters.append("&");
			ipnParameters.append(signK);
			ipnParameters.append("=");
			ipnParameters.append(signature);
		}
		
		int delay = Optional.ofNullable(parameters.get("delay")).map(Integer::valueOf).orElse(1);
		String requestParameters = beforeRequestParameters + ipnParameters.toString();
		taskScheduler.schedule(Duration.ofMinutes(delay), 
				() -> contact(urlIPN, requestParameters));

		return "Lancement de la requette dans " + parameters.get("delay") + " minute";
	}

	private void contact(URL urlIPN, String requestParams) {
		LOGGER.info("SEND IPN : {}{} -- {}", urlIPN.toString(), requestParams, urlIPN.getQuery());
		
		try(RxHttpClient client = RxHttpClient.create(urlIPN)) {
			String result = client.toBlocking().retrieve(requestParams);
			LOGGER.info("IPN RESULT : {} =>\n{}\n<=", urlIPN.toString(), result);
		}
	}

	@Get(value = "/sign")
	public String sign(HttpRequest<?> request) {
		LOGGER.info("Sign requested");
		StringBuilder toSign = new StringBuilder();
		boolean first = true;
		String parameterK = "sign";

		for (Entry<String, List<String>> parameters : request.getParameters()) {
			if ("K".equals(parameters.getKey())) {
				String value = parameters.getValue().get(0);
				if (value != null && ! value.isEmpty() ) {
					parameterK = value;
				}
				continue;
			}
			if (!first) toSign.append("&");

			if (parameters.getValue().size() > 1) {
				int current = 0;
				for (String value : parameters.getValue()) {
					toSign.append(parameters.getKey() + '[' + current + "]=" + value); // FIXME URLEncoder.encode(value, StandardCharsets.UTF_8.name())
				}
			} else {
				toSign.append(parameters.getKey() + "=" + parameters.getValue().get(0)); // FIXME URLEncoder.encode(value, StandardCharsets.UTF_8.name())
			}

			first = false;
		}


		try {
			String signature = PayboxUtil.sign(toSign.toString(), privateKey);
			signature = URLEncoder.encode(signature, StandardCharsets.UTF_8.name());
			LOGGER.debug("Sign : >{}< => >{}<", toSign.toString(), signature);
			String signed = toSign.toString() + "&" + parameterK + "=" + signature ;
			return signed;
		} catch (InvalidKeyException | SignatureException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
			LOGGER.error("impossible de signer la requete", e);
			return "ERROR -----> Impossible de signer la requete";
		}
	}		
}
