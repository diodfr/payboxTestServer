/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.paybox;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


/**
 *
 * Utility class, provides help to create links to paybox and validates
 * answers from paybox.
 */
public final class PayboxUtil
{
	/** UTF-8 Charset. */
	private static final String CHARSET = "utf-8";

	/** Public Key encryption algorithm. */
	private static final String ENCRYPTION_ALGORITHM = "RSA";

	/** Signature hash encryption algorithm. */
	private static final String HASH_ENCRYPTION_ALGORITHM = "SHA1withRSA";

	/**
	 * private constructor.
	 */
	private PayboxUtil(  )
	{
	}

	/**
	 *
	 * Get private key at specified path.
	 *
	 * @param keyPath private key path.
	 * @return private key object.
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InvalidKeySpecException the invalid key spec exception
	 */
	public static final PrivateKey getKey( final String keyPath )
			throws NoSuchAlgorithmException, IOException, InvalidKeySpecException
	{
		return getKey(new File( keyPath ));
	}

	public static final PrivateKey getKey( final File keyFile )
			throws NoSuchAlgorithmException, IOException, InvalidKeySpecException
	{
		final KeyFactory keyFactory = KeyFactory.getInstance( PayboxUtil.ENCRYPTION_ALGORITHM );

		try (final FileInputStream fis = new FileInputStream(keyFile)) {
			try (DataInputStream dis = new DataInputStream(fis)) {
				byte[] privateKey = new byte[(int) keyFile.length()];
				dis.readFully(privateKey);
				final PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey);

				return keyFactory.generatePrivate( privateKeySpec );
			}
		}
	}

	public static final String sign(final String message, final PrivateKey privateKey) throws SignatureException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		final Signature sig = Signature.getInstance(PayboxUtil.HASH_ENCRYPTION_ALGORITHM);
		sig.initSign(privateKey);
		sig.update(message.getBytes(PayboxUtil.CHARSET));

		byte[] signature = sig.sign();
		return Base64.getEncoder().encodeToString(ByteBuffer.wrap(signature).array());
	}
}