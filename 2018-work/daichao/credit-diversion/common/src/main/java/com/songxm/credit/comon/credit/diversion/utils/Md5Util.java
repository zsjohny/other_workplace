package com.songxm.credit.comon.credit.diversion.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Md5Util {
	private static MessageDigest sMd5MessageDigest;
	private static StringBuilder sStringBuilder;

	static {
		try {
			sMd5MessageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		sStringBuilder = new StringBuilder();
	}

	public static String md5(String s) {
		sMd5MessageDigest.reset();
		sMd5MessageDigest.update(s.getBytes());
		byte digest[] = sMd5MessageDigest.digest();
		sStringBuilder.setLength(0);
		for (int i = 0; i < digest.length; i++) {
			final int b = digest[i] & 255;
			if (b < 16) {
				sStringBuilder.append('0');
			}
			sStringBuilder.append(Integer.toHexString(b));
		}
		return sStringBuilder.toString().toLowerCase();
	}
	
	/**
	 * 对原始密码根据盐值进行两次md5加密
	 * 
	 * @param password	原始密码
	 * @param salt		盐值
	 * @return
	 */
	public static String password(String password,String salt){
		return md5(md5(password)+salt);
	}
	
	/**
	 * 获取6位数随机盐值
	 */
	public static String getSalt(){
		
		String chars = "123456789abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<6;i++){
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}
	
	
}
