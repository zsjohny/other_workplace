package com.jiuyuan.service.common;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.jiuy.util.http.HttpUtil;
import com.jiuyuan.util.http.EntityUtil;
import com.jiuyuan.util.http.HttpUtil;
import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.jiuyuan.util.http.component.HttpClientOperation;
import com.jiuyuan.util.http.component.HttpClientQuery;
import com.jiuyuan.util.http.httpclient.HttpParamsX;
import com.jiuyuan.util.http.log.LogBuilder;

public class HttpClientService {

	private static final Logger log = LoggerFactory.getLogger(HttpClientService.class);

	private static final String DEFAULT_CHARSET = "UTF-8";

	private DefaultHttpClient httpClient;

	public CachedHttpResponse post(String url, String text, String mimeType, String charset, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		StringEntity entity = new StringEntity(text, ContentType.create(mimeType, charset));
		return post(url, charset, entity, headers, httpParams);
	}

	public CachedHttpResponse post(String url, List<NameValuePair> parameters) throws IOException {
		return post(url, parameters, null);
	}

	public CachedHttpResponse post(String url, List<NameValuePair> parameters, List<Header> headers)
			throws IOException {
		return post(url, parameters, headers, null);
	}

	public CachedHttpResponse post(String url, List<NameValuePair> parameters, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		return post(url, parameters, DEFAULT_CHARSET, headers, httpParams);
	}

	public CachedHttpResponse post(String url, List<NameValuePair> parameters, String charset, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		UrlEncodedFormEntity entity = HttpUtil.createUrlEncodedFormEntity(parameters, charset);
		return post(url, charset, entity, headers, httpParams);
	}

	public CachedHttpResponse post(String url, HttpEntity entity) throws IOException {
		return post(url, entity, null);
	}

	public CachedHttpResponse post(String url, HttpEntity entity, List<Header> headers) throws IOException {
		return post(url, entity, headers, null);
	}

	public CachedHttpResponse post(String url, HttpEntity entity, List<Header> headers, HttpParamsX httpParams)
			throws IOException {
		return post(url, DEFAULT_CHARSET, entity, headers, httpParams);
	}

	public CachedHttpResponse post(String url, String charset, HttpEntity entity, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		HttpPost req = HttpUtil.createHttpPost(url, entity, headers, httpParams);
		try {
			HttpResponse resp = httpClient.execute(req);
			return new CachedHttpResponse(url, resp.getStatusLine().getStatusCode(),
					EntityUtil.toByteArray(resp.getEntity()), charset, resp.getAllHeaders());
		} finally {
			req.releaseConnection();
		}
	}

	public CachedHttpResponse get(String url) throws IOException {
		return get(url, null, null);
	}

	public CachedHttpResponse get(String url, List<Header> headers) throws IOException {
		return get(url, headers, null);
	}

	public CachedHttpResponse get(String url, HttpParamsX httpParams) throws IOException {
		return get(url, null, httpParams);
	}

	public CachedHttpResponse get(String url, List<Header> headers, HttpParamsX httpParams) throws IOException {
		return get(url, DEFAULT_CHARSET, headers, httpParams);
	}

	public CachedHttpResponse get(String url, String charset, List<Header> headers, HttpParamsX httpParams)
			throws IOException {
		HttpGet req = new HttpGet(url);
		HttpUtil.addRequestHeaders(req, headers);
		HttpUtil.copyHttpParams(req, httpParams);
		try {
			HttpResponse resp = httpClient.execute(req);
			return new CachedHttpResponse(url, resp.getStatusLine().getStatusCode(),
					EntityUtil.toByteArray(resp.getEntity()), charset, resp.getAllHeaders());
		} finally {
			req.releaseConnection();
		}
	}

	public boolean execute(HttpClientOperation operation) {
		CachedHttpResponse response = null;
		LogBuilder errorLog = new LogBuilder(operation.getOperationName(), false).init(operation);
		try {
			response = operation.sendRequest();
			errorLog.append("status", response.getStatusCode()).append("response", response.getResponseText());

			if (response.isStatusCodeOK()) {
				if (operation instanceof HttpClientQuery) {
					HttpClientQuery query = (HttpClientQuery) operation;
					boolean success = query.readResponse(response.getResponseText(), errorLog);
					if (!success) {
						log.error(errorLog.toString());
					}
					return success;
				}
				return true;
			} else {
				log.error(errorLog.toString());
			}
		} catch (Exception e) {
			log.error(errorLog.toString(), e);
		}
		return false;
	}

	public DefaultHttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(DefaultHttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
