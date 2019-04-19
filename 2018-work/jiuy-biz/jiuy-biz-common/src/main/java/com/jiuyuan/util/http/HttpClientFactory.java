package com.jiuyuan.util.http;

import java.security.KeyStore;

import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

//import com.jiuy.util.http.HttpUtil;
import com.jiuyuan.util.KeyStoreUtil;
//import com.yujj.util.http.HttpUtil;
import com.jiuyuan.util.http.HttpUtil;

public class HttpClientFactory implements FactoryBean<DefaultHttpClient> {

    private Resource trustStore;

    private String trustStorePassword;

    private int maxThreads;

    private int connectionTimeoutSecs;

    private int soTimeoutSecs;

    private int retryCount;

    private boolean requestSentRetryEnabled;

    private boolean timeoutRetryEnabled;

    @Override
    public DefaultHttpClient getObject() throws Exception {
        KeyStore store = null;
        if (trustStore != null) {
            store = KeyStoreUtil.loadKeyStore(trustStore.getFile(), trustStorePassword);
        }
        return HttpUtil.createHttpClient(store, maxThreads, connectionTimeoutSecs, soTimeoutSecs, retryCount,
            requestSentRetryEnabled, timeoutRetryEnabled);
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultHttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Resource getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(Resource trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getConnectionTimeoutSecs() {
        return connectionTimeoutSecs;
    }

    public void setConnectionTimeoutSecs(int connectionTimeoutSecs) {
        this.connectionTimeoutSecs = connectionTimeoutSecs;
    }

    public int getSoTimeoutSecs() {
        return soTimeoutSecs;
    }

    public void setSoTimeoutSecs(int soTimeoutSecs) {
        this.soTimeoutSecs = soTimeoutSecs;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
        this.requestSentRetryEnabled = requestSentRetryEnabled;
    }

    public boolean isTimeoutRetryEnabled() {
        return timeoutRetryEnabled;
    }

    public void setTimeoutRetryEnabled(boolean timeoutRetryEnabled) {
        this.timeoutRetryEnabled = timeoutRetryEnabled;
    }
}
