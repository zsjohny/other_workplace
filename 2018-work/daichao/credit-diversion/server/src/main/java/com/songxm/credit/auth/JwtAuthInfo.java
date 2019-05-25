package com.songxm.credit.auth;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.songxm.credit.comon.credit.diversion.enums.TokenType;
import com.xinbang.cif.gateway.common.JwtClaim;
import moxie.cloud.service.server.model.AuthInfo;

import java.util.Map;

public class JwtAuthInfo extends AuthInfo {
    private Map<String, Object> claims;

    public JwtAuthInfo(Map<String, Object> claims) {
        this.claims = claims;
    }

    public String getTokenType() {
        return (String) claims.get(JwtClaim.type);
    }

    public String getUserId() {
        return (String) claims.get(JwtClaim.userId);
    }

    public String getOrgCode() {
        return (String) claims.get(JwtClaim.orgCode);
    }

    public String getAudience() {
        return (String) claims.get(JwtClaim.aud);
    }

    public String getJwtID() {
        return (String) claims.get(JwtClaim.jti);
    }

    public String getProductCode() {
        return (String) claims.get(JwtClaim.productCode);
    }

    public String getLoginNo() {
        return (String) claims.get(JwtClaim.loginNo);
    }

    public Integer getExpirationTime() {
        return (Integer) claims.get(JwtClaim.exp);
    }

    public Integer getIssuedAt() {
        return (Integer) claims.get(JwtClaim.iat);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() / 1000 > getExpirationTime();
    }

    @Override
    public boolean isUser() {
        return getTokenType() != null && getTokenType().equalsIgnoreCase(TokenType.USER.name());
    }

    @Override
    public boolean isService() {
        return getTokenType() != null && getTokenType().equalsIgnoreCase(TokenType.SERVICE.name());
    }

    public boolean isTenant() {
        return StringUtils.equalsIgnoreCase(getTokenType(), TokenType.TENANT.name());
    }

    @Override
    public boolean isClient() {
        return getTokenType() != null && getTokenType().equalsIgnoreCase(TokenType.CLIENT.name());
    }

    public Map<String, Object> getClaims() {
        return claims;
    }
}
