package com.finace.miscroservice.getway.util;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;

import javax.servlet.http.HttpServletRequest;

/**
 * options请求的检查
 */
public class OptionsCheckUtil {

    private void OptionsCheckUtil() {

    }

    private static Log log = Log.getInstance(OptionsCheckUtil.class);

    private static final String OPTIONS_REQUEST_METHOD = "OPTIONS";

    /**
     * 检查options 是否包含
     *
     * @param request
     * @return
     */
    public static Boolean isContain(HttpServletRequest request) {
        if (OPTIONS_REQUEST_METHOD.equals(request.getMethod())) {
            log.info("IP={}下用户访问方法={}开始进行预检查", Iptools.gainRealIp(request), request.getRequestURI());
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }


}
