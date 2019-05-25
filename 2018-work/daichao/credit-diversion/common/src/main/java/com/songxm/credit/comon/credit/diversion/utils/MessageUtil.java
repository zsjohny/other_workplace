package com.songxm.credit.comon.credit.diversion.utils;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 * 短信发送工具类
 * 
 * @author bone
 *
 */
public class MessageUtil {

	/**
	 * 获取一个6位数的随机验证码
	 * 
	 * @return
	 */
	public static String randomCode() {

		String code = "";
		Random random = new Random();
		code = code + (random.nextInt(9) + 1);
		for (int i = 0; i < 5; i++) {
			code = code + random.nextInt(10);
		}
		return code;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param phoneNum
	 *            电话号码
	 * @param msg
	 *            短信内容
	 * @return true:发送成功 false:发送失败
	 */
	public static String send(String phoneNum, String msg) {

		String host = "h.1069106.com";
		int port = 1210;
		String protocol = "http";
		String url = "/services/msgsend.asmx/SendMsg";
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, port, protocol);
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		NameValuePair[] param = { new NameValuePair("userCode", "srdz"),
				new NameValuePair("userPass", "srdz487"),
				new NameValuePair("DesNo", phoneNum),
				new NameValuePair("Msg", msg), new NameValuePair("Channel", "") };
		post.setRequestBody(param);
		post.releaseConnection();
		int i = 0;
		String response = null;
		try {
			i = client.executeMethod(post);
			System.out.println(i);
			response = post.getResponseBodyAsString();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (i != 200) {
			return null;
		}
		String result = readerXML(response);
		System.out.println(result);
		return result;
	}

	/**
	 * 解析xml片段
	 * 
	 * @param xml
	 * @return
	 */
	private static String readerXML(String xml) {

		String result = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		try {
			XMLEventReader eventReader = factory.createXMLEventReader(is);
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					String eventName = event.asStartElement().getName()
							.getLocalPart();
					if (eventName.equalsIgnoreCase("string")) {
						XMLEvent textEvent = eventReader.nextEvent();
						if (textEvent.isCharacters()) {
							result = textEvent.asCharacters().getData();
						}
					}
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取配置文件
	 */
	public static String loadProperties(String key) {
		Properties prop = null;
		String result = null;
		try {
			prop = PropertiesLoaderUtils
					.loadAllProperties("message.properties");
			String temp = prop.getProperty(key);
			result = new String(temp.getBytes("iso-8859-1"), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		

		
		
		
		//7.#开始服务提醒*/
        String code = loadProperties("msg_regist");
       String result =  code.replaceAll("XXXXXX","123455");
		System.out.println(code);
        System.out.println(result);
		//System.out.println(send("18720987043", loadProperties("msg_regist")));
		
		
	}
}
