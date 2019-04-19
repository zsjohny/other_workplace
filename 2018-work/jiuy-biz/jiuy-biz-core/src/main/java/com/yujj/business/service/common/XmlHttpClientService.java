package com.yujj.business.service.common;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.util.http.Dom4jElementDecorator;
import com.jiuyuan.util.http.MimeTypes;
import com.jiuyuan.util.http.component.XmlHttpClientOperation;
import com.jiuyuan.util.http.component.XmlHttpClientQuery;
import com.jiuyuan.util.http.component.XmlHttpResponse;
import com.jiuyuan.util.http.httpclient.HttpParamsX;
import com.jiuyuan.util.http.log.LogBuilder;

@Service
public class XmlHttpClientService {

	private static final Logger log = LoggerFactory.getLogger(XmlHttpClientService.class);

	private static final String MIME_TYPE = MimeTypes.APPLICATION_XML;

	@Autowired
	private HttpClientService httpClientService;

	public XmlHttpResponse post(String url, String text, String charset, List<Header> headers, HttpParamsX httpParams)
			throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, text, MIME_TYPE, charset, headers, httpParams));
	}

	public XmlHttpResponse post(String url, List<NameValuePair> parameters) throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, parameters));
	}

	public XmlHttpResponse post(String url, List<NameValuePair> parameters, List<Header> headers) throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, parameters, headers));
	}

	public XmlHttpResponse post(String url, List<NameValuePair> parameters, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, parameters, headers, httpParams));
	}

	public XmlHttpResponse post(String url, List<NameValuePair> parameters, String charset, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, parameters, charset, headers, httpParams));
	}

	public XmlHttpResponse post(String url, HttpEntity entity) throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, entity));
	}

	public XmlHttpResponse post(String url, HttpEntity entity, List<Header> headers) throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, entity, headers));
	}

	public XmlHttpResponse post(String url, HttpEntity entity, List<Header> headers, HttpParamsX httpParams)
			throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, entity, headers, httpParams));
	}

	public XmlHttpResponse post(String url, String charset, HttpEntity entity, List<Header> headers,
			HttpParamsX httpParams) throws IOException {
		return new XmlHttpResponse(httpClientService.post(url, charset, entity, headers, httpParams));
	}

	public XmlHttpResponse get(String url) throws IOException {
		return new XmlHttpResponse(httpClientService.get(url));
	}

	public XmlHttpResponse get(String url, List<Header> headers) throws IOException {
		return new XmlHttpResponse(httpClientService.get(url, headers));
	}

	public XmlHttpResponse get(String url, HttpParamsX httpParams) throws IOException {
		return new XmlHttpResponse(httpClientService.get(url, httpParams));
	}

	public XmlHttpResponse get(String url, List<Header> headers, HttpParamsX httpParams) throws IOException {
		return new XmlHttpResponse(httpClientService.get(url, headers, httpParams));
	}

	public XmlHttpResponse get(String url, String charset, List<Header> headers, HttpParamsX httpParams)
			throws IOException {
		return new XmlHttpResponse(httpClientService.get(url, charset, headers, httpParams));
	}

	public boolean execute(XmlHttpClientOperation operation) {
		XmlHttpResponse response = null;
		LogBuilder errorLog = new LogBuilder(operation.getOperationName(), false).init(operation);
		try {
			response = operation.sendRequest();
			errorLog.append("status", response.getStatusCode()).append("response", response.getResponseText());

			if (response.isStatusCodeOK()) {
				if (operation instanceof XmlHttpClientQuery) {
					XmlHttpClientQuery query = (XmlHttpClientQuery) operation;
					Element root = response.getResponseXml().getRootElement();
					boolean success = query.readResponse(new Dom4jElementDecorator(root), errorLog);
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
}
