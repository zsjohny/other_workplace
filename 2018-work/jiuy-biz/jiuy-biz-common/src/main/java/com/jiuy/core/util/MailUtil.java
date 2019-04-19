package com.jiuy.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeUtility;

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

public class MailUtil {
	
	private static final Logger logger = Logger.getLogger(MailUtil.class);
	private static final String SENDCLOUD_URL = "http://sendcloud.sohu.com/webapi/mail.send.json";
	private static final String SENDCLOUD_APIUSER = "helper";
	private static final String SENDCLOUD_APIKEY = "5gdaMNY7Owlfo7OS";
	private static final String SENDCLOUD_MAIL_ALIASNAME= "俞姐姐网站";
	private static final String SENDCLOUD_MAIL_FROM = "helper@service.yujiejie.com";
	
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
	
	/**
	 * 发送邮件方法，使用sendcloud服务
	 * @param subject 邮件标题
	 * @param htmlContent 邮件HTML内容
	 * @param toEmail 邮件接收者
	 * 
	 * */
	public static void send(String subject, String htmlContent, String toEmail)  {
		
		
		HttpPost httpost = new HttpPost(SENDCLOUD_URL);

		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		// 不同于登录SendCloud站点的帐号，您需要登录后台创建发信子帐号，使用子帐号和密码才可以进行邮件的发送。
		nvps.add(new BasicNameValuePair("api_user", SENDCLOUD_APIUSER));
		nvps.add(new BasicNameValuePair("api_key", SENDCLOUD_APIKEY));
		nvps.add(new BasicNameValuePair("mail_from", SENDCLOUD_MAIL_FROM));
		
		String from = null;
		//如何写别名
		try {
			from = MimeUtility.encodeText(SENDCLOUD_MAIL_ALIASNAME, "UTF-8", "B") + "<" + SENDCLOUD_MAIL_FROM + ">";
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		nvps.add(new BasicNameValuePair("from", from));
		nvps.add(new BasicNameValuePair("to", toEmail));
		nvps.add(new BasicNameValuePair("subject", subject));
		nvps.add(new BasicNameValuePair("html",
				htmlContent));
		
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			// 请求
			HttpResponse response = httpClient.execute(httpost);
			// 处理响应
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) { // 正常返回
				// 读取xml文档
				String result = EntityUtils.toString(response.getEntity());
				logger.info("sendcloud send mail, result is:" + result);
			} else {
				logger.error("sendcloud send mail, met error, statusCode is " + statusCode);
			}
		} catch (IOException e) {
			logger.error("sendcloud send mail ,met error, error is " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			httpost.releaseConnection();
		}
	}
}
