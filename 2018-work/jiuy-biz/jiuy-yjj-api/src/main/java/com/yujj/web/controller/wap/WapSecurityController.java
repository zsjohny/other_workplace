package com.yujj.web.controller.wap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.web.controller.delegate.SecurityDelegator;

@Controller
@RequestMapping("/m/security")
public class WapSecurityController {

	private static final Logger logger = LoggerFactory.getLogger(WapSecurityController.class);
	
    @Autowired
    private SecurityDelegator securityDelegator;

    @RequestMapping("/phone/resetpwd")
    public String phoneResetPasswrodForm() {
        return "wap/security/phone/resetpwd";
    }

    @RequestMapping(value = "/phone/resetpwd/code", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse sendPhoneVerifyCode(@RequestParam("phone") String phone,ClientPlatform clientPlatform, @ClientIp String ip) {
    	logger.error("WapRegisterController clientPlatform:{}, ip:{}", clientPlatform.getPlatform(), ip);
        return securityDelegator.sendPhoneVerifyCode(phone);
    }

    @RequestMapping("/phone/editpwd")
    public String phoneEditPasswrodForm() {
        return "wap/security/phone/editpwd";
    }

    @RequestMapping(value = "/phone/resetpwd/commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse resetPasswordCommit(@RequestParam("phone") String phone,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("verify_code") String verifyCode) {
        return securityDelegator.resetPasswordCommit(phone, password, verifyCode);
    }

}
