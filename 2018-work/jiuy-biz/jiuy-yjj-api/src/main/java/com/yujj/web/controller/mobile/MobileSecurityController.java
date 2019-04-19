package com.yujj.web.controller.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.ClientPlatform;
import com.yujj.entity.account.UserDetail;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.web.controller.delegate.SecurityDelegator;

@Controller
@RequestMapping("/mobile/security")
public class MobileSecurityController {

    private static final Logger logger = LoggerFactory.getLogger(MobileSecurityController.class);
    
    @Autowired
    private SecurityDelegator securityDelegator;

    @RequestMapping(value = "/phone/resetpwd/code", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse sendPhoneVerifyCode(@RequestParam("phone") String phone,
            ClientPlatform clientPlatform, UserDetail userDetail, @ClientIp String ip) {
    	logger.error("MobileSecurityController sendPhoneVerifyCode clientPlatform:{}, ip:{}", clientPlatform.getPlatform(), ip);
        return securityDelegator.sendPhoneVerifyCode(phone);
    }

    @RequestMapping(value = "/phone/resetpwd/commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse resetPasswordCommit(@RequestParam("phone") String phone,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("verify_code") String verifyCode) {
        return securityDelegator.resetPasswordCommit(phone, password, verifyCode);
    }

}
