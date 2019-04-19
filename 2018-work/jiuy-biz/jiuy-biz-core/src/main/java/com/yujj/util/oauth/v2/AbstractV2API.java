package com.yujj.util.oauth.v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicHeader;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.util.http.HttpUtil;
//import com.jiuy.util.http.HttpUtil;
import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.jiuyuan.util.oauth.common.Display;
import com.jiuyuan.util.oauth.common.credential.IClientCredentials;
import com.jiuyuan.util.oauth.common.credential.ICredentials;
import com.jiuyuan.util.oauth.v2.V2Config;
import com.jiuyuan.util.oauth.v2.V2Constants;
import com.jiuyuan.util.oauth.v2.credential.IRawDataV2TokenCredentials;
import com.jiuyuan.util.oauth.v2.credential.RawDataV2TokenCredentials;
import com.yujj.util.JsonUtil;
import com.yujj.util.uri.UriBuilder;
import com.yujj.util.uri.UriParams;
import com.yujj.util.uri.UriUtil;

/**
 * OAuth 2.0 API基础实现
 */
public abstract class AbstractV2API implements IV2API, V2Constants {

    protected HttpClientService httpClientService;

    protected V2Config config;

    protected IClientCredentials clientCredentials;

    public AbstractV2API(HttpClientService httpClientService, V2Config config, IClientCredentials clientCredentials) {
        this.httpClientService = httpClientService;
        this.config = config;
        this.clientCredentials = clientCredentials;
    }

    @Override
    public IClientCredentials getClientCredentials() {
        return clientCredentials;
    }

