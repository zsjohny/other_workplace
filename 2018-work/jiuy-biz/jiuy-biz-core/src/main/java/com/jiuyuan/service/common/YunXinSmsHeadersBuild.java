package com.jiuyuan.service.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.jiuyuan.service.common.YunXinSmsService.CheckSumBuilder;
import com.jiuyuan.util.http.MimeTypes;
import com.jiuyuan.util.http.httpclient.ConnectionKeepAliveStrategyX;
import com.jiuyuan.util.http.httpclient.HttpParamsX;
import com.jiuyuan.util.http.httpclient.HttpRequestRetryHandlerX;
import com.jiuyuan.service.common.JsonHttpClientService;

@SuppressWarnings("deprecation")
public class YunXinSmsHeadersBuild {
//	
//	// 普通模板
//
	private static final String APP_KEY = "aaf6335640b65963fa91e3b308b0133a";
	private static final String APP_SECRET = "3db2ab33743b";
//
//	// 验证码专用 
	private static final String APP_KEY1 = "f4f6a01a98b58f89eeb2efa456f1280d";
	private static final String APP_SECRET1 = "a066ed4df363";
//	
	// 通知模板
	private static final String APP_KEY2 = "30095ee7e6c2462d4114832fc3ff8c32";
	private static final String APP_SECRET2 = "2f66ab5cff7e";

//
//
	// 云信普通模板使用
	public static List<Header> constructYuxinAuthHeaders() {

		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("AppKey", APP_KEY));
		String nonce = RandomStringUtils.randomAlphanumeric(20);
		headers.add(new BasicHeader("Nonce", nonce));
		String curTime = String.valueOf(System.currentTimeMillis());
		headers.add(new BasicHeader("CurTime", curTime));
		headers.add(new BasicHeader("CheckSum", CheckSumBuilder.getCheckSum(
				APP_SECRET, nonce, curTime)));

		return headers;
	}	
//
//	// 云信验证码专用
	public static List<Header> constructYuxinAuthHeaders1() {

		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("AppKey", APP_KEY1));
		String nonce = RandomStringUtils.randomAlphanumeric(20);
		headers.add(new BasicHeader("Nonce", nonce));
		String curTime = String.valueOf(System.currentTimeMillis());
		headers.add(new BasicHeader("CurTime", curTime));
		headers.add(new BasicHeader("CheckSum", CheckSumBuilder.getCheckSum(
				APP_SECRET1, nonce, curTime)));

		return headers;
	}
//	
	// 云信通知模板使用
	public static List<Header> constructYuxinAuthHeaders2() {

		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("AppKey", APP_KEY2));
		String nonce = RandomStringUtils.randomAlphanumeric(20);
		headers.add(new BasicHeader("Nonce", nonce));
		String curTime = String.valueOf(System.currentTimeMillis());
		headers.add(new BasicHeader("CurTime", curTime));
		headers.add(new BasicHeader("CheckSum", CheckSumBuilder.getCheckSum(		
				APP_SECRET2, nonce, curTime)));
		
		return headers;
		}	

}