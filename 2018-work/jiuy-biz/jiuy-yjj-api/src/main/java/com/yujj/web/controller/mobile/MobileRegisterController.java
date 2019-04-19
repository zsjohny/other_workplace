package com.yujj.web.controller.mobile;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.CaptchaParams;
import com.jiuyuan.entity.ClientPlatform;
import com.yujj.entity.account.UserDetail;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.web.controller.delegate.RegisterDelegator;

/**
 * 移动端注册
 */
@Controller
@RequestMapping("/mobile/register")
public class MobileRegisterController {

    private static final Logger logger = LoggerFactory.getLogger(MobileRegisterController.class);
    private static final Logger loggerAttack = LoggerFactory.getLogger("ATTACK");
    
    
    
    @Autowired
    private RegisterDelegator registerDelegator;

    @RequestMapping(value = "/phone/code", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse sendPhoneVerifyCode(@RequestParam("phone") String phone, CaptchaParams captchaParams,
                                            ClientPlatform clientPlatform, UserDetail userDetail, @ClientIp String ip) {    	
        long userId = userDetail.getUserId();
        boolean needCaptcha = clientPlatform.needRegisterCaptcha();
        return registerDelegator.sendPhoneVerifyCode(phone, captchaParams, needCaptcha, userId);
    }
    
    @RequestMapping(value = "/phone/verify_code", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse sendPhoneSimpleVerifyCode(@RequestParam("phone") String phone,
            ClientPlatform clientPlatform, UserDetail userDetail, @ClientIp String ip) {

    	JsonResponse jsonResponse = new JsonResponse();
    	if (clientPlatform.isAndroid() || clientPlatform.isIphone())
    		return registerDelegator.sendPhoneVerifyCode(phone, null, false, 0);
    	else {
        	loggerAttack.info("MobileRegisterController sendPhoneSimpleVerifyCode clientPlatform:{}, ip:{}, phone:{}", clientPlatform.getPlatform(), ip, phone);
    	}
    		
    	return jsonResponse;
    }
   
    @RequestMapping(value = "/phone/commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse phoneNumberCommit(@RequestParam("phone") String phone,
                                          @RequestParam("password") String password,
                                          @RequestParam("verify_code") String verifyCode,
                                          @RequestParam(value = "invite_code", required = false) String inviteCode,  @ClientIp String ip, ClientPlatform client,
                                          HttpServletResponse response) {
        return registerDelegator.phoneNumberCommit(phone, password, verifyCode, inviteCode, response , ip ,client);
    }
    
    /**
     * @author zhanqian
     * 验证码
     */
    @RequestMapping(value = "/verifyCode18", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse verifyCode18(@RequestParam("phone") String phone,
                                            @RequestParam("verify_code") String verifyCode,
                                            HttpServletResponse response) {
        return registerDelegator.verifyCode18(phone, verifyCode, response);
    }
    
    /**
     * @author zhanqian
     * 验证码
     */
    @RequestMapping(value = "/phone/commit18")
    @ResponseBody
    public JsonResponse phoneNumberCommit18(@RequestParam("phone") String phone,
                                            @RequestParam("password") String password,
                                            @RequestParam("verify_code") String verifyCode,
                                            @RequestParam(value = "yjj_number", required = false, defaultValue = "-1") long yJJNumber,
                                            @RequestParam(value = "store_id", required = false, defaultValue = "-1") long storeId,
                                            @RequestParam(value = "product_id", required = false, defaultValue = "-1") long productId,
                                            ClientPlatform clientPlatform, @ClientIp String ip,
                                            HttpServletResponse response) {
        return registerDelegator.phoneNumberCommit18(phone, password, verifyCode, yJJNumber, storeId, productId, response, clientPlatform,ip);
    }

}
