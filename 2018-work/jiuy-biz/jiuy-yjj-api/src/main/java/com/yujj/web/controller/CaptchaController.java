package com.yujj.web.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.CaptchaType;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.CaptchaParams;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.util.uri.UriBuilder;
import com.yujj.business.facade.CaptchaFacade;
import com.yujj.business.service.UserService;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.LoginDelegator;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CaptchaFacade captchaFacade;

    @Autowired
    private LoginDelegator loginDelegator;
    /**
     * 
     * 参数说明：CaptchaParams
     */
    @RequestMapping(produces = "image/jpeg")
    @ResponseBody
    public BufferedImage createCaptcha(@RequestParam(value = "type") String strType, String nonce, long time,
                                       String signature, UserDetail userDetail) {
        CaptchaType type = CaptchaType.fromStringValue(strType);
        String cacheKey = captchaFacade.getAndValidateCacheKey(userDetail.getUserId(), nonce, time, signature);

        try {
            return captchaFacade.create(type, cacheKey, DateConstants.SECONDS_PER_MINUTE * 5);
        } catch (IOException e) {
            logger.error("Captcha creation fail: {}.", e.getMessage());
        }
        return null;
    }
    /**
     * 获取图形验证码信息
     * @param strType	login（登录）、register（注册）、resetpwd（重置密码）
     * @param userDetail	userId
     * @return
     */
    @RequestMapping(value = "/gen", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse generateCaptcha(@RequestParam(value = "type") String strType, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        /*验证码类型*/
        CaptchaType captchaType = CaptchaType.fromStringValue(strType);
        UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/captcha.do");
        builder.set("type", captchaType.getStringValue());
        String nonce = UUID.randomUUID().toString();
        builder.set("nonce", nonce);
        long time = System.currentTimeMillis();
        builder.set("time", time);
        
        String cacheKey = captchaFacade.getCacheKey(userDetail.getUserId(), nonce, time);
        String signature = captchaFacade.generateSignature(cacheKey);
        builder.set("signature", signature);

        data.put("nonce", nonce);
        data.put("time", time);
        data.put("signature", signature);
        data.put("captchaUrl", builder.toUri());

        return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 验证图形验证码
     * @param strType	login（登录）、register（注册）、resetpwd（重置密码）
     * @param captchaParams	code	nonce	time	signature
     * @param userDetail	userId
     * @return
     */
    @RequestMapping(value = "/validate")
    @ResponseBody
    public JsonResponse validateCaptcha(@RequestParam(value = "type") String strType,@RequestParam(value = "username") String username, CaptchaParams captchaParams,
                                        UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        long userId = userDetail.getUserId();
        CaptchaType captchaType = CaptchaType.fromStringValue(strType);
        String cacheKey = captchaFacade.getAndValidateCacheKey(userId, captchaParams);
       
        if (!captchaFacade.verifyCode(captchaParams.getCode(), captchaType, cacheKey, false)) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_VALIDATE_CODE);
        }
        
        User user = userService.getUserByAllWay(username);
        if(user != null){
        	//验证通过后将用户的密码错误次数减掉一次
            String userIdStr = String.valueOf(user.getUserId());
            loginDelegator.clearOnePasswordErrorExceedConfine(userIdStr);
        }
        
        return jsonResponse.setSuccessful();
    }
    
   
}
