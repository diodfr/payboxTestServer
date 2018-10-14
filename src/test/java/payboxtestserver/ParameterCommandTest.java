package payboxtestserver;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import io.mikael.urlbuilder.UrlBuilder;

public class ParameterCommandTest {

	@Test
	public void test() {
		UrlBuilder builder = UrlBuilder.fromString("http://www.yahoo.fr?test=toto")
				.addParameter("titi", "bof")
				.addParameter("ddd", "bof");
		
		assertThat(builder.queryParameters.entrySet().toString(), IsEqual.equalTo("[test=[toto], titi=[bof], ddd=[bof]]"));
	}
	
	@Test
	public void getSignature() {
		MessagePaiement message = new MessagePaiement();
		message.setPbxRetour("Mt:M;Ref:R;Auto:A;Erreur:E;Signatt:K");
		ParameterCommand command = new ParameterCommand();
		Optional<String> signatureField = command.getSignatureField(message);
		assertThat(signatureField.isPresent(), IsEqual.equalTo(true));
		assertThat(signatureField.get(), IsEqual.equalTo("Signatt"));
	}
}
