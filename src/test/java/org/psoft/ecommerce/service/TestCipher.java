package org.psoft.ecommerce.service;

import junit.framework.TestCase;

import org.psoft.ecommerce.crypt.CipherUtil;

public class TestCipher extends TestCase {

	public void testEncryptDecrypt() throws Exception {

		String cleartext = "4055011111111111";
		byte[] ciphertext = CipherUtil.encrypt(cleartext);

		String decrypttext = CipherUtil.decrypt(ciphertext);

		assertTrue(cleartext.equals(decrypttext));

	}

}
