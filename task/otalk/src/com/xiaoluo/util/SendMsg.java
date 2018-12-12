package com.xiaoluo.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.InputStream;

/**
 * Created by p on 2016/2/18.
 */
public class SendMsg {

	public static int batchSend(String mobile, String num) {
		String url = "http://222.73.117.156/msg/";// 应用地址
		String account = "vip-szwl";// 账号
		String pswd = "Tch123456";// 密码

		String msg = "您好，您的验证码：" + num + ",请于5分钟内正确输入验证码。";// 短信内容
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
		String product = null;// 产品ID
		String extno = null;// 扩展码
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "HttpBatchSendSM", false));
			method.setQueryString(new NameValuePair[] { new NameValuePair("account", account),
					new NameValuePair("pswd", pswd), new NameValuePair("mobile", mobile),
					new NameValuePair("needstatus", String.valueOf(needstatus)), new NameValuePair("msg", msg),
					new NameValuePair("product", product), new NameValuePair("extno", extno), });
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in;
				in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return result;
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} catch (Exception e) {

		} finally {
			method.releaseConnection();
		}
		return 300;
	}

	public static void main(String[] args) {

		String mobile = "15924179757";// 手机号码，多个号码使用","分割

	}
}
