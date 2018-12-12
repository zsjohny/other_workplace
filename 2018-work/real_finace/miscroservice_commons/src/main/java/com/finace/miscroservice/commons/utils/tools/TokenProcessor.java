package com.finace.miscroservice.commons.utils.tools;

public class TokenProcessor {
	private TokenProcessor() {
	}

	private static TokenProcessor instance = new TokenProcessor();

	public static TokenProcessor getInstance() {
		return instance;
	}

	/**
	 * token生成器  dixed 为true时生成固定token，为flase时为动态token
	 * @param userId
	 * @param passwd
	 * @param dixed
	 * @return
	 */
	public String generateTokeCode(String userId, String passwd, boolean dixed) {
		String value = userId + passwd + "";
		if(dixed){
			value += System.currentTimeMillis();
		}
//		long currentTime = System.currentTimeMillis();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
//		Date date = new Date(currentTime);
		// 获取数据指纹，指纹是唯一的
		try {
//			MessageDigest md = MessageDigest.getInstance("md5");
//			byte[] b = md.digest(value.getBytes());// 产生数据的指纹
//			// Base64编码
//			BASE64Encoder be = new BASE64Encoder();
//			return be.encode(b);// 制定一个编码
			return MD5Util.getLowercaseMD5(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 红包使用时需要用道德token
	 */
	public String getHbTokeCode(String userId, String borrowId) {
		String value = userId + borrowId + System.currentTimeMillis();
//		long currentTime = System.currentTimeMillis();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
//		Date date = new Date(currentTime);
		// 获取数据指纹，指纹是唯一的
		try {
//			MessageDigest md = MessageDigest.getInstance("md5");
//			byte[] b = md.digest(value.getBytes());// 产生数据的指纹
//			// Base64编码
//			BASE64Encoder be = new BASE64Encoder();
//			return be.encode(b);// 制定一个编码
			return MD5Util.getLowercaseMD5(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	public static void main(String[] args) {
		TokenProcessor processor = new TokenProcessor();
//		processor.generateTokeCode();
	}
}