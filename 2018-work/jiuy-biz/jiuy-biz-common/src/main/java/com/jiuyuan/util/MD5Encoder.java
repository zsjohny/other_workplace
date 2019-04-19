/**
 * 
 */
package com.jiuyuan.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author LiuWeisheng
 *
 */
public class MD5Encoder {
	/**
	 * encode the source string with MD5 algorithm
	 * @param source
	 * @return
	 */
	public static String encode(String source){
		if(null == source){
			return null;
		}
		byte [] buf = source.getBytes();
		StringBuilder sb = new StringBuilder();
        MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(buf);
	        byte [] tmp = md5.digest();
	        for (byte b:tmp) {
	            sb.append(Integer.toHexString(b&0xff));
	        }
		} catch (NoSuchAlgorithmException e) {
			// doing nothing here 
		}
        return sb.toString(); 		
	}
}
