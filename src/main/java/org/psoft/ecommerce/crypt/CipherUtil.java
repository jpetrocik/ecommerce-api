package org.psoft.ecommerce.crypt;

import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CipherUtil {

	private static SecretKey desKey;

	private static void init() throws Exception {

		if (desKey != null)
			return;

		InputStream in = CipherUtil.class.getResourceAsStream("/des.key");
		byte[] keyBuffer = new byte[in.available()];
		in.read(keyBuffer);
		in.close();

		SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
		DESKeySpec ks = new DESKeySpec(keyBuffer);
		desKey = kf.generateSecret(ks);
	}

	public static synchronized byte[] encrypt(String text) throws Exception {

		if (text == null)
			return null;

		init();

		Cipher desCipher = Cipher.getInstance("DES");
		// TODO More spec
		desCipher.init(Cipher.ENCRYPT_MODE, desKey);

		// Our cleartext
		byte[] cleartext = text.getBytes();

		// Encrypt the cleartext
		byte[] ciphertext = desCipher.doFinal(cleartext);

		return ciphertext;

	}

	public static synchronized String decrypt(byte[] text) throws Exception {

		if (text == null)
			return null;

		init();

		byte[] ciphertext = text;

		Cipher desCipher = Cipher.getInstance("DES");

		// Initialize the same cipher for decryption
		desCipher.init(Cipher.DECRYPT_MODE, desKey);

		// Decrypt the ciphertext
		byte[] cleartext1 = desCipher.doFinal(ciphertext);

		return new String(cleartext1);
	}
}
