package com.jiuyuan.util.oauth.v2.credential;

import java.util.Map;

import com.jiuyuan.util.http.NumberUtil;
import com.jiuyuan.util.oauth.v2.V2Constants;

/**
 * OAuth 2.0 access token
 */
public class RawDataV2TokenCredentials implements IRawDataV2TokenCredentials, V2Constants {

    private static final long serialVersionUID = -7750812837413158847L;

    private Map<String, ?> data;

    @Override
    public String getIdentifier() {
        return getRawDataAsString(ACCESS_TOKEN);
    }

    @Override
    public String getSecret() {
        return null;
    }

    public Integer getExpiresIn() {
        int expiresIn = NumberUtil.parseInt(getRawDataAsString(EXPIRES_IN), -1, false);
        return expiresIn > 0 ? expiresIn : null;
    }

    public String getRefreshToken() {
        return getRawDataAsString(REFRESH_TOKEN);
    }

    public RawDataV2TokenCredentials(Map<String, ?> data) {
        this.data = data;
    }

    public RawDataV2TokenCredentials(RawDataV2TokenCredentials credentials) {
        this.data = credentials.data;
    }

    @Override
    public String getRawDataAsString(String key) {
        Object val = data.get(key);
        return val != null ? val.toString() : null;
    }
}
