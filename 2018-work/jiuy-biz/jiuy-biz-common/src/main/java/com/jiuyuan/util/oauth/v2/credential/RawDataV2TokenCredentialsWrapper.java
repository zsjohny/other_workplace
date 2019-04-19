package com.jiuyuan.util.oauth.v2.credential;

import java.util.Map;

public class RawDataV2TokenCredentialsWrapper implements IRawDataV2TokenCredentials {

    private static final long serialVersionUID = 721012977468093453L;

    private IRawDataV2TokenCredentials delegate;

    private Map<String, ?> data;

    public RawDataV2TokenCredentialsWrapper(IRawDataV2TokenCredentials tokenCredentials, Map<String, ?> data) {
        this.delegate = tokenCredentials;
        this.data = data;
    }

    @Override
    public Integer getExpiresIn() {
        return delegate.getExpiresIn();
    }

    @Override
    public String getRefreshToken() {
        return delegate.getRefreshToken();
    }

    @Override
    public String getIdentifier() {
        return delegate.getIdentifier();
    }

    @Override
    public String getSecret() {
        return delegate.getSecret();
    }

    @Override
    public String getRawDataAsString(String key) {
        Object value = data.get(key);
        if (value != null) {
            return value.toString();
        }
        return delegate.getRawDataAsString(key);
    }
}
