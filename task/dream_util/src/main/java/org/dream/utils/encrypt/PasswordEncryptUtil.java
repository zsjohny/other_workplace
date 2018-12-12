/**
 * 
 */
package org.dream.utils.encrypt;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

/**
 * @author Boyce
 * 下午5:48:52
 */
public class PasswordEncryptUtil {
	public static  String digest(String pass, String salt, int iterations) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("md5");
		

		if (salt != null) {
			digest.update(salt.getBytes("UTF-8"));
		}

		byte[] result = digest.digest(pass.getBytes("UTF-8"));

		for (int i = 1; i < iterations; i++) {
			digest.reset();
			result = digest.digest(result);
		}
		return Hex.encodeHexString(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
