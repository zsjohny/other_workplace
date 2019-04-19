package com.jiuyuan.util.oauth.sns.common.api;

import com.jiuyuan.util.oauth.common.credential.ICredentials;
import com.jiuyuan.util.oauth.sns.common.response.ISnsResponse;
import com.jiuyuan.util.oauth.sns.common.status.IImageStatus;
import com.jiuyuan.util.oauth.sns.common.status.IStatus;
import com.jiuyuan.util.oauth.sns.common.user.ISnsEndUser;

/**
 * SNS接口
 */
public interface ISnsAPI {

    ISnsResponse<Void> publish(ICredentials tokenCredentials, IStatus status, String clientIP);

    /**
     * @param imageFileName 注意必须包含图片的扩展名（厂商的实现可能会根据扩展名判断图片类型是否支持）
     */
    ISnsResponse<Void> publishWithImage(ICredentials tokenCredentials, IStatus status, String imageFileName,
                                        byte[] imageBytes, String clientIP);

    ISnsResponse<Void> publishWithImage(ICredentials tokenCredentials, IImageStatus status, String clientIP);

    ISnsResponse<ISnsEndUser> getEndUser(ICredentials tokenCredentials, String clientIP);

}