/**
 * 
 */
package com.jiuyuan.service.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * @author DongZhong
 * @version 创建时间: 2017年10月11日 下午4:29:39
 */
public class RestClientService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			URI uri = new URIBuilder().setScheme("http").setHost("storelocal.yujiejie.com")
					.setPath("/mobile/config/client15.json").build();
			HttpRequestBase httpRequest = new HttpGet(uri);
			// HttpRequestBase httpRequest = new HttpHead(uri);
			// HttpRequestBase httpRequest = new HttpPost(uri);
			// HttpRequestBase httpRequest = new HttpPut(uri);
			// HttpRequestBase httpRequest = new HttpDelete(uri);
			// HttpRequestBase httpRequest = new HttpTrace(uri);
			// HttpRequestBase httpRequest = new HttpOptions(uri);
			System.out.println(httpRequest.getURI());

			ResponseHandler<JsonObject> rh = new ResponseHandler<JsonObject>() {

				@Override
				public JsonObject handleResponse(final HttpResponse response) throws IOException {
					StatusLine statusLine = response.getStatusLine();
					HttpEntity entity = response.getEntity();
					if (statusLine.getStatusCode() >= 300) {
						throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
					}
					if (entity == null) {
						throw new ClientProtocolException("Response contains no content");
					}
					Gson gson = new GsonBuilder().create();
					ContentType contentType = ContentType.getOrDefault(entity);
					Charset charset = contentType.getCharset();
					Reader reader = new InputStreamReader(entity.getContent(), charset);
					return gson.fromJson(reader, JsonObject.class);
				}
			};
			JsonObject myjson = httpclient.execute(httpRequest, rh);

			System.out.println(myjson.toString());

			response = httpclient.execute(httpRequest);
			System.out.println(response.getStatusLine().toString());

			HeaderIterator it0 = response.headerIterator("Set-Cookie");
			while (it0.hasNext()) {
				System.out.println(it0.next());
			}

			HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Set-Cookie"));
			while (it.hasNext()) {
				HeaderElement elem = it.nextElement();
				System.out.println(elem.getName() + " ==== " + elem.getValue());
				NameValuePair[] params = elem.getParameters();
				for (int i = 0; i < params.length; i++) {
					System.out.println(" " + params[i]);
				}
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long len = entity.getContentLength();
				if (len != -1 && len < 2048) {
					System.out.println(EntityUtils.toString(entity));
				} else {
					// Stream content out
					InputStream instream = entity.getContent();
					int byteOne = instream.read();
					int byteTwo = instream.read();
					System.out.println("byteOne:" + byteOne + " byteTwo:" + byteTwo);
					// Do not need the rest
				}
			}
			// System.out.println(EntityUtils.toString(entity));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
