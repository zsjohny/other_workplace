package com.jiuyuan.util.oauth.v2.credential;

public class V2TokenCredentials implements IV2TokenCredentials {

    private static final long serialVersionUID = 5961127570413291788L;

    private String identifier;

    private String refreshToken;

    private Integer expiresIn;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String getSecret() {
        return null;
    }
}