    @Override
    public IRawDataV2TokenCredentials getTokenCredentials(String code, String refreshToken, UriParams callbackParams) {
        String charset = config.getDefaultCharset();
        String baseUri = config.getAccessTokenUri();

        int statusCode = 0;
        String responseText = null;

        try {
            CachedHttpResponse httpResponse = sendTokenCredentialsRequest(code, refreshToken, callbackParams);

            statusCode = httpResponse.getStatusCode();
            responseText = httpResponse.getResponseText();

            if (httpResponse.isStatusCodeFine()) {
                Map<String, String> data = null;
                if (JsonUtil.isJsonObjectLike(responseText)) {
                    data = new UriParams().setAll(JSONObject.parseObject(responseText)).asSingleValueMap();
                } else {
                    data = UriUtil.parseQueryString(responseText, charset, true).asSingleValueMap();
                }

                IRawDataV2TokenCredentials credentials = new RawDataV2TokenCredentials(data);
                if (StringUtils.isNotBlank(credentials.getIdentifier())) {
                    return credentials;
                }
            }

            throw new RuntimeException("Unable to get access token, baseUri: " + baseUri + ", status code: " +
                statusCode + ", response text: " + responseText);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get access token, baseUri: " + baseUri + ", status code: " +
                statusCode + ", response text: " + responseText, e);
        }
    }

    protected CachedHttpResponse sendTokenCredentialsRequest(String code, String refreshToken, UriParams callbackParams)
        throws IOException {

        String charset = config.getDefaultCharset();

        UriParams params = new UriParams();
        params.set(CLIENT_ID, clientCredentials.getIdentifier());
        params.set(CLIENT_SECRET, clientCredentials.getSecret());
        params.set(REDIRECT_URI, getCallbackUrl(callbackParams));

        if (StringUtils.isNotBlank(code)) {
            params.set(CODE, code);
            params.set(GRANT_TYPE, AUTHORIZATION_CODE);
        } else {
            params.set(REFRESH_TOKEN, refreshToken);
            params.set(GRANT_TYPE, REFRESH_TOKEN);
        }
        
        updateAccessTokenParams(params);
        return httpClientService.post(config.getAccessTokenUri(), params.asNameValuePairs(), charset, null,
            config.getDefaultHttpParams());
    }

    @Override
    public String getCallbackUrl(UriParams callbackParams) {
        UriBuilder builder = new UriBuilder(clientCredentials.getCallbackUri(), config.getDefaultCharset());
        if (callbackParams != null) {
            builder.setAll(callbackParams);
        }
        return builder.toUri();
    }

    @Override
    public UriBuilder getAuthorizeUrl(UriParams callbackParams, String state, Display display) {
        UriBuilder builder =
            new UriBuilder(config.getAuthorizeUri()).set("client_id", clientCredentials.getIdentifier()).set(
                "response_type", "code").set("state", state).set("redirect_uri", getCallbackUrl(callbackParams));

        String scope = config.getAuthorizeScope();
        if (StringUtils.isNotBlank(scope)) {
            builder.set("scope", scope);
        }

        updateAuthorizeUrl(builder, display);
        return builder;
    }

    protected void updateAuthorizeUrl(UriBuilder builder, Display display) {
        // hook
    }

    protected void updateAccessTokenParams(UriParams params) {
        // hook
    }
    
    protected CachedHttpResponse httpGet(String baseUri  ) {
    	 UriParams params = new UriParams();
    	return httpGet(baseUri,params);
    }
    
    protected CachedHttpResponse httpGet(String baseUri, UriParams params ) {
    	return httpGet(baseUri,params,null);
    }

    protected CachedHttpResponse httpGet(String baseUri, UriParams params, ICredentials tokenCredentials) {

        String charset = config.getDefaultCharset();
        List<Header> headers = generateHttpRequestHeaders(tokenCredentials);

        try {
            return httpClientService.get(UriUtil.concatUri(baseUri, params), charset, headers,
                config.getDefaultHttpParams());
        } catch (Exception e) {
            throw new RuntimeException("Unable to call api. baseUri: " + baseUri, e);
        }
    }

    protected CachedHttpResponse httpPost(String baseUri, UriParams params, ICredentials tokenCredentials) {

        String charset = config.getDefaultCharset();
        List<Header> headers = generateHttpRequestHeaders(tokenCredentials);

        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null) {
                nvps = params.asNameValuePairs();
            }
            return httpClientService.post(baseUri, nvps, charset, headers, config.getDefaultHttpParams());
        } catch (Exception e) {
            throw new RuntimeException("Unable to call api. baseUri: " + baseUri, e);
        }
    }

    protected CachedHttpResponse httpPostWithEntity(String baseUri, HttpEntity requestEntity,
                                                    ICredentials tokenCredentials) {

        String charset = config.getDefaultCharset();
        List<Header> headers = generateHttpRequestHeaders(tokenCredentials);

        try {
            return httpClientService.post(baseUri, charset, requestEntity, headers, config.getDefaultHttpParams());
        } catch (Exception e) {
            throw new RuntimeException("Unable to call api. baseUri: " + baseUri, e);
        }
    }

    protected CachedHttpResponse httpMultipartRequest(String baseUri, UriParams params, MultipartEntity requestEntity,
                                                      ICredentials tokenCredentials) {

        String charset = config.getDefaultCharset();
        List<Header> headers = generateHttpRequestHeaders(tokenCredentials);

        if (params != null) {
            for (NameValuePair nvp : params.asNameValuePairs()) {
                requestEntity.addPart(nvp.getName(), HttpUtil.createStringBody(nvp.getValue(), charset));
            }
        }

        try {
            return httpClientService.post(baseUri, charset, requestEntity, headers, config.getDefaultHttpParams());
        } catch (Exception e) {
            throw new RuntimeException("Unable to call api. baseUri: " + baseUri, e);
        }
    }

    protected List<Header> generateHttpRequestHeaders(ICredentials tokenCredentials) {
        List<Header> headers = new ArrayList<Header>();
        if(tokenCredentials != null){
	        String accessToken = tokenCredentials.getIdentifier();
	        if (StringUtils.isNotBlank(accessToken)) {
	            headers.add(new BasicHeader("Authorization", "OAuth2 " + accessToken));
	        }
        }
        return headers;
    }
}
