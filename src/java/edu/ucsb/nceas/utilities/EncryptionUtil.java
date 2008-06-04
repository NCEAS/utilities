/**
 *  '$RCSfile: EncryptionUtil.java,v $'
 *  Copyright: 2008 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-06-04 18:51:16 $'
 * '$Revision: 1.1.2.1 $'
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
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;


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

	public static void createNewKeyFile(String keyFileName) {
		File keyFile = new File(keyFileName);
		if (keyFile.exists()) {
			throw new KeyFileExistsException(
					"Cannot create encryption key file when one already exists: "
							+ keyFileName);
		}
		
		SecretKey key = generateKey();
		writeKey(key, keyfile);
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


