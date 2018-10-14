package payboxtestserver;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.paris.lutece.plugins.paybox.PayboxUtil;
import io.micronaut.context.annotation.Factory;

@Factory
public class PrivateKeyFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrivateKeyFactory.class);
	
	@Singleton
	PrivateKey privateKey() {
		try {
			File file = new File("/work/prvkey.der");
			if (!file.exists()) {
				String keyFilePath = getClass().getClassLoader().getResource("prvkey.der").getFile();
				file = new File(keyFilePath);
			}
			return PayboxUtil.getKey(file);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			LOGGER.error("Error during paybox key loading", e);
			return null;
		}
	}
}
