package com.jiuyuan.util.oauth.common.credential;

import java.io.Serializable;

/**
 * client credentials (Consumer Key and Secret), temporary credentials (Request Token and Secret) or token credentials
 * (Access Token and Secret)
 */
public interface ICredentials extends Serializable {

    String getIdentifier();

    String getSecret();
}
