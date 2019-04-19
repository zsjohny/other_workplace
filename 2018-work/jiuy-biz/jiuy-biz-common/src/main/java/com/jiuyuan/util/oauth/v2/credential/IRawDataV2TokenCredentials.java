package com.jiuyuan.util.oauth.v2.credential;

import com.jiuyuan.util.oauth.common.credential.IRawDataCredentials;

public interface IRawDataV2TokenCredentials extends IRawDataCredentials, IV2TokenCredentials {

    String getRawDataAsString(String key);
}
