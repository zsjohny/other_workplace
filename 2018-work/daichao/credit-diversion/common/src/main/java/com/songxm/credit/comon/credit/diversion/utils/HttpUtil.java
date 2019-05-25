//package com.songxm.credit.comon.credit.diversion.utils;
//
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.methods.PostMethod;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class HttpUtil {
//
//	public static String post(String host,int port,String url,Map<String,String> map,Map<String,String> heads){
//		//String host = "127.0.0.1";
//		//int port = 8080;
//		String protocol = "http";
//		//String url = "/flower/mobile/consumer/validateCode";
//		HttpClient client = new HttpClient();
//		client.getHostConfiguration().setHost(host, port, protocol);
//		PostMethod post = new PostMethod(url);
//		post.setRequestHeader("Content-Type",
//				"application/x-www-form-urlencoded;charset=UTF-8");
//
//		if(heads==null){
//			heads = new HashMap<String, String>();
//		}
//		for(Map.Entry<String, String> entry : heads.entrySet()){
//			post.setRequestHeader(entry.getKey(), entry.getValue());
//		}
//
//
//
//		/*NameValuePair[] param = {
//				new NameValuePair("phoneNum", "18512345670"),
//				new NameValuePair("type", "0")
//				};*/
//		if(map==null){
//			map = new HashMap<String,String>();
//		}
//		NameValuePair[] param = new NameValuePair[map.size()];
//		int index = 0;
//		for(Map.Entry<String,String> entry : map.entrySet()){
//			param[index] = new NameValuePair(entry.getKey(),entry.getValue());
//			index++;
//		}
//
//		post.setRequestBody(param);
//		System.out.println("**" + post.getRequestEntity());
//		post.releaseConnection();
//		int i = 0;
//		String response = null;
//		try {
//			i = client.executeMethod(post);
//			System.out.println("client.executeMethod:" +i);
//			response = post.getResponseBodyAsString();
//			System.out.println(post.toString());
//			System.out.println("HttpClirnt response: " + response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//
//	public static String post2(String host,int port,String url,Map<String,String> map,Map<String,String> heads){
//		//String host = "127.0.0.1";
//		//int port = 8080;
//		String protocol = "http";
//		//String url = "/flower/mobile/consumer/validateCode";
//		HttpClient client = new HttpClient();
//		client.getHostConfiguration().setHost(host, port, protocol);
//		PostMethod post = new PostMethod(url);
//		post.setRequestHeader("Content-Type",
//				"application/x-www-form-urlencoded;charset=UTF-8");
//		post.addRequestHeader("Content-Type", "multipart/form-data");
//		if(heads==null){
//			heads = new HashMap<String, String>();
//		}
//		for(Map.Entry<String, String> entry : heads.entrySet()){
//			post.setRequestHeader(entry.getKey(), entry.getValue());
//		}
//
//
//
//		/*NameValuePair[] param = {
//				new NameValuePair("phoneNum", "18512345670"),
//				new NameValuePair("type", "0")
//				};*/
//		if(map==null){
//			map = new HashMap<String,String>();
//		}
//		NameValuePair[] param = new NameValuePair[map.size()];
//		int index = 0;
//		for(Map.Entry<String,String> entry : map.entrySet()){
//			param[index] = new NameValuePair(entry.getKey(),entry.getValue());
//			index++;
//		}
//
//		post.setRequestBody(param);
//		post.releaseConnection();
//		int i = 0;
//		String response = null;
//		try {
//			i = client.executeMethod(post);
//			System.out.println("client.executeMethod:" +i);
//			response = post.getResponseBodyAsString();
//			System.out.println("HttpClirnt response: " + response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	/*public static String post(String host,int port,String url,Map<String,String> map,Map<String,String> heads){
//		//String host = "127.0.0.1";
//		//int port = 8080;
//		String protocol = "http";
//		//String url = "/flower/mobile/consumer/validateCode";
//		HttpClient client = new HttpClient();
//		client.getHostConfiguration().setHost(host, port, protocol);
//		PostMethod post = new PostMethod(url);
//		post.setRequestHeader("Content-Type",
//				"application/x-www-form-urlencoded;charset=UTF-8");
//
//		if(heads==null){
//			heads = new HashMap<String, String>();
//		}
//		for(Map.Entry<String, String> entry : heads.entrySet()){
//			post.setRequestHeader(entry.getKey(), entry.getValue());
//		}
//
//
//
//		NameValuePair[] param = {
//				new NameValuePair("phoneNum", "18512345670"),
//				new NameValuePair("type", "0")
//				};
//		if(map==null){
//			map = new HashMap<String,String>();
//		}
//		NameValuePair[] param = new NameValuePair[map.size()];
//		int index = 0;
//		for(Map.Entry<String,String> entry : map.entrySet()){
//			param[index] = new NameValuePair(entry.getKey(),entry.getValue());
//			index++;
//		}
//
//		post.setRequestBody(param);
//		post.releaseConnection();
//		int i = 0;
//		String response = null;
//		try {
//			i = client.executeMethod(post);
//			System.out.println("client.executeMethod:" +i);
//			response = post.getResponseBodyAsString();
//			System.out.println("HttpClirnt response: " + response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}*/
//
//	public static String post3(String host,int port,String url,Map<String,Object> map,Map<String,String> heads){
//		//String host = "127.0.0.1";
//		//int port = 8080;
//		String protocol = "http";
//		//String url = "/flower/mobile/consumer/validateCode";
//		HttpClient client = new HttpClient();
//		client.getHostConfiguration().setHost(host, port, protocol);
//		PostMethod post = new PostMethod(url);
//		post.setRequestHeader("Content-Type",
//				"application/x-www-form-urlencoded;charset=UTF-8");
//
//		if(heads==null){
//			heads = new HashMap<String, String>();
//		}
//		for(Map.Entry<String, String> entry : heads.entrySet()){
//			post.setRequestHeader(entry.getKey(), entry.getValue());
//		}
//
//
//
//		/*NameValuePair[] param = {
//				new NameValuePair("phoneNum", "18512345670"),
//				new NameValuePair("type", "0")
//				};*/
//		if(map==null){
//			map = new HashMap<String,Object>();
//		}
//		NameValuePair[] param = new NameValuePair[map.size()];
//		int index = 0;
//		for(Map.Entry<String,Object> entry : map.entrySet()){
//			System.out.println("entry.getKey()###"+entry.getKey()+"entry.getValue()*****" + entry.getValue());
//			param[index] = new NameValuePair(entry.getKey(),entry.getValue()+"");
//			index++;
//		}
//
//		post.setRequestBody(param);
//		System.out.println("**" + post.getRequestEntity());
//		post.releaseConnection();
//		int i = 0;
//		String response = null;
//		try {
//			i = client.executeMethod(post);
//			System.out.println("client.executeMethod:" +i);
//			response = post.getResponseBodyAsString();
//			System.out.println(post.toString());
//			System.out.println("HttpClirnt response: " + response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//}
