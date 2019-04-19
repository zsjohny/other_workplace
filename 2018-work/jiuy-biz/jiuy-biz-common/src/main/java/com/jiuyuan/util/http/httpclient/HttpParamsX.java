package com.jiuyuan.util.http.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

@SuppressWarnings("deprecation")
public class HttpParamsX extends BasicHttpParams {

    private static final long serialVersionUID = -5214800234206842467L;

    public int getRetryCount() {
        return ParamConverter.getIntValue(getParameter(ClientPNamesX.RETRY_COUNT), 0);
    }

    public void setRetryCount(int retryCount) {
        setParameter(ClientPNamesX.RETRY_COUNT, retryCount);
    }

    public Boolean isRequestSentRetryEnabled() {
        return ParamConverter.getBooleanValue(getParameter(ClientPNamesX.REQUEST_SNET_RETRY_ENABLED), null);
    }

    public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
        setParameter(ClientPNamesX.REQUEST_SNET_RETRY_ENABLED, requestSentRetryEnabled);
    }

    public Boolean isTimeoutRetryEnabled() {
        return ParamConverter.getBooleanValue(getParameter(ClientPNamesX.TIMEOUT_RETRY_ENABLED), null);
    }

    public void setTimeoutRetryEnabled(boolean timeoutRetryEnabled) {
        setParameter(ClientPNamesX.TIMEOUT_RETRY_ENABLED, timeoutRetryEnabled);
    }

    public Long getConnManagerTimeout() {
        return ParamConverter.getLongValue(getParameter(ClientPNames.CONN_MANAGER_TIMEOUT), null);
    }

    public void setConnManagerTimeout(long timeout) {
        setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, timeout);
    }

    public Integer getConnectionTimeout() {
        return ParamConverter.getIntValue(getParameter(CoreConnectionPNames.CONNECTION_TIMEOUT), null);
    }

    public void setConnectionTimeout(int timeout) {
        setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    public int getSoTimeout() {
        return ParamConverter.getIntValue(getParameter(CoreConnectionPNames.SO_TIMEOUT), 0);
    }

    public void setSoTimeout(int timeout) {
        setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
    }

    public String getCookiePolicy() {
        return (String) getParameter(ClientPNames.COOKIE_POLICY);
    }

    public void setCookiePolicy(String cookiePolicy) {
        setParameter(ClientPNames.COOKIE_POLICY, cookiePolicy);
    }

    public HttpHost getDefaultProxy() {
        return (HttpHost) getParameter(ConnRoutePNames.DEFAULT_PROXY);
    }

    public void setDefaultProxy(HttpHost proxy) {
        setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    public String getUserAgent() {
        return (String) getParameter(CoreProtocolPNames.USER_AGENT);
    }

    public void setUserAgent(String userAgent) {
        setParameter(CoreProtocolPNames.USER_AGENT, userAgent);
    }
}
