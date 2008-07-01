/**
 *  '$RCSfile: EncryptionUtil.java,v $'
 *  Copyright: 2008 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-01 17:35:17 $'
 * '$Revision: 1.1.2.2 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package edu.ucsb.nceas.utilities;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;


/**
 *  General static utilities for IO operations
 */
public class EncryptionUtil                                         
{
    
    /**
	 *  constructor
	 */
	private EncryptionUtil() {
	}

	private static String algorithm = "DESede";
	private static Cipher cipher = null;

	public static byte[] encrypt(Key key, String input) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException  {
		if (cipher == null) {
			createCipher();
		}
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] inputBytes = input.getBytes();
		return cipher.doFinal(inputBytes);
	}

	public static String decrypt(Key key, byte[] encryptionBytes)
			throws InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
		if (cipher == null) {
			createCipher();
		}
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
		String recovered = new String(recoveredBytes);
		return recovered;
	}

	public static void createNewKeyFile(String keyFileName)
			throws NoSuchAlgorithmException, KeyFileExistsException,
			InvalidKeySpecException, IOException {
		File keyFile = new File(keyFileName);
		if (keyFile.exists()) {
			throw new KeyFileExistsException(
					"Cannot create encryption key file when one already exists: "
							+ keyFileName);
		}

		SecretKey key = generateKey();
		writeKeyToFile(key, keyFile);
	}
	
    /** Generate a secret TripleDES encryption/decryption key */
	public static SecretKey generateKey() throws NoSuchAlgorithmException {
		// Get a key generator for Triple DES (a.k.a DESede)
		KeyGenerator keygen = KeyGenerator.getInstance("DESede");
		// Use it to generate a key
		return keygen.generateKey();
	}

	
	/** Read a TripleDES secret key from the specified file */
	public static SecretKey readKeyFromFile(String keyFileName) throws IOException,
			NoSuchAlgorithmException, InvalidKeyException,
			InvalidKeySpecException {
		// Read the raw bytes from the keyfile
		File keyFile = new File(keyFileName);
		DataInputStream in = new DataInputStream(new FileInputStream(keyFile));
		byte[] rawkey = new byte[(int) keyFile.length()];
		in.readFully(rawkey);
		in.close();

		// Convert the raw bytes to a secret key like this
		DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		SecretKey key = keyfactory.generateSecret(keyspec);
		return key;
	}
	
    /** Save the specified TripleDES SecretKey to the specified file */
	public static void writeKeyToFile(SecretKey key, File f)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		// Convert the secret key to an array of bytes like this
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		DESedeKeySpec keyspec = (DESedeKeySpec) keyfactory.getKeySpec(key,
				DESedeKeySpec.class);
		byte[] rawkey = keyspec.getKey();

		// Write the raw key to the file
		FileOutputStream out = new FileOutputStream(f);
		out.write(rawkey);
		out.close();
	}

	
	private static void createCipher() throws NoSuchPaddingException,
			NoSuchAlgorithmException {
		try {
			cipher = Cipher.getInstance(algorithm);
		} catch (GeneralSecurityException gse) {
			// An exception here probably means the JCE provider hasn't
			// been permanently installed on this system by listing it
			// in the $JAVA_HOME/jre/lib/security/java.security file.
			// Therefore, we have to install the JCE provider explicitly.
			Provider sunjce = new com.sun.crypto.provider.SunJCE();
			Security.addProvider(sunjce);
			cipher = Cipher.getInstance(algorithm);
		}
	}
}


