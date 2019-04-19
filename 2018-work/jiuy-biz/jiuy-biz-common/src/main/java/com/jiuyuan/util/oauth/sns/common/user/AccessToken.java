package com.jiuyuan.util.oauth.sns.common.user;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.jiuyuan.constant.Tristate;

public class AccessToken implements IAccessToken {

    private static final long serialVersionUID = 7762198778972903375L;

    private String accessToken;

    private String expiresIn;

    private String refreshToken;

    private String openid;

    private String scope;
    
    public AccessToken() {
		
	}
    
    public AccessToken(String accessToken,String expiresIn,String refreshToken,String openid,String scope) {
    	this.accessToken = accessToken;
    	this.expiresIn = expiresIn;
    	this.refreshToken = refreshToken;
    	this.openid = openid;
    	this.scope = scope;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "AccessToken [accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", refreshToken=" + refreshToken
				+ ", openid=" + openid + ", scope=" + scope + "]";
	}

   
}