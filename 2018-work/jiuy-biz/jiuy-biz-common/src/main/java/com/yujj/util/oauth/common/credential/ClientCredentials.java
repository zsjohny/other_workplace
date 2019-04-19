package com.yujj.util.oauth.common.credential;

import com.jiuyuan.util.oauth.common.credential.Credentials;
import com.jiuyuan.util.oauth.common.credential.IClientCredentials;

/**
 * client credentials (Consumer Key and Secret)
 */
public class ClientCredentials extends Credentials implements IClientCredentials {

    private static final long serialVersionUID = 3470422920491611710L;

    private String callbackUri;

    public String getCallbackUri() {
        return callbackUri;
    }

    public void setCallbackUri(String callbackUri) {
        this.callbackUri = callbackUri;
    }

    public ClientCredentials() {
        //
    }

    public ClientCredentials(String identifier, String secret, String callbackUri) {
        super(identifier, secret);
        this.callbackUri = callbackUri;
    }
}
