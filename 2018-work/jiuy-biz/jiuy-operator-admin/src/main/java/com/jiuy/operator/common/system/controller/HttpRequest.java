package com.jiuy.operator.common.system.controller;

import com.jiuyuan.util.BizUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

	/** 
	* @Title: sendPost 
	* @Description: 
	*              解释
	* @param  @param url
	* @param  @param param
	* @param  @return    
	* @author Aison
	* @return String    返回类型 
	* @throws 
	*/
	public static String sendPost(String url, Map<String,Object> paramMap) {
		
		StringBuffer sb  = new StringBuffer("");
		if(paramMap!=null){
			for(Map.Entry<String, Object> en: paramMap.entrySet()) {
				sb.append(en.getKey()).append("=").append(en.getValue()).append("&");
			}
			if(sb.length()>0){
				sb.delete(sb.length()-1, sb.length());
			}
		}
		return sendPost(url, sb.toString());
	}
	
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param, String code) {
		String result = "";
		BufferedReader in = null;
		try {

			String urlNameString = url + "?" + param;

			System.out.println(urlNameString + "                 ============================");
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), code));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}


	/**
	 * 获取请求参数
	 * @param request
	 * @date:   2018/4/26 10:31
	 * @author: Aison
	 */
	public static Map<String, Object> getRequestMap(HttpServletRequest request) {

		Map<String, String[]> map = request.getParameterMap();
		Map<String, Object> maps = new HashMap<String, Object>();
		String[] vals = null;
		for (Map.Entry<String, String[]> en : map.entrySet()) {

			vals = en.getValue();
			if (vals != null && vals.length > 0) {
				// 单个属性
				if (vals.length == 1 && !StringUtils.isBlank(vals[0])) {
					maps.put(en.getKey(), vals[0]);
				}
				// 数组属性
				if (vals.length > 1) {
					maps.put(en.getKey().replace("[]", ""), en.getValue());
				}
			}
		}
		return maps;
	}
}
