package com.jfinal.weixin.jiuy.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 请求参数签名工具类
 * 加密方法：
 *
 * 1.给app分配对应的key、secret
 * 2.Sign签名，调用API 时需要对请求参数进行签名验证，签名方式如下：
 * 		a. 按照请求参数名称将所有请求参数按照字母先后顺序排序得到：keyvaluekeyvalue...keyvalue
 * 			字符串如：将arong=1,mrong=2,crong=3 排序为：arong=1, crong=3,mrong=2 然后将参数名和参数值进行拼接得到参数
 * 			字符串：arong1crong3mrong2。
 * 		b. 将secret加在参数字符串的头部和尾部后进行MD5加密 ,加密后的字符串需大写。即得到签名sign。例子secret为yjj2018则参数字符串为yjj2018arong1crong3mrong2yjj2018
		SignHashMap:{appSign=EB9A352AAA23C416378E372B47807D45, appParam=yjj2018a01b03c02yjj2018}
		SignHashMap2:{appSign=FD12D2C3EAFC9AE542B7A845DA14D9A2, appParam=yjj2018b03c02yjj2018}
 */
public class ParamSignUtils {
	private static final Logger logger = LoggerFactory.getLogger(ParamSignUtils.class);

	private static final String secret ="yjj2018";
	public static void main(String[] args)
	{
		HashMap<String, String> signMap = new HashMap<String, String>();
		signMap.put("a","01");
		signMap.put("c","02");
		signMap.put("b","03");
		String secret="yjj2018";
		HashMap SignHashMap=ParamSignUtils.sign(signMap, secret);
		System.out.println("SignHashMap:"+SignHashMap);
		List<String> ignoreParamNames=new ArrayList<String>();
		ignoreParamNames.add("a");
		HashMap SignHashMap2=ParamSignUtils.sign(signMap,ignoreParamNames, secret);
		System.out.println("SignHashMap2:"+SignHashMap2);
	}

	public static String  getSign(Map<String, String> paramValues) {
	    logger.info ("获取签名 paramValues[{}].secret[{}].", paramValues, secret);
		HashMap<String, String> map = sign(paramValues, secret);
		logger.info("getSign [{}].", map);
		return map.get("appSign");
	}

	public static HashMap<String, String> sign(Map<String, String> paramValues,String secret) {
		return sign(paramValues, null, secret);
	}

	/**
	 * yjj2018a01b03c02yjj2018
	 * @param paramValues
	 * @param ignoreParamNames
	 * @param secret
	 * @return
	 */
	public static HashMap<String, String> sign(Map<String, String> paramValues,
			List<String> ignoreParamNames, String secret) {
		try {
			HashMap<String, String> signMap = new HashMap<String, String>();
			StringBuilder sb = new StringBuilder();
			List<String> paramNames = new ArrayList<String>(paramValues.size());
			paramNames.addAll(paramValues.keySet());
			if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
				for (String ignoreParamName : ignoreParamNames) {
					paramNames.remove(ignoreParamName);
				}
			}
			Collections.sort(paramNames);
			sb.append(secret);
			for (String paramName : paramNames) {
				sb.append(paramName).append(paramValues.get(paramName));
			}
			sb.append(secret);
			byte[] md5Digest = getMD5Digest(sb.toString());
			String sign = byte2hex(md5Digest);
			signMap.put("appParam", sb.toString());
			signMap.put("appSign", sign);
			return signMap;
		} catch (IOException e) {
			throw new RuntimeException("加密签名计算错误", e);
		}

	}

	public static String utf8Encoding(String value, String sourceCharsetName) {
		try {
			return new String(value.getBytes(sourceCharsetName), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static byte[] getSHA1Digest(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			bytes = md.digest(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse);
		}
		return bytes;
	}

	private static byte[] getMD5Digest(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse);
		}
		return bytes;
	}


	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}

}
