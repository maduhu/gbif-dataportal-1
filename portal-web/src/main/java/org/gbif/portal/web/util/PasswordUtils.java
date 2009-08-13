/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.web.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Utils concerned with passwords
 * 
 * @author dmartin
 */
public class PasswordUtils {
	
	public static final String encryptKey = "BAA5BD17-4788-4B05-88A1-4250CCA2665D";	
	
	/**
	 * Used to encrypt and decrypt passwords passed in emails.
	 * @param password
	 * @param encrypt
	 * @return
	 * @throws Exception
	 */
	public static String encryptPassword(String password, boolean encrypt) throws Exception{
	    
		PBEKeySpec pbeKeySpec;
	    PBEParameterSpec pbeParamSpec;
	    SecretKeyFactory keyFac;

	    // Salt
	    byte[] salt = {
	        (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
	        (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
	    };

	    // Iteration count
	    int count = 20;

	    // Create PBE parameter set
	    pbeParamSpec = new PBEParameterSpec(salt, count);

	    // Prompt user for encryption password.
	    // Collect user password as char array (using the
	    // "readPasswd" method from above), and convert
	    // it into a SecretKey object, using a PBE key
	    // factory.
	    pbeKeySpec = new PBEKeySpec(encryptKey.toCharArray());
	    keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
	    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

	    // Create PBE Cipher
	    Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

	    // Initialize PBE Cipher with key and parameters
	    if(encrypt)
	    	pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
	    else
	    	pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

	    if(encrypt){
		    // Our cleartext
		    byte[] cleartext = password.getBytes();
		    // Encrypt the cleartext
		    byte[] ciphertext = pbeCipher.doFinal(cleartext);
	    	return PasswordUtils.byteArrayToHexString(ciphertext);
	    } else {
		    // Our cleartext
		    byte[] cleartext = PasswordUtils.hexStringToByteArray(password);
		    // Encrypt the cleartext
		    byte[] ciphertext = pbeCipher.doFinal(cleartext);
	    	return new String(ciphertext);
	    }
	}	
	
	public static String byteArrayToHexString(byte[] b){
	    StringBuffer sb = new StringBuffer(b.length * 2);
	    for (int i = 0; i < b.length; i++){
	      int v = b[i] & 0xff;
	      if (v < 16) {
	        sb.append('0');
	      }
	      sb.append(Integer.toHexString(v));
	    }
	    return sb.toString().toUpperCase();
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    byte[] b = new byte[s.length() / 2];
	    for (int i = 0; i < b.length; i++){
	      int index = i * 2;
	      int v = Integer.parseInt(s.substring(index, index + 2), 16);
	      b[i] = (byte)v;
	    }
	    return b;
	 }

	/**
	 * @return the encryptKey
	 */
	public static String getEncryptKey() {
		return encryptKey;
	}
}