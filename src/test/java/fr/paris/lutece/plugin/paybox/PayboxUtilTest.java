package fr.paris.lutece.plugin.paybox;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import fr.paris.lutece.plugins.paybox.PayboxUtil;

public class PayboxUtilTest {

	@Test
	public void sign() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException, SignatureException {
		String toSign = "Mt=1990&"
				+ "Ref=CMD9542124-01A5G&"
				+ "Auto=NUM_AUTORISATION&"
				+ "Appel=NUM_APPEL_PAYBOX&"
				+ "Abo=NUM_ABONNEMENT&"
				+ "Reponse=REPLACE_THIS_VALUE&"
				+ "Transaction=NUM_TRANSACTION&"
				+ "Pays=FR";
		
		String keyFilePath = getClass().getClassLoader().getResource("prvkey.der").getFile();
		PrivateKey privateKey = PayboxUtil.getKey(keyFilePath);
		assertThat(PayboxUtil.sign(toSign, privateKey),
				IsEqual.equalTo("aRIeACWSbnMvIdiD4t0N6rkuz5uowr1poNyuYhDxPxDFKQVIke53RL+DM2weKF7nrjfJNRr+Ll/TNzUcVnAFwJRdtPb9dzjFOQ1NFh4GhntsIvs8ngzZyqDMzH+ZiIRmyEwz2ZkHpBiw8LLMUuDIXXU22CxfcE9M2W5HZ8cWMI8="));
	
	}

}
