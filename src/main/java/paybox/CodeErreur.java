package paybox;

public enum CodeErreur {
	OK("00000"), // Opération réussie.
	FAIL_CONNECT("00001"),  /* La connexion au centre d’autorisation a échoué ou une erreur interne est survenue.
	Dans ce cas, il est souhaitable de faire une tentative sur le site secondaire :
	tpeweb1.paybox.com.*/
	FAIL_REFUSE_INVALIDE("00112"), /*001xx Paiement refusé par le centre d’autorisation [voir §12.1 Codes réponses du centre
	d’autorisation].
	En cas d’autorisation de la transaction par le centre d’autorisation de la banque ou de
	l’établissement financier privatif, le code erreur “00100” sera en fait remplacé
	directement par “00000”.*/
	FAIL_TRY_OTHER_SITE("00003"), /*Erreur Paybox. Dans ce cas, il est souhaitable de faire une tentative sur le site
	secondaire QDN tpeweb1.paybox.com.*/
	FAIL_INV_NUM_PORT_OR_CRYPT("00004"), // Numéro de porteur ou cryptogramme visuel invalide.
	FAIL_ACCESS_DENIED("00006"), // Accès refusé ou site/rang/identifiant incorrect.
	FAIL_INV_DATE_VALID("00008"), // Date de fin de validité incorrecte.
	FAIL_SUBSCRIPTION_CREATION("00009"), // Erreur de création d’un abonnement.
	FAIL_UNKOWN_CURRENCY("00010"), // Devise inconnue.
	FAIL_INV_AMOUNT("00011"), // Montant incorrect.
	FAIL_ALREADY_DONE_PAYMENT("00015"), // Paiement déjà effectué.
	FAIL_ALREADY_SUBSCRIBED("00016"), // Abonné déjà existant ("inscription nouvel abonné"). Utilisation de la valeur ‘U’ dans la variable PBX_RETOUR.
	FAIL_NOT_ALLOWED_CARD("0021"), // Carte non autorisée.
	FAIL_NOT_CONFORM_CARD("00029"), // Carte non conforme. Code erreur renvoyé lors de la documentation de la variable « PBX_EMPREINTE ».
	FAIL_TIMEOUT("00030"), //Temps d’attente supérieur au délai maximal par l’internaute/acheteur au niveau de la page de paiements. Délais de 15 min par défaut, ou définit dans la variable PBX_DISPLAY
	FAIL_RESERVED("00031"), //Réservé
	FAIL_RESERVED_1("00032"), //Réservé
	FAIL_INVALID_IP_COUNTRY("00033"), // Code pays de l’adresse IP du navigateur de l’acheteur non autorisé.
	FAIL_3D_SECURE_MANDATORY("00040"), // Opération sans authentification 3-D Secure, bloquée par le filtre.
	FAIL_WAITING_FOR_VALIDATION("99999") // Opération en attente de validation par l’émetteur du moyen de paiement.
	; 
	
	private String code;
	
	private CodeErreur(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
