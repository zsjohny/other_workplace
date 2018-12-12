package com.onway.baib.core.model;

import com.onway.platform.common.enums.PlatformCodeEnum;

public interface Constant {

    final PlatformCodeEnum platformCodeEnum             = PlatformCodeEnum.YD_PLATFORM;

    final String           SYS_DEFAULT_STR              = "-1";

    final String           USER_CREDIT_LIMIT_APPLY_EXIT = "该用户的信用额度申请已经存在";

    final String           EXCEPTION_MESSAGE            = "服务异常，请稍后尝试";

    final String           SYS_BUSY                     = "系统繁忙,请稍后再试";

    final String           UPDATE_MESSAGE               = "您需要升级到最新版本才能使用该功能";

    final String           ILLEGAL_REQUEST              = "非法请求";

    final String           USERID_ISNULL                = "用户ID为空";

    final String           TOKEN_ERROR                  = "token不正确";

    final String           USER_ID                      = "userId";

    final String           REGISTER_CHANNEL             = "registerChannel";

    final String           TOKEN                        = "token";

    final String           APP_TYPE                     = "appType";

    final String           VERSION                      = "version";

    final String           SIGN_T                       = "sign_t";

    final String           ORDER_STATUS                 = "orderStatus";

    final PlatformCodeEnum YOU_MI                       = Constant.platformCodeEnum;

    final String           SIGN                         = "sign";

    final String           TIME                         = "stime";

    final String           CHECK_CODE                   = "checkCode";

    final String           SIGN_SEED                    = "onway888888";

    final String           PROD_CODE                    = "prodCode";

    final int              PAGE_NUM_DIGIT               = 1;

    final int              PAGE_SIZE_DIGIT              = 10;

    final String           PAGE_NUM                     = "pageNum";

    final String           PAGE_SIZE                    = "pageSize";

    final String           TRANSCODING_ERROR            = "编码方式转型异常";

    final String           UPLOAD_SUCCESS               = "上传成功";

    final String           UPLOAD_ERROR                 = "上传异常";

    final String           UTF_8                        = "UTF-8";
}
