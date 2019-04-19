package com.qianmi.open.api.tool.util;

import com.qianmi.open.api.ApiException;
import com.qianmi.open.api.Constants;
import com.qianmi.open.api.QianmiResponse;
import com.qianmi.open.api.tool.parser.json.ObjectJsonParser;
import com.qianmi.open.api.tool.util.json.JSONReader;
import com.qianmi.open.api.tool.util.json.JSONValidatingReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 系统工具类。
 */
public abstract class ApiUtils {

    private static String localIp;
	private ApiUtils() {}

	private static List<String> ignoreList;

	static {
		ignoreList = new ArrayList<String>();
		ignoreList.add("file");
		ignoreList.add("image");
		ignoreList.add("logo");
	}

	/**
	 * 给请求签名。
	 * 
	 * @param requestHolder 所有字符型的请求参数
	 * @param secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String signRequest(RequestParametersHolder requestHolder, String secret) throws IOException {
		// 第一步：检查参数是否已经排序
		Map<String, String> params = requestHolder.getAllParams();
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder(secret);
		for (String key : keys) {
			if (ignoreList.contains(key)) {
				continue;
			}
			String value = params.get(key);
			if (StringUtils.areNotEmpty(key, value) && !ignoreList.contains(key)) {
				query.append(key).append(value);
			}
		}

		// 第三步：使用MD5加密
		byte[] bytes = encryptMD5(query.toString());

		// 第四步：把二进制转化为大写的十六进制
		return byte2hex(bytes);
	}

	/**
	 * 给请求签名。
	 * 
	 * @param requestHolder 所有字符型的请求参数
	 * @param secret 签名密钥
	 * @param isHmac 是否为HMAC方式加密
	 * @return 签名
	 * @throws IOException
	 */
	public static String signRequestNew(RequestParametersHolder requestHolder, String secret, boolean isHmac) throws IOException {
		return signRequestNew(requestHolder.getAllParams(), secret, isHmac);
	}

	public static String signRequestNew(Map<String, String> params, String secret, boolean isHmac) throws IOException {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder();
		if (!isHmac) {
			query.append(secret);
		}
		for (String key : keys) {
			String value = params.get(key);
			if (StringUtils.areNotEmpty(key, value) && !ignoreList.contains(key)) {
				query.append(key).append(value);
			}
		}

		// 第三步：使用MD5/HMAC加密
		byte[] bytes;
		if (isHmac) {
			bytes = encryptHMAC(query.toString(), secret);
		} else {
			query.append(secret);
			bytes = encryptMD5(query.toString());
		}

		// 第四步：把二进制转化为大写的十六进制
		return byte2hex(bytes);
	}

	private static byte[] encryptHMAC(String data, String secret) throws IOException {
		byte[] bytes = null;
		try {
			SecretKey secretKey = new SecretKeySpec(secret.getBytes(Constants.CHARSET_UTF8), "HmacMD5");
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			bytes = mac.doFinal(data.getBytes(Constants.CHARSET_UTF8));
		} catch (GeneralSecurityException gse) {
			String msg=getStringFromException(gse);
			throw new IOException(msg);
		}
		return bytes;
	}

	private static String getStringFromException(Throwable e) {
		String result = "";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		e.printStackTrace(ps);
		try {
			result = bos.toString(Constants.CHARSET_UTF8);
		} catch (IOException ioe) {
		}
		return result;
	}

