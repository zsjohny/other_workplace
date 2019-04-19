package com.jiuyuan.util.oauth.common.credential;

import java.io.Serializable;

/**
 * client credentials (Consumer Key and Secret), temporary credentials (Request Token and Secret) or token credentials
 * (Access Token and Secret)
 */
public class Credentials implements ICredentials, Serializable {

    private static final long serialVersionUID = -1533045598238527591L;

    private String identifier;

    private String secret;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Credentials() {
        //
    }

    public Credentials(String identifier, String secret) {
        this.identifier = identifier;
        this.secret = secret;
    }
}
