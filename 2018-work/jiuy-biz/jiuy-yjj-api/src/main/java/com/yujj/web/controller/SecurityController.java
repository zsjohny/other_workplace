package com.yujj.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yujj.business.facade.CaptchaFacade;
import com.yujj.business.service.UserService;
import com.yujj.business.service.common.MailService;
import com.jiuyuan.constant.CaptchaType;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.CaptchaParams;
import com.jiuyuan.util.AESUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.util.uri.UriBuilder;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;

@Controller
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private CaptchaFacade captchaFacade;

    @RequestMapping("/mail/resetpwd")
    public String resetPassword(UserDetail userDetail, Map<String, Object> model) {
        String nonce = UUID.randomUUID().toString();
        model.put("captchaNonce", nonce);

        long time = System.currentTimeMillis();
        model.put("captchaTime", time);

        String cacheKey = captchaFacade.getCacheKey(userDetail.getUserId(), nonce, time);
        String signature = captchaFacade.generateSignature(cacheKey);
        model.put("signature", signature);

        return "security/mail/reset_password";
    }

    private static final String RESETPWD_TOKEN_ENCRYPT_PASSWORD = "LU*&JKkjldsafy#_IUOI_DF(*!3";

    @RequestMapping("/mail/resetpwd/sendmail")
    @ResponseBody
    public JsonResponse sendResetPasswordMail(@RequestParam("email") String email, CaptchaParams captchaParams,
                                              UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        
        CaptchaType type = CaptchaType.RESET_PASSWORD;
        String cacheKey = captchaFacade.getAndValidateCacheKey(userDetail.getUserId(), captchaParams);
        boolean successful = captchaFacade.verifyCode(captchaParams.getCode(), type, cacheKey, true);
        if (!successful) {
            return jsonResponse.setResultCode(ResultCode.SECURITY_ERROR_CAPTCHA);
        }


//        User user = userService.getUserByRelatedName(email, UserType.EMAIL);
        User user = userService.getUserByAllWay(email);//


        if (user == null) {
            return jsonResponse.setResultCode(ResultCode.SECURITY_ERROR_EMAIL_NOT_REGISTED);
        }

        long expireTime = System.currentTimeMillis() + DateUtils.MILLIS_PER_DAY;// 激活token一天内有效
        String source = email + "#" + expireTime;
        
        JSONObject params = new JSONObject();

        JSONArray emails = new JSONArray();
        emails.add(email);
        params.put("%email%", emails);

        JSONArray links = new JSONArray();
        String token = AESUtil.encrypt(source, "UTF-8", RESETPWD_TOKEN_ENCRYPT_PASSWORD);
        links.add(new UriBuilder(Constants.SERVER_URL + "/security/mail/resetpwd/reset.do").set("token", token).toUri());
        params.put("%link%", links);

        mailService.sendMail("俞姐姐密码重置邮件", params, email, "");

        return jsonResponse.setSuccessful();
    }

    @RequestMapping("/mail/resetpwd/reset")
    public String resetPassword(@RequestParam("token") String token, Map<String, Object> model) {
        String source = AESUtil.decrypt(token, "UTF-8", RESETPWD_TOKEN_ENCRYPT_PASSWORD);
        String[] parts = StringUtils.split(source, "#");
        long expireTime = Long.parseLong(parts[1]);
        long time = System.currentTimeMillis();
        if (time > expireTime) {
            model.put("msg", "链接已失效");
            return "security/mail/reset_password_fail";
        }

        String email = parts[0];
//        User user = userService.getUserByRelatedName(email, UserType.EMAIL);
        User user = userService.getUserByAllWay(email);

        if (user == null) {
            model.put("msg", "账号不存在");
            return "security/mail/reset_password_fail";
        }

        return "security/mail/reset_password_form";
    }

    @RequestMapping("/mail/resetpwd/reset/commit")
    @ResponseBody
    public JsonResponse commitResetPassword(@RequestParam("token") String token,
                                            @RequestParam("password") String password,
                                            @RequestParam("repeat_password") String repeatPassword) {
        JsonResponse jsonResponse = new JsonResponse();

        String source = AESUtil.decrypt(token, "UTF-8", RESETPWD_TOKEN_ENCRYPT_PASSWORD);
        String[] parts = StringUtils.split(source, "#");
        long expireTime = Long.parseLong(parts[1]);
        long time = System.currentTimeMillis();
        if (time > expireTime) {
            return jsonResponse.setResultCode(ResultCode.SECURITY_ERROR_TOKEN_EXPIRE);
        } else if (StringUtils.length(password) < 6) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        } else if (!StringUtils.equals(password, repeatPassword)) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        String email = parts[0];
//        User user = userService.getUserByRelatedName(email, UserType.EMAIL);
        User user = userService.getUserByAllWay(email);
        if (user == null) {
            return jsonResponse.setResultCode(ResultCode.SECURITY_ERROR_EMAIL_NOT_REGISTED);
        }

        userService.updateUserPassword(user.getUserId(), DigestUtils.md5Hex(password));

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("redirectUrl", new UriBuilder("/security/mail/resetpwd/reset/success.do"));

        return jsonResponse.setData(data).setSuccessful();
    }

    @RequestMapping("/mail/resetpwd/reset/success")
    public String resetPasswordSuccess() {
        return "security/mail/reset_password_success";
    }

}
