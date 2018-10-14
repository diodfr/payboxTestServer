package payboxtestserver;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class MessagePaiement {
	public static final String SIGNATURE_VALUE_TO_BE_REPLACED = "REPLACE_THIS_VALUE_BY_SIGNATURE";
	public static final String ERROR_VALUE_TO_BE_REPLACED = "REPLACE_THIS_VALUE_BY_ERROR";

	// Obligatoires
	String PBX_SITE; //Numéro de site (fourni par Verifone)
	String PBX_RANG; //Numéro de rang (fourni par Verifone)
	String PBX_IDENTIFIANT; //Identifiant interne (fourni par Verifone)
	String PBX_TOTAL; //Montant total de la transaction
	String PBX_DEVISE; //Devise de la transaction
	String PBX_CMD; //Référence commande côté commerçant
	String PBX_PORTEUR; //Adresse E-mail de l’acheteur
	String PBX_RETOUR; //Liste des variables qui seront retournées par Verifone
	String PBX_HASH; //Type d’algorithme de hachage pour le calcul de l’empreinte
	String PBX_TIME; //Horodatage de la transaction
	String PBX_HMAC; //Signature calculée avec la clé secrète

	// Optionnels
	String PBX_EFFECTUE;
	String PBX_REFUSE;
	String PBX_ANNULE;
	String PBX_ATTENTE;

	// IPN
	String PBX_REPONDRE_A;
	
	// on n'utilise pas les dates de retour
	String PBX_2MONT1;
	String PBX_2MONT2;
	String PBX_2MONT3;

	public MessagePaiement() {
	}

	@JsonGetter("PBX_SITE")
	public String getPbxSite() {
		return PBX_SITE;
	}

	@JsonSetter("PBX_SITE")
	public void setPbxSite(String pBX_SITE) {
		PBX_SITE = pBX_SITE;
	}

	@JsonGetter("PBX_RANG")
	public String getPbxRang() {
		return PBX_RANG;
	}

	@JsonSetter("PBX_RANG")
	public void setPbxRang(String pBX_RANG) {
		PBX_RANG = pBX_RANG;
	}

	@JsonGetter("PBX_IDENTIFIANT")
	public String getPbxIdentifiant() {
		return PBX_IDENTIFIANT;
	}

	@JsonSetter("PBX_IDENTIFIANT")
	public void setPbxIdentifiant(String pBX_IDENTIFIANT) {
		PBX_IDENTIFIANT = pBX_IDENTIFIANT;
	}

	@JsonGetter("PBX_TOTAL")
	public String getPbxTotal() {
		return PBX_TOTAL;
	}

	@JsonSetter("PBX_TOTAL")
	public void setPBXTotal(String pBX_TOTAL) {
		PBX_TOTAL = pBX_TOTAL;
	}

	@JsonGetter("PBX_DEVISE")
	public String getPbxDevise() {
		return PBX_DEVISE;
	}

	@JsonSetter("PBX_DEVISE")
	public void setPbxDevise(String pBX_DEVISE) {
		PBX_DEVISE = pBX_DEVISE;
	}

	@JsonGetter("PBX_CMD")
	public String getPbxCMD() {
		return PBX_CMD;
	}

	@JsonSetter("PBX_CMD")
	public void setPbxCMD(String pBX_CMD) {
		PBX_CMD = pBX_CMD;
	}

	@JsonGetter("PBX_PORTEUR")
	public String getPbxPorteur() {
		return PBX_PORTEUR;
	}

	@JsonSetter("PBX_PORTEUR")
	public void setPbxPorteur(String pBX_PORTEUR) {
		PBX_PORTEUR = pBX_PORTEUR;
	}

	@JsonGetter("PBX_RETOUR")
	public String getPbxRetour() {
		return PBX_RETOUR;
	}

	@JsonSetter("PBX_RETOUR")
	public void setPbxRetour(String pBX_RETOUR) {
		PBX_RETOUR = pBX_RETOUR;
	}

	@JsonGetter("PBX_HASH")
	public String getPbxHash() {
		return PBX_HASH;
	}

	@JsonSetter("PBX_HASH")
	public void setPbxHash(String pBX_HASH) {
		PBX_HASH = pBX_HASH;
	}

	@JsonGetter("PBX_TIME")
	public String getPbxTime() {
		return PBX_TIME;
	}

	@JsonSetter("PBX_TIME")
	public void setPbxTime(String pBX_TIME) {
		PBX_TIME = pBX_TIME;
	}

	@JsonGetter("PBX_HMAC")
	public String getPbxHMAC() {
		return PBX_HMAC;
	}

	@JsonSetter("PBX_HMAC")
	public void setPbxHMAC(String pBX_HMAC) {
		PBX_HMAC = pBX_HMAC;
	}

	@JsonGetter("PBX_EFFECTUE")
	public String getPbxEffectue() {
		return PBX_EFFECTUE;
	}

	@JsonSetter("PBX_EFFECTUE")
	public void setPbxEffectue(String pBX_EFFECTUE) {
		PBX_EFFECTUE = pBX_EFFECTUE;
	}

	@JsonGetter("PBX_REFUSE")
	public String getPbxRefuse() {
		return PBX_REFUSE;
	}

	@JsonSetter("PBX_REFUSE")
	public void setPbxRefuse(String pBX_REFUSE) {
		PBX_REFUSE = pBX_REFUSE;
	}

	@JsonGetter("PBX_ANNULE")
	public String getPbxAnnule() {
		return PBX_ANNULE;
	}

	@JsonSetter("PBX_ANNULE")
	public void setPbxAnnule(String pBX_ANNULE) {
		PBX_ANNULE = pBX_ANNULE;
	}

	@JsonGetter("PBX_ATTENTE")
	public String getPbxAttente() {
		return PBX_ATTENTE;
	}

	@JsonSetter("PBX_ATTENTE")
	public void setPbxAttente(String pBX_ATTENTE) {
		PBX_ATTENTE = pBX_ATTENTE;
	}

	@JsonGetter("PBX_REPONDRE_A")
	public String getPbxRepondreA() {
		return PBX_REPONDRE_A;
	}

	@JsonSetter("PBX_REPONDRE_A")
	public void setPbxRepondreA(String pBX_REPONDRE_A) {
		PBX_REPONDRE_A = pBX_REPONDRE_A;
	}

	@JsonGetter("PBX_2MONT1")
	public String getPBX_2MONT1() {
		return PBX_2MONT1;
	}

	@JsonSetter("PBX_2MONT1")
	public void setPBX_2MONT1(String pBX_2MONT1) {
		PBX_2MONT1 = pBX_2MONT1;
	}

	@JsonGetter("PBX_2MONT2")
	public String getPBX_2MONT2() {
		return PBX_2MONT2;
	}

	@JsonSetter("PBX_2MONT2")
	public void setPBX_2MONT2(String pBX_2MONT2) {
		PBX_2MONT2 = pBX_2MONT2;
	}

	@JsonGetter("PBX_2MONT3")
	public String getPBX_2MONT3() {
		return PBX_2MONT3;
	}

	@JsonSetter("PBX_2MONT3")
	public void setPBX_2MONT3(String pBX_2MONT3) {
		PBX_2MONT3 = pBX_2MONT3;
	}

	@Override
	public String toString() {
		return "MessagePaiement [PBX_SITE=" + PBX_SITE + ", PBX_RANG=" + PBX_RANG + ", PBX_IDENTIFIANT="
				+ PBX_IDENTIFIANT + ", PBX_TOTAL=" + PBX_TOTAL + ", PBX_DEVISE=" + PBX_DEVISE + ", PBX_CMD=" + PBX_CMD
				+ ", PBX_PORTEUR=" + PBX_PORTEUR + ", PBX_RETOUR=" + PBX_RETOUR + ", PBX_HASH=" + PBX_HASH
				+ ", PBX_TIME=" + PBX_TIME + ", PBX_HMAC=" + PBX_HMAC + ", PBX_EFFECTUE=" + PBX_EFFECTUE
				+ ", PBX_REFUSE=" + PBX_REFUSE + ", PBX_ANNULE=" + PBX_ANNULE + ", PBX_ATTENTE=" + PBX_ATTENTE
				+ ", PBX_REPONDRE_A=" + PBX_REPONDRE_A + ", PBX_2MONT1=" + PBX_2MONT1 + ", PBX_2MONT2=" + PBX_2MONT2
				+ ", PBX_2MONT3=" + PBX_2MONT3 + "]";
	}

}
