package com.jiuyuan.util.oauth.v2.credential;

import com.jiuyuan.util.oauth.common.credential.ICredentials;

public interface IV2TokenCredentials extends ICredentials {

    Integer getExpiresIn();

    String getRefreshToken();
}
