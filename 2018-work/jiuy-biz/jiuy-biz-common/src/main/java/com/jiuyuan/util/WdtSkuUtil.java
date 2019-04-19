package com.jiuyuan.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.jiuyuan.constant.AdminConstants;

public class WdtSkuUtil {
	
	private final static String base_url = AdminConstants.WDT_BASE_URL;
	private final static String sid = AdminConstants.WDT_SID;
	private final static String appsecret = AdminConstants.WDT_APPSECRET;
	private final static String appkey = AdminConstants.WDT_APPKEY;
	
	//构造httpClinet，供多线程环境使用
	private static HttpClient httpClient = null;
	static {
		HttpClientBuilder builder = HttpClientBuilder.create();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(50);
		cm.setDefaultMaxPerRoute(20);
		builder.setConnectionManager(cm);
		httpClient = builder.build();
	}
	
	public static String send(String url, Map<String, String> map)  {//
		HttpPost httpost = new HttpPost(base_url + url);
		
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		// 不同于登录SendCloud站点的帐号，您需要登录后台创建发信子帐号，使用子帐号和密码才可以进行邮件的发送。
		
		map.put("sid", sid);
		map.put("appkey", appkey);
		map.put("timestamp", System.currentTimeMillis()/1000 + "");
		
		Map<String, String> map2 = sortMapByKey(map);
		wrapMap(nvps, map2);
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			// 请求
			HttpResponse response = httpClient.execute(httpost);
			// 处理响应
			int statusCode = response.getStatusLine().getStatusCode();
			// 正常返回
			if (statusCode == HttpStatus.SC_OK) { 
				String result = EntityUtils.toString(response.getEntity());
				return result;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			httpost.releaseConnection();
		}
		return "";
	}
	
	public static Map<String, String> sortMapByKey(Map<String, String> oriMap) {  
	    if (oriMap == null || oriMap.isEmpty()) {  
	        return null;  
	    }  
	    Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {  
	        public int compare(String key1, String key2) {  
	        	return key1.compareTo(key2);
	        }});  
	    sortedMap.putAll(oriMap);  
	    return sortedMap;  
	}  
	
	public static void wrapMap(List<BasicNameValuePair> nvps, Map<String, String> map2) {
		Set<String> keys = map2.keySet();
		Iterator<String> it = keys.iterator();
		StringBuilder builder = new StringBuilder();
		while (it.hasNext()) {
			String key = it.next();
			int keyLen = key.length();
			builder.append(String.format("%2d", keyLen).replace(" ", "0") + "-" + key + ":");
			
			String value = map2.get(key);
			int valueLen = value.length();
			builder.append(String.format("%4d", valueLen).replace(" ", "0") + "-" + value + ";");
			
			nvps.add(new BasicNameValuePair(key, value));
		}
		builder = builder.deleteCharAt(builder.lastIndexOf(";"));
		builder.append(appsecret);
//		System.out.println(builder);
		String sign = DigestUtils.md5Hex(builder.toString());

		nvps.add(new BasicNameValuePair("sign", sign));
	}
	
}
