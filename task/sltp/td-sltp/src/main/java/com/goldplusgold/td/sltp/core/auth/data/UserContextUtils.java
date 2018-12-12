package com.goldplusgold.td.sltp.core.auth.data;


import com.goldplusgold.td.sltp.core.auth.constant.LoginParamsEnum;
import com.goldplusgold.td.sltp.core.auth.constant.PlatformEnum;

import javax.servlet.http.HttpServletRequest;

public class UserContextUtils {

    /**
     * 设置一些金专家与TD用户常用数据，在上下文中使用
     */
    public static void set(HttpServletRequest req,
                           IUserContext userContext) {

        String jzjUserId = (String) req.getAttribute(LoginParamsEnum.JZJUSERID.toName());
        String jzjUserName = (String) req.getAttribute(LoginParamsEnum.JZJUSERNAME.toName());
        String platform = (String) req.getAttribute(LoginParamsEnum.PLATFORM.toName());
        String imei = (String) req.getAttribute(LoginParamsEnum.IMEI.toName());
        String clientId = (String) req.getAttribute(LoginParamsEnum.CLIENTID.toName());
        String custId = (String) req.getAttribute(LoginParamsEnum.TDCUSTID.toName());
        String acctNo = (String) req.getAttribute(LoginParamsEnum.TDACCTNO.toName());
        //Integer tdUserID = (Integer) req.getAttribute(LoginParamsEnum.TDUSERID.toName());
        String sessionId = (String) req.getAttribute(LoginParamsEnum.TDSESSIONID.toName());
        String sessionKey = (String) req.getAttribute(LoginParamsEnum.TDSESSIONKEY.toName());

        userContext.setJzjUserID(jzjUserId);
        userContext.setJzjUserName(jzjUserName);
        userContext.setImei(imei);
        userContext.setClientID(clientId);
        userContext.setPlatform(PlatformEnum.getPlatform(platform));
        userContext.setCustId(custId);
        userContext.setAcctNo(acctNo);
        //userContext.setTdUserID(tdUserID);
        userContext.setTdSessionID(sessionId);
        userContext.setTdSessionKey(sessionKey);
    }
}
