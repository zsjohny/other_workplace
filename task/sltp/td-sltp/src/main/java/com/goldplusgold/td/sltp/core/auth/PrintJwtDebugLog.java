package com.goldplusgold.td.sltp.core.auth;

import com.goldplusgold.td.sltp.core.auth.constant.LoginParamsEnum;
import com.goldplusgold.td.sltp.core.auth.constant.TokenInfo;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 打印jwt的debug日志
 */
class PrintJwtDebugLog {

    private static final Logger logger = LoggerFactory.getLogger(PrintJwtDebugLog.class);

    public static void printDebugLog(ServletRequest request,
                                     boolean flag,
                                     String jwtToken) {

        if (logger.isDebugEnabled() && flag) {
            HttpServletRequest req = WebUtils.toHttp(request);
            String jzjUserId = (String) req.getAttribute(LoginParamsEnum.JZJUSERID.toName());
            String jzjUserName = (String) req.getAttribute(LoginParamsEnum.JZJUSERNAME.toName());
            String platform = (String) req.getAttribute(LoginParamsEnum.PLATFORM.toName());
            String imei = (String) req.getAttribute(LoginParamsEnum.IMEI.toName());
            String clientId = (String) req.getAttribute(LoginParamsEnum.CLIENTID.toName());
            Long expiration = (Long) req.getAttribute(TokenInfo.EXPIRATION);

            logger.debug("-------------------- isAccessAllowed() VERIFY JWT TOKEN ----------------------");
            logger.debug("jzjUserId: {}", jzjUserId);
            logger.debug("jzjUserName: {}", jzjUserName);
            logger.debug("platform: {}", platform);
            logger.debug("imei: {}", imei);
            logger.debug("clientId: {}", clientId);
            logger.debug("expiration: {}", expiration);
            logger.debug("JWT TOKEN : {}", jwtToken);
            logger.debug("===================== isAccessAllowed() VERIFY JWT TOKEN =====================");
        }
    }
}
