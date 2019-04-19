/**
 * 发送短信功能
 */
package com.jiuyuan.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @author LWS
 *
 */
public final class SMSUtil {

	private static final Logger logger = Logger.getLogger(SMSUtil.class);
	private static final String SENDCLOUD_SMS_URL = "http://sendcloud.sohu.com/smsapi/send";
	private static final String SENDCLOUD_SMS_USER = "helper";
	private static final String SENDCLOUD_SMS_KEY = "5gdaMNY7Owlfo7OS";
	private static final String SENDCLOUD_TEMPLATE_ID = "俞姐姐网站";
	private static final String SENDCLOUD_RANDOMCODE_NAME = "";

	// 构造httpClinet，供多线程环境使用
	private static HttpClient httpClient = null;
	static {
		HttpClientBuilder builder = HttpClientBuilder.create();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(50);
		cm.setDefaultMaxPerRoute(20);
		builder.setConnectionManager(cm);
		httpClient = builder.build();
	}

	/**
	 * 发送短信随机码
	 * 
	 * @param phone
	 * @param randomCode
	 */
	public static void send(String phone, String randomCode) {
		HttpPost httpost = new HttpPost(SENDCLOUD_SMS_URL);
		String signature = getSignature(phone, randomCode);
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		// 设置短信接入参数
		nvps.add(new BasicNameValuePair("smsUser", SENDCLOUD_SMS_USER));
		nvps.add(new BasicNameValuePair("templateId", SENDCLOUD_TEMPLATE_ID));
		nvps.add(new BasicNameValuePair("phone", phone));
		nvps.add(new BasicNameValuePair("vars", "{" + SENDCLOUD_RANDOMCODE_NAME
				+ ":" + randomCode + "}"));
		nvps.add(new BasicNameValuePair("signature", signature));

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			// 请求
			HttpResponse response = httpClient.execute(httpost);
			// 处理响应
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) { // 正常返回
				// 读取xml文档
				String result = EntityUtils.toString(response.getEntity());
				logger.info("sendcloud send sms, result is:" + result);
			} else {
				logger.error("sendcloud send sms, met error, statusCode is "
						+ statusCode);
			}
		} catch (IOException e) {
			logger.error("sendcloud send sms ,met error, error is "
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			httpost.releaseConnection();
		}
	}

	/**
	 * 生成签名
	 * 
	 * @param phone
	 * @param randomCode
	 * @return
	 */
	private static String getSignature(String phone, String randomCode) {
		Map<String, String> params = new Hashtable<String, String>();
		params.put("smsUser", SENDCLOUD_SMS_USER);
		params.put("templateId", SENDCLOUD_TEMPLATE_ID);
		params.put("phone", phone);
		params.put("vars", "{" + SENDCLOUD_RANDOMCODE_NAME + ":" + randomCode
				+ "}");
		List<String> keyList = new ArrayList<String>(params.keySet());
		Collections.sort(keyList);
		StringBuffer sb = new StringBuffer();
		for (String key : keyList) {
			sb.append(key);
			sb.append("=");
			sb.append(params.get(key));
			sb.append("&");
		}
		String paramString = SENDCLOUD_SMS_KEY + "&" + sb.toString()
				+ SENDCLOUD_SMS_KEY;
		return MD5Encoder.encode(paramString);
	}

}
