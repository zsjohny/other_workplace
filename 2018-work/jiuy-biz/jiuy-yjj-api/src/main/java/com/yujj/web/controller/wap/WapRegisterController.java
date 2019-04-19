package com.yujj.web.controller.wap;

import javax.servlet.http.HttpServletResponse;

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
import com.yujj.web.controller.delegate.RegisterDelegator;

/**
 * 移动端注册
 */
@Controller
@RequestMapping("/m/register")
public class WapRegisterController {

	private static final Logger logger = LoggerFactory.getLogger(WapRegisterController.class);
	
    @Autowired
    private RegisterDelegator registerDelegator;

    @RequestMapping("/phone")
    public String phoneRegisterForm() {
        return "wap/register/phone/form";
    }

    @RequestMapping(value = "/phone/code", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse sendPhoneVerifyCode(@RequestParam("phone") String phone, UserDetail userDetail,ClientPlatform clientPlatform, @ClientIp String ip) {
    	logger.error("WapRegisterController clientPlatform:{}, ip:{}", clientPlatform.getPlatform(), ip);
        long userId = userDetail.getUserId();
        return registerDelegator.sendPhoneVerifyCode(phone, null, false, userId);
    }

    @RequestMapping("/phone/account")
    public String phoneCommitForm() {
        return "wap/register/phone/account";
    }

    @RequestMapping(value = "/phone/commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse phoneNumberCommit(@RequestParam("phone") String phone,
                                          @RequestParam("password") String password,
                                          @RequestParam("verify_code") String verifyCode,
                                          @RequestParam(value = "invite_code", required = false) String inviteCode, @ClientIp String ip, ClientPlatform client,
                                          HttpServletResponse response) {
        return registerDelegator.phoneNumberCommit(phone, password, verifyCode, inviteCode, response, ip, client);
    }

}
