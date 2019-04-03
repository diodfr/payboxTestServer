package payboxtestserver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.validation.Valid;


public class ParameterCommand {

	private Map<String, String> mapping;

	private synchronized void buildMapping(String pbxRetour) {
		if (mapping == null) {
			mapping = new HashMap<>();
			try(Scanner scan = new Scanner(pbxRetour)) {
				scan.useDelimiter(";");

				while (scan.hasNext()) {
					String nameVariable[] = scan.next().split(":");
					mapping.put(nameVariable[1], nameVariable[0]);
				}
			}
		}
	}

	public String getValue(String codeRetour, MessagePaiement paiement) {
		switch (codeRetour) {
		case "M":
			return paiement.getPbxTotal();
		case "R":
			return paiement.getPbxCMD();
		case "T":
			return "NUM_APPEL_PAYBOX";
		case "A":
			return "NUM_AUTORISATION_" + Math.abs(ThreadLocalRandom.current().nextInt());
		case "B":
			return "NUM_ABONNEMENT";
		case "C":
			return "VISA"; //CB, VISA, EUROCARD_MASTERCARD
		case "D":
			return Year.now().plusYears(2).format(DateTimeFormatter.ofPattern("yy"));
		case "E":
			return MessagePaiement.ERROR_VALUE_TO_BE_REPLACED;
		case "F":
			return "N";
		case "G":
			return "N";
		case "H":
			return "EMPREINTE_CARTE";
		case "I":
			return "FR";
		case "J":
			return "45";
		case "K":
			return MessagePaiement.SIGNATURE_VALUE_TO_BE_REPLACED;
		case "L":
			return "123456";
		case "O":
			return "N";
		case "o":
			return "005";
		case "P":
			return "CARTE"; // FIXME TEST
		case "Q":
			return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:SS"));
		case "S":
			return "NUM_TRANSACTION_" + Math.abs(ThreadLocalRandom.current().nextInt());
		case "U":
			return "NOT_MANAGED";
		case "V":
			return "ID_SIGMAPLUS";
		case "W":
			return LocalDate.now().format(DateTimeFormatter.ofPattern("DDMMYYYY"));
		case "Y":
			return "FR";
		case "Z":
			return "NOT_IMPLEMENTED";
		default:
			return "PAS_ENCORE";
		}
	}

	//FIXME faire avec du pattern matching puisque la signature doit être à la fin
	public Optional<String> getSignatureField(MessagePaiement paiement) {
		buildMapping(paiement.getPbxRetour());
		
		return Optional.ofNullable(mapping.get("K"));
	}
	
	public Optional<String> getCmdRefField(MessagePaiement paiement) {
		buildMapping(paiement.getPbxRetour());

		return Optional.ofNullable(mapping.get("R"));
	}

	public Optional<String> getMontantField(MessagePaiement paiement) {
		buildMapping(paiement.getPbxRetour());

		return Optional.ofNullable(mapping.get("M"));
	}

	public Optional<String> getTransactionField(MessagePaiement paiement) {
		buildMapping(paiement.getPbxRetour());

		return Optional.ofNullable(mapping.get("S"));
	}
	
	public Optional<String> getErrorField(MessagePaiement paiement) {
		buildMapping(paiement.getPbxRetour());

		return Optional.ofNullable(mapping.get("E"));
	}
	
	public Optional<String> getAuthorisationField(MessagePaiement paiement) {
		buildMapping(paiement.getPbxRetour());

		return Optional.ofNullable(mapping.get("A"));
	}

	public Map<String, String> buildParameters(@Valid MessagePaiement paiement) {
		buildMapping(paiement.getPbxRetour());
		Map<String, String> parameters = new HashMap<>();
		parameters.put("PBX_REPONDRE_A", paiement.getPbxRepondreA());

		Set<String> excluded = Set.of("K", "E");
		for (Entry<String, String> entry : mapping.entrySet()) {
			if (!excluded.contains(entry.getKey())) {
				parameters.put(entry.getValue(), getValue(entry.getKey(), paiement));
			}
		}
		
		return parameters;
	}
}
