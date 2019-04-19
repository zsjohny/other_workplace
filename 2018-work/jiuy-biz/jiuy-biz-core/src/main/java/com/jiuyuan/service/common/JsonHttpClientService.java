package com.jiuyuan.service.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.util.http.MimeTypes;
import com.jiuyuan.util.http.component.JsonHttpClientOperation;
import com.jiuyuan.util.http.component.JsonHttpClientQuery;
import com.jiuyuan.util.http.component.JsonHttpResponse;
import com.jiuyuan.util.http.httpclient.HttpParamsX;
import com.jiuyuan.util.http.log.LogBuilder;

@Service
public class JsonHttpClientService {

	private static final Logger log = LoggerFactory.getLogger(JsonHttpClientService.class);

	private static final String MIME_TYPE = MimeTypes.APPLICATION_JSON;

	private static final String DEFAULT_CHARSET = "UTF-8";

	@Autowired
	private HttpClientService httpClientService;

	public JsonHttpResponse post(String url, Map<String, Object> map) throws IOException {
		return post(url, map, DEFAULT_CHARSET, null);
	}

	public JsonHttpResponse post(String url, Map<String, Object> map, String charset) throws IOException {
		return post(url, map, charset, null);
	}

	public JsonHttpResponse post(String url, Map<String, Object> map, String charset, HttpParamsX httpParams)
			throws IOException {
		String requestBody = JSON.toJSONString(map);
		return post(url, requestBody, MIME_TYPE, (List<Header>) null, httpParams);
	}

	public JsonHttpResponse post(String url, String text, String charset, List<Header> headers, HttpParamsX httpParams)
			throws IOException {
		return new JsonHttpResponse(httpClientService.post(url, text, MIME_TYPE, charset, headers, httpParams));
	}

	public JsonHttpResponse post(String url, List<NameValuePair> parameters) throws IOException {
		return post(url, parameters, null);
	}

	public JsonHttpResponse post(String url, List<NameValuePair> parameters, List<Header> headers) throws IOException {
		return post(url, parameters, headers, null);
	}

	public JsonHttpResponse post(String url, List<NameValuePair> parameters, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		return post(url, parameters, DEFAULT_CHARSET, headers, httpParams);
	}

	public JsonHttpResponse post(String url, List<NameValuePair> parameters, String charset, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		return new JsonHttpResponse(httpClientService.post(url, parameters, charset, headers, httpParams));
	}

	public JsonHttpResponse post(String url, HttpEntity entity) throws IOException {
		return post(url, entity, null);
	}

	public JsonHttpResponse post(String url, HttpEntity entity, List<Header> headers) throws IOException {
		return post(url, entity, headers, null);
	}

	public JsonHttpResponse post(String url, HttpEntity entity, List<Header> headers, HttpParamsX httpParams)
			throws IOException {
		return post(url, DEFAULT_CHARSET, entity, headers, httpParams);
	}

	public JsonHttpResponse post(String url, String charset, HttpEntity entity, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		return new JsonHttpResponse(httpClientService.post(url, charset, entity, headers, httpParams));
	}

	public JsonHttpResponse get(String url) throws IOException {
		return get(url, (List<Header>) null);
	}

	public JsonHttpResponse get(String url, List<Header> headers) throws IOException {
		return get(url, headers, null);
	}

	public JsonHttpResponse get(String url, HttpParamsX httpParams) throws IOException {
		return get(url, null, httpParams);
	}

	public JsonHttpResponse get(String url, List<Header> headers, HttpParamsX httpParams) throws IOException {
		return get(url, DEFAULT_CHARSET, headers, httpParams);
	}

	public JsonHttpResponse get(String url, String charset, List<Header> headers, HttpParamsX httpParams)
			throws IOException {
		return new JsonHttpResponse(httpClientService.get(url, charset, headers, httpParams));
	}

	public boolean execute(JsonHttpClientOperation operation) {
		LogBuilder errorLog = new LogBuilder(operation.getOperationName(), false).init(operation);
		try {
			JsonHttpResponse response = operation.sendRequest();
			errorLog.append("status", response.getStatusCode()).append("response", response.getResponseText());

			if (response.isStatusCodeOK()) {
				if (operation instanceof JsonHttpClientQuery) {
					JsonHttpClientQuery query = (JsonHttpClientQuery) operation;
					boolean success = query.readResponse(response.parseJson(), errorLog);
					if (!success) {
						log.error(errorLog.toString());
					}
					return success;
				}
				return true;
			}
			log.error(errorLog.toString());
		} catch (Exception e) {
			log.error(errorLog.toString(), e);
		}
		return false;
	}
}
