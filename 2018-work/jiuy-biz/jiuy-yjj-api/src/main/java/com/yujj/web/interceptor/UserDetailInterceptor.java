package com.yujj.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yujj.business.service.UserService;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.CookieConstants;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.account.DefaultUserDetail;
import com.jiuyuan.entity.account.UserLoginLog;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.util.http.CookieUtil;
import com.yujj.entity.account.User;

public class UserDetailInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        DefaultUserDetail userDetail = new DefaultUserDetail();

        String[] parts = LoginUtil.getLoginCookieParts(request);
        if (parts != null) {
            try {
            	String userRelatedName = parts[0];
                long time = System.currentTimeMillis();
                UserLoginLog userLoginLog = userService.getUserNewestLoginLog(userRelatedName);
                if(userLoginLog != null && time - userLoginLog.getCreateTime() < DateUtils.MILLIS_PER_DAY * 15 ){
                	 UserType userType = UserType.parse(Integer.parseInt(parts[1]));

//                	 User user = userService.getUserByRelatedName(userRelatedName, userType);
                	 User user = userService.getUserByAllWay(userRelatedName);

                     if (user != null) {
                         userDetail.setUser(user);
                     }
                }
//                long createTime = Long.parseLong(parts[2]);
//                if (time - createTime < DateUtils.MILLIS_PER_DAY * 2) {
//                    UserType userType = UserType.parse(Integer.parseInt(parts[1]));
//                    User user = userService.getUserByRelatedName(userRelatedName, userType);
//                    if (user != null) {
//                        userDetail.setUser(user);
//                    }
//                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }else{
        }

        String virtualDeviceId = checkAndSetVirtualDeviceId(request, response);
        userDetail.setVirtualDeviceId(virtualDeviceId);

        request.setAttribute(Constants.KEY_USER_DETAIL, userDetail);

        return true;
    }

    private String checkAndSetVirtualDeviceId(HttpServletRequest request, HttpServletResponse response) {
        String virtualDeviceId = CookieUtil.getCookieValue(request, CookieConstants.COOKIE_NAME_VIRTUAL_DEVICEID, "");
        if (StringUtils.isNotBlank(virtualDeviceId)) {
            return virtualDeviceId;
        }
        virtualDeviceId = System.currentTimeMillis() + RandomStringUtils.randomNumeric(3);
        int expires = Integer.MAX_VALUE;
        String cookieValue =
            CookieUtil.buildCookieHeaderValue(CookieConstants.COOKIE_NAME_VIRTUAL_DEVICEID, virtualDeviceId, "/",
                CookieConstants.COOKIE_DOMAIN, expires, true);
        response.addHeader("Set-Cookie", cookieValue);
        return virtualDeviceId;
    }

}
