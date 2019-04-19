package com.util;

import com.jiuy.base.exception.BizException;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.web.help.JsonResponse;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/18 17:45
 * @Copyright 玖远网络
 */
public class WebUtil {

    /**
     * 统一处理异常
     *
     * @param e e
     * @date  2018/4/20 10:11
     * @author  Aison
     */
    public static JsonResponse exceptionHandler(Throwable e) {
        e.printStackTrace();
        if (e instanceof BizException) {
            BizException be = (BizException) e;
            return new JsonResponse().setCode(be.getCode()).setError(be.getMsg());
        } else {
            return new JsonResponse().setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("系统繁忙请稍后再试");
        }
    }
}