	private static byte[] encryptMD5(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes(Constants.CHARSET_UTF8));
		} catch (GeneralSecurityException gse) {
			String msg = getStringFromException(gse);
			throw new IOException(msg);
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

	/**
	 * 验证回调地址的签名是否合法。要求所有参数均为已URL反编码的。
	 * 
	 * @param params 私有参数（未经BASE64解密）
	 * @param session 私有会话码
	 * @param sign 回调签名
	 * @param appKey 应用公钥
	 * @param appSecret 应用密钥
	 * @return 验证成功返回true，否则返回false
	 * @throws IOException
	 */
	public static boolean verifyResponse(String params, String session, String sign,
			String appKey, String appSecret) throws IOException {
		StringBuilder result = new StringBuilder();
		result.append(appKey).append(params).append(session).append(appSecret);
		byte[] bytes = encryptMD5(result.toString());
		return Base64.encodeToString(bytes, false).equals(sign);
	}

	/**
	 * 获取文件的真实后缀名。目前只支持JPG, GIF, PNG, BMP四种图片文件。
	 * 
	 * @param bytes 文件字节流
	 * @return JPG, GIF, PNG or null
	 */
	public static String getFileSuffix(byte[] bytes) {
		if (bytes == null || bytes.length < 10) {
			return null;
		}

		if (bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F') {
			return "GIF";
		} else if (bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G') {
			return "PNG";
		} else if (bytes[6] == 'J' && bytes[7] == 'F' && bytes[8] == 'I' && bytes[9] == 'F') {
			return "JPG";
		} else if (bytes[0] == 'B' && bytes[1] == 'M') {
			return "BMP";
		} else {
			return null;
		}
	}

	/**
	 * 获取文件的真实媒体类型。目前只支持JPG, GIF, PNG, BMP四种图片文件。
	 * 
	 * @param bytes 文件字节流
	 * @return 媒体类型(MEME-TYPE)
	 */
	public static String getMimeType(byte[] bytes) {
		String suffix = getFileSuffix(bytes);
		String mimeType;

		if ("JPG".equals(suffix)) {
			mimeType = "image/jpeg";
		} else if ("GIF".equals(suffix)) {
			mimeType = "image/gif";
		} else if ("PNG".equals(suffix)) {
			mimeType = "image/png";
		} else if ("BMP".equals(suffix)) {
			mimeType = "image/bmp";
		}else {
			mimeType = "application/octet-stream";
		}

		return mimeType;
	}

	/**
	 * 清除字典中值为空的项。
	 * 
	 * @param <V> 泛型
	 * @param map 待清除的字典
	 * @return 清除后的字典
	 */
	public static <V> Map<String, V> cleanupMap(Map<String, V> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}

		Map<String, V> result = new HashMap<String, V>(map.size());
		Set<Entry<String, V>> entries = map.entrySet();

		for (Entry<String, V> entry : entries) {
			if (entry.getValue() != null) {
				result.put(entry.getKey(), entry.getValue());
			}
		}

		return result;
	}

	/**
	 * 把JSON字符串转化为Map结构。
	 * 
	 * @param body JSON字符串
	 * @return Map结构
	 */
	public static Map<?, ?> parseJson(String body) {
		JSONReader jr = new JSONValidatingReader();
		Object obj = jr.read(body);
		if (obj instanceof Map<?, ?>) {
			return (Map<?, ?>) obj;
		} else {
			return null;
		}
	}

	/**
	 * 把JSON字符串解释为对象结构。
	 * 
	 * @param <T> API响应类型
	 * @param json JSON字符串
	 * @param clazz API响应类
	 * @return API响应对象
	 */
	public static <T extends QianmiResponse> T parseResponse(String json, Class<T> clazz) throws ApiException {
		ObjectJsonParser<T> parser = new ObjectJsonParser<T>(clazz);
		T rsp = parser.parse(json);
		rsp.setBody(json);
		return rsp;
	}

	/**
	 * 获取本机的网络IP
	 */
	public static String getLocalNetWorkIp() {
		if (localIp != null) {
			return localIp;
		}
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {// 遍历所有的网卡
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				if (ni.isLoopback() || ni.isVirtual()) {// 如果是回环和虚拟网络地址的话继续
					continue;
				}
				Enumeration<InetAddress> addresss = ni.getInetAddresses();
				while (addresss.hasMoreElements()) {
					InetAddress address = addresss.nextElement();
					if (address instanceof Inet4Address) {// 这里暂时只获取ipv4地址
						ip = address;
						break;
					}
				}
				if (ip != null) {
					break;
				}
			}
			if (ip != null) {
				localIp = ip.getHostAddress();
			} else {
				localIp = "127.0.0.1";
			}
		} catch (Exception e) {
			localIp = "127.0.0.1";
		}
		return localIp;
	}

}
