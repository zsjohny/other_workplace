package com.jiuyuan.util.oauth.common.credential;

/**
 * client credentials (Consumer Key and Secret)
 */
public interface IClientCredentials extends ICredentials {

    String getCallbackUri();
}
