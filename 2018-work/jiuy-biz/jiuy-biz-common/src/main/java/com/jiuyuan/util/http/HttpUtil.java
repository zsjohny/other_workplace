package com.jiuyuan.util.http;

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

//import com.jiuy.core.service.common.YunXinSmsService.CheckSumBuilder;
import com.jiuyuan.util.http.MimeTypes;
import com.jiuyuan.util.http.httpclient.ConnectionKeepAliveStrategyX;
import com.jiuyuan.util.http.httpclient.HttpParamsX;
import com.jiuyuan.util.http.httpclient.HttpRequestRetryHandlerX;

@SuppressWarnings("deprecation")
public class HttpUtil {
	
	// 普通模板

	private static final String APP_KEY = "aaf6335640b65963fa91e3b308b0133a";

	private static final String APP_SECRET = "3db2ab33743b";

	// 验证码专用 
	private static final String APP_KEY1 = "f4f6a01a98b58f89eeb2efa456f1280d";

	private static final String APP_SECRET1 = "a066ed4df363";
	
	// 通知模板

	private static final String APP_KEY2 = "30095ee7e6c2462d4114832fc3ff8c32";

	private static final String APP_SECRET2 = "2f66ab5cff7e";

    public static void setNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1970);
        response.setDateHeader("Expires", c.getTimeInMillis());
    }

    /**
     * 设置HttpClient的默认参数。每个请求可以选择覆盖其中一些设置。比如：
     * 
     * <pre>
     * DefaultHttpClient httpClient = HttpUtil.createHttpClient(null, 500, 5, 5, 0, false, false);
     * HttpGet req = new HttpGet(&quot;http://yuedu.163.com&quot;);
     * 
     * try {
     *     HttpParamsX params = new HttpParamsX();
     *     params.setUserAgent(&quot;myUA&quot;);
     * 
     *     // 请求超时设置
     *     params.setConnManagerTimeout(2 * 1000);
     *     params.setConnectionTimeout(2 * 1000);
     *     params.setSoTimeout(5 * 1000);
     * 
     *     // 请求重试设置
     *     params.setRetryCount(3);
     *     params.setRequestSentRetryEnabled(true);
     *     params.setTimeoutRetryEnabled(true);
     * 
     *     // 设置代理
     *     HttpHost proxy = new HttpHost(&quot;localhost&quot;, 18888);
     *     params.setDefaultProxy(proxy);
     * 
     *     // 允许CookieStore保存cookie，并使用local CookieStore。
     *     params.setCookiePolicy(CookiePolicy.BEST_MATCH);
     *     HttpContext localContext = new BasicHttpContext();
     *     localContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
     * 
     *     HttpUtil.copyHttpParams(req, params);
     * 
     *     HttpResponse response = httpClient.execute(req, localContext);
     *     HttpEntity entity = response.getEntity();
     *     if (entity != null) {
     *         // entity.writeTo(System.out);
     *     }
     * } finally {
     *     req.releaseConnection();
     *     httpClient.getConnectionManager().shutdown();
     * }
     * </pre>
     */
    public static DefaultHttpClient createHttpClient(KeyStore trustStore, int maxThreads, int connectionTimeoutSecs,
                                                     int soTimeoutSecs, int retryCount,
                                                     boolean requestSentRetryEnabled, boolean timeoutRetryEnabled) {

        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        connectionManager.setMaxTotal(maxThreads);
        connectionManager.setDefaultMaxPerRoute(maxThreads);

        if (trustStore != null) {
            try {
                // 自签名的证书导入trust store，然后传入。一般建议为所有自签名证书新建一个独立的HttpClient实例。
                // 注意如果要访问一个站点，它的证书不是自签名的，而且证书没有导入trust store，也是访问不了的。这时需要新建一个不使用trust store的HttpClient实例。
                SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
                Scheme httpsScheme = new Scheme("https", 443, socketFactory);
                connectionManager.getSchemeRegistry().register(httpsScheme);
            } catch (GeneralSecurityException e) {
                throw new IllegalStateException("Failed to register https scheme.", e);
            }
        }

        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager);
        httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategyX());
        // 设置请求重试的条件、次数。每个请求也可以独立设置合适的策略。
        httpClient.setHttpRequestRetryHandler(new HttpRequestRetryHandlerX(retryCount, requestSentRetryEnabled,
            timeoutRetryEnabled));

        HttpParams params = httpClient.getParams();
        // 这里指定默认超时时间。每个请求也可以单独设置合适的超时时间。
        params.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, connectionTimeoutSecs * 1000L);
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeoutSecs * 1000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeoutSecs * 1000);
        // 让CookieStore实例不保存cookie。单个请求如果需要使用CookieStore，可以在发起请求时设置其它CookiePolicy。
        params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);

        // gzip支持
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(final HttpRequest request, final HttpContext context) {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }
            }
        });

        // gzip支持
        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process(final HttpResponse response, final HttpContext context) {
                HttpEntity entity = response.getEntity();
                if (entity != null) { // be aware
                    Header contentEncodingHeader = entity.getContentEncoding();
                    if (contentEncodingHeader != null) {
                        HeaderElement[] codecs = contentEncodingHeader.getElements();
                        for (int i = 0; i < codecs.length; i++) {
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(new GzipDecompressingEntity(entity));
                                return;
                            }
                        }
                    }
                }
            }

        });

        return httpClient;
    }

    /**
     * scheme://serverName[:serverPort][/contextPath]。
     */
    public static String getBaseUrl(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName();
        int port = request.getServerPort();
        if (port != 80 && port != 443) {
            baseUrl += ":" + port;
        }
        String contextPath = request.getContextPath();
        if (contextPath != null && !"/".equals(contextPath)) {
            if (contextPath.endsWith("/")) {
                contextPath = contextPath.substring(0, contextPath.length() - 1);
            }
            baseUrl += contextPath;
        }
        return baseUrl;
    }

    /**
     * do not contain contextPath
     */
    public static String getRequestUrl(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !"/".equals(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            uri += "?" + request.getQueryString();
        }
        return uri;
    }

    public static String getFullRequestUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }

    public static HttpPost createHttpPost(String url, String text, String mimeType, String charset,
                                          List<Header> headers, HttpParamsX httpParams) {
        StringEntity entity = new StringEntity(text, ContentType.create(mimeType, charset));
        return createHttpPost(url, entity, headers, httpParams);
    }

    public static HttpPost createHttpPost(String url, List<NameValuePair> parameters, String charset,
                                          List<Header> headers, HttpParamsX httpParams) {
        UrlEncodedFormEntity entity = createUrlEncodedFormEntity(parameters, charset);
        return createHttpPost(url, entity, headers, httpParams);
    }

    public static HttpPost createHttpPost(String url, HttpEntity entity, List<Header> headers, HttpParamsX httpParams) {
        HttpPost httpPost = new HttpPost(url);
        addRequestHeaders(httpPost, headers);
        copyHttpParams(httpPost, httpParams);
        httpPost.setEntity(entity);
        return httpPost;
    }

    public static UrlEncodedFormEntity createUrlEncodedFormEntity(List<NameValuePair> parameters, String charset) {
        try {
            return new UrlEncodedFormEntity(parameters, charset);
        } catch (UnsupportedEncodingException e) {
            // ignore
            throw new RuntimeException("Failed to create entity.", e);
        }
    }

    public static void addRequestHeaders(HttpUriRequest request, List<Header> headers) {
        if (headers != null) {
            for (Header header : headers) {
                request.addHeader(header);
            }
        }
    }

    public static void copyHttpParams(HttpUriRequest request, HttpParamsX params) {
        HttpParams reqParams = request.getParams();
        if (params != null) {
            for (String name : params.getNames()) {
                Object value = params.getParameter(name);
                reqParams.setParameter(name, value);
            }
        }
        /**
         * add by Liuweisheng, Modified the BUG
         */
        request.setParams(reqParams);
    }

    public static StringBody createStringBody(String text, String charset) {
        try {
            return new StringBody(text, Charset.forName(charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip.contains(",")) {
            String[] ips = StringUtils.split(ip, ", ");
            ip = ips[0].trim();
        }

        return ip;
    }

    public static String getRequestProtocol(HttpServletRequest request) {
        String protocol = request.getHeader("X-Forwarded-Proto");
        if (StringUtils.isBlank(protocol)) {
            protocol = request.getProtocol();
        }
        return protocol;
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

    public static boolean isJsonRequest(HttpServletRequest request) {
        return StringUtils.endsWith(request.getRequestURI(), ".json") ||
            StringUtils.contains(request.getContentType(), MimeTypes.APPLICATION_JSON);
    }

    public static void sendResponse(HttpServletResponse response, String mimeType, String charset, String responseBody)
        throws IOException {
        setNoCache(response);
        PrintWriter writer = null;
        try {
            ContentType contentType = ContentType.create(mimeType, charset);
            response.setContentType(contentType.toString());
            writer = response.getWriter();
            writer.print(responseBody);
            writer.flush();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
    
    public static boolean isWeixin(HttpServletRequest request) {
    	String ua = getUserAgent(request);
    	if (ua != null) {
    		return StringUtils.containsIgnoreCase(ua, "micromessenger");
    	}
    	return false;
    }
    
    public static boolean isYixin(HttpServletRequest request) {
    	String ua = getUserAgent(request);
    	if (ua != null) {
    		return StringUtils.containsIgnoreCase(ua, "yixin");
    	}
    	return false;
    }


//	// 云信普通模板使用
//	public static List<Header> constructYuxinAuthHeaders() {
//
//		List<Header> headers = new ArrayList<Header>();
//		headers.add(new BasicHeader("AppKey", APP_KEY));
//		String nonce = RandomStringUtils.randomAlphanumeric(20);
//		headers.add(new BasicHeader("Nonce", nonce));
//		String curTime = String.valueOf(System.currentTimeMillis());
//		headers.add(new BasicHeader("CurTime", curTime));
//		headers.add(new BasicHeader("CheckSum", CheckSumBuilder.getCheckSum(
//				APP_SECRET, nonce, curTime)));
//
//		return headers;
//	}	

//	// 云信验证码专用
//	public static List<Header> constructYuxinAuthHeaders1() {
//
//		List<Header> headers = new ArrayList<Header>();
//		headers.add(new BasicHeader("AppKey", APP_KEY1));
//		String nonce = RandomStringUtils.randomAlphanumeric(20);
//		headers.add(new BasicHeader("Nonce", nonce));
//		String curTime = String.valueOf(System.currentTimeMillis());
//		headers.add(new BasicHeader("CurTime", curTime));
//		headers.add(new BasicHeader("CheckSum", CheckSumBuilder.getCheckSum(
//				APP_SECRET1, nonce, curTime)));
//
//		return headers;
//	}
//	
//	// 云信通知模板使用
//	public static List<Header> constructYuxinAuthHeaders2() {
//
//		List<Header> headers = new ArrayList<Header>();
//		headers.add(new BasicHeader("AppKey", APP_KEY2));
//		String nonce = RandomStringUtils.randomAlphanumeric(20);
//		headers.add(new BasicHeader("Nonce", nonce));
//		String curTime = String.valueOf(System.currentTimeMillis());
//		headers.add(new BasicHeader("CurTime", curTime));
//		headers.add(new BasicHeader("CheckSum", CheckSumBuilder.getCheckSum(		
//				APP_SECRET2, nonce, curTime)));
//		
//		return headers;
//		}	

}