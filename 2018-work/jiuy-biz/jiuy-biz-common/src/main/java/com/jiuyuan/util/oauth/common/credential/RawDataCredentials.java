package com.jiuyuan.util.oauth.common.credential;

import java.util.Map;

import com.jiuyuan.util.oauth.v2.V2Constants;


public class RawDataCredentials implements IRawDataCredentials, V2Constants {

    private static final long serialVersionUID = -7750812837413158847L;

    protected Map<String, ?> data;

    @Override
    public String getIdentifier() {
        return getRawDataAsString(ACCESS_TOKEN);
    }

    @Override
    public String getSecret() {
        return getRawDataAsString(TOKEN_SECRET);
    }

    public RawDataCredentials(Map<String, ?> data) {
        this.data = data;
    }

    public RawDataCredentials(RawDataCredentials credentials) {
        this.data = credentials.data;
    }

    @Override
    public String getRawDataAsString(String key) {
        Object val = data.get(key);
        return val != null ? val.toString() : null;
    }
}
