package com.jiuyuan.util.oauth.sns.common.user;

import java.io.Serializable;
import java.util.Date;

import com.jiuyuan.constant.Tristate;
/**
 * { "access_token":"ACCESS_TOKEN",    
 "expires_in":7200,    
 "refresh_token":"REFRESH_TOKEN",    
 "openid":"OPENID",    
 "scope":"SCOPE" } 
 access_token	网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
expires_in	access_token接口调用凭证超时时间，单位（秒）
refresh_token	用户刷新access_token
openid	用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
scope	用户授权的作用域，使用逗号（,）分隔
 * @author zhaoxinglin
 *
 * @version 2017年4月7日下午1:03:13
 */
public interface IAccessToken extends Serializable {

    String getAccessToken();

    String getExpiresIn();

    String getRefreshToken();

    String getOpenid();

    String getScope();

}
