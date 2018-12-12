package com.finace.miscroservice.getway.interpect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 总的访问拦截
 */
@Component
public class AccessInterceptor {

    @Autowired
    private IpAccessInterceptor ipAccessInterceptor;

    public static final String PASS = "pass";


    public boolean isPass(HttpServletRequest request) {



        //IP拦截
        if (ipAccessInterceptor.interceptor(request)) {

            return false;
        }

        //白名单检查
        switch (IpWhiteList.check(request)) {
            //如果是白名单里面直接略过检查
            case PASS:
                request.setAttribute(PASS, Boolean.TRUE);
                return true;
            case FORBID:

                return false;
            default:
        }

        //黑名单检查
        if (IpBlackList.check(request)) {

            return false;
        }



        return true;


    }

}
