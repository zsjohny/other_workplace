package com.yujj.util.oauth.v2;

import com.jiuyuan.util.oauth.common.Display;
import com.jiuyuan.util.oauth.common.credential.IClientCredentials;
import com.jiuyuan.util.oauth.v2.credential.IRawDataV2TokenCredentials;
import com.yujj.util.uri.UriBuilder;
import com.yujj.util.uri.UriParams;

/**
 * OAuth 2.0 API接口
 */
public interface IV2API {

    IClientCredentials getClientCredentials();

    /**
     * @param callbackParams 在申请授权过程中，可以为回调url指定一些自定义参数。当从OAuth服务方回调应用接口时，这些参数会原样返回。
     * @param state RECOMMENDED. An opaque value used by the client to maintain state between the request and callback.
     *        The authorization server includes this value when redirecting the user-agent back to the client. The
     *        parameter SHOULD be used for preventing cross-site request forgery as described in Section 10.12.
     * @param display 授权页面的终端类型（注意：OAuth 2规范没有对终端类型的支持，但大部分厂商都有相关的实现）
     * @return 请求用户授权的完整url
     */
    UriBuilder getAuthorizeUrl(UriParams callbackParams, String state, Display display);

    /**
     * 获取access token
     * 
     * @param code 授权后服务提供方返回的code。注意code和refresh token只需要传入一个，另外一个传入null。
     * @param refreshToken refresh token。 注意code和refresh token只需要传入一个，另外一个传入null。
     * @param callbackParams 在申请授权过程中，可以为回调url指定一些自定义参数。当从OAuth服务方回调应用接口时，这些参数会原样返回。
     * @return access token, refresh token and expiry
     */
    IRawDataV2TokenCredentials getTokenCredentials(String code, String refreshToken, UriParams callbackParams);

    /**
     * @param callbackParams 在申请授权过程中，可以为回调url指定一些自定义参数。当从OAuth服务方回调应用接口时，这些参数会原样返回。
     * @return 授权完成后，服务方回调的url
     */
    String getCallbackUrl(UriParams callbackParams);
}
