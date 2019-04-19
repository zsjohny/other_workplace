
package com.jiuy.base.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PWDCrypt {
	public static final String Unix = "Unix";
	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA-1";
	public static final String SHA256 = "SHA-256";
	public static final String SHA384 = "SHA-384";
	public static final String SHA512 = "SHA-512";

	private static Map<String, String> KeyMap;
	private static Map<String, String> SeedMap;

	private static List<String> DigestAlgorithms;
	private String key;

	static {
		KeyMap = new HashMap<String, String>();
		KeyMap.put("RO", Unix);
		KeyMap.put("MD", MD5);
		KeyMap.put("RS", SHA1);
		KeyMap.put("RH", SHA256);
		KeyMap.put("RA", SHA384);
		KeyMap.put("R5", SHA512);

		SeedMap = new HashMap<String, String>();
		SeedMap.put(Unix, "RO");
		SeedMap.put(MD5, "MD");
		SeedMap.put(SHA1, "RS");
		SeedMap.put(SHA256, "RH");
		SeedMap.put(SHA384, "RA");
		SeedMap.put(SHA512, "R5");

		DigestAlgorithms = new ArrayList<String>();
		DigestAlgorithms.add(MD5);
		DigestAlgorithms.add(SHA1);
		DigestAlgorithms.add(SHA256);
		DigestAlgorithms.add(SHA384);
		DigestAlgorithms.add(SHA512);
	}

	public PWDCrypt(String key) {
		this.key = key;
	}

	public PWDCrypt() {
		this.key = "MD5";
	}

	public final String crypt(String input) {
		if (input == null || input.equals("")) {
			return input;
		}
		key = key == null ? "MD5" : key;
		String secret = null;
		if (key.equals(Unix)) {
			secret = UnixPwdCrypt.crypt("RO", input);
		} else if (DigestAlgorithms.contains(key)) {
			secret = messageDigest(key, input, null);
			secret = SeedMap.get(key).toString() + secret;
		} else if (key.equals("RD")) {// ����ԭ����ǰ��׼ȷ��MD5���ܷ�ʽ
			secret = messageDigest("MD5", input, "rone");
			secret = "RD" + secret;
		}

		return secret;
	}

	public static final boolean validate(String orginal, String secret) {
		if (orginal == null && secret == null) {
			if (orginal == null && secret == null) {
				return true;
			} else {
				return false;
			}
		}

		String key = getAlgorithm(secret);
		key = key == null ? "MD5" : key;
		PWDCrypt crypt = new PWDCrypt(key);
		return secret.equals(crypt.crypt(orginal));
	}

	/**
	 * ��������жϵ�ǰ���õļ��ܷ�ʽ��������R1ʹ��
	 * 
	 * @param secret
	 * @return
	 */
	public static final String getAlgorithm(String secret) {
		if (secret == null || secret.length() < 3) {
			return null;
		}

		String seed = secret.substring(0, 2);
		if (seed.equals("RD"))
			return "RD";
		if (KeyMap.containsKey(seed)) {
			return (String) KeyMap.get(seed);
		}
		return null;
	}

	/**
	 * ���ò��Գ��㷨
	 * 
	 * @param key
	 * @param input
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private String messageDigest(String key, String input, String encode) {
		byte[] data = null;
		try {
			data = input.getBytes("UTF8");
			MessageDigest md = MessageDigest.getInstance(key);
			md.update(data);
			data = md.digest();
		} catch (Exception e) {
		}

		if (encode != null && !encode.trim().equals("") && encode.equals("rone")) {
			BigInteger bigInteger = new BigInteger(data);
			return bigInteger.toString(16);
		}

		StringBuffer encodeBuf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			if (Integer.toHexString(0xFF & data[i]).length() == 1)
				encodeBuf.append("0").append(Integer.toHexString(0xFF & data[i]));
			else
				encodeBuf.append(Integer.toHexString(0xFF & data[i]));
		}

		return encodeBuf.toString();
	}

	//
	// /**
	// * ��ʼ��HMAC��Կ
	// *
	// * @return
	// * @throws Exception
	// */
	// public static String initMacKey() throws Exception {
	// KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
	// SecretKey secretKey = keyGenerator.generateKey();
	// return encryptBASE64(secretKey.getEncoded());
	// }
	//
	// /**
	// * HMAC����
	// *
	// * @param data
	// * @param key
	// * @return
	// * @throws Exception
	// */
	// public static byte[] encryptHMAC(byte[] data, String key) throws
	// Exception {
	//
	// SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
	// Mac mac = Mac.getInstance(secretKey.getAlgorithm());
	// mac.init(secretKey);
	//
	// return mac.doFinal(data);
	//
	// }

	public static void main(String[] args) {

		System.out.println(new PWDCrypt().crypt("1"));
	}

}
