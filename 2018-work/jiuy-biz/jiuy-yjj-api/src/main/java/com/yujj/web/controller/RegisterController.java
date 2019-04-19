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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yujj.business.facade.CaptchaFacade;
import com.yujj.business.facade.RegisterFacade;
import com.yujj.business.service.RegisterService;
import com.yujj.business.service.UserService;
import com.yujj.business.service.common.MailService;
import com.jiuyuan.constant.CaptchaType;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.CaptchaParams;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.MailRegister;
import com.jiuyuan.util.AESUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.util.uri.UriBuilder;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private RegisterFacade registerFacade;

    @Autowired
    private CaptchaFacade captchaFacade;

    @RequestMapping("/mail")
    public String mailRegisterForm(UserDetail userDetail, Map<String, Object> model) {
        String nonce = UUID.randomUUID().toString();
        model.put("captchaNonce", nonce);

        long time = System.currentTimeMillis();
        model.put("captchaTime", time);

        String cacheKey = captchaFacade.getCacheKey(userDetail.getUserId(), nonce, time);
        String signature = captchaFacade.generateSignature(cacheKey);
        model.put("signature", signature);

 //       return "register/mail/form";
        return "/";
    }
    
    @RequestMapping(value = "/mail/checkusername", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse checkUserName(@RequestParam("username") String userName) {
        JsonResponse jsonResponse = new JsonResponse();

        User user = userService.getUserByAllWay(userName);
        if (user != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_USER_NAME_EXISTS);
        }

        long time = System.currentTimeMillis();
        MailRegister mailRegister = registerService.getMailRegister(userName, time);
        if (mailRegister != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_USER_NAME_EXISTS);
        }

        return jsonResponse.setSuccessful();
    }

    @RequestMapping(value = "/mail/checkmail", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse checkEmail(@RequestParam("email") String email) {
        JsonResponse jsonResponse = new JsonResponse();

//        User user = userService.getUserByRelatedName(email, UserType.EMAIL);
        User user = userService.getUserByAllWay(email);

        if (user != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_EMAIL_REGISTED);
        }

        return jsonResponse.setSuccessful();
    }

    @RequestMapping(value = "/mail/confirm", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse mailRegisterConfirm(@RequestParam("username") String userName,
                                            @RequestParam("email") String email,
                                            @RequestParam("password") String password,
                                            @RequestParam("repeat_password") String repeatPassword,
                                            CaptchaParams captchaParams, UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        if (!userName.matches("[a-zA-z0-9_]+")) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        } else if (StringUtils.length(password) < 6) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        } else if (!StringUtils.equals(password, repeatPassword)) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PASSWORD_MISMATCH);
        }

        String cacheKey = captchaFacade.getAndValidateCacheKey(userDetail.getUserId(), captchaParams);
        if (!captchaFacade.verifyCode(captchaParams.getCode(), CaptchaType.REGISTER, cacheKey, true)) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_CAPTCHA_INVALID);
        }

        User user = userService.getUserByAllWay(userName);
        if (user != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_USER_NAME_EXISTS);
        }

        long time = System.currentTimeMillis();
        MailRegister mailRegister = registerService.getMailRegister(userName, time);
        if (mailRegister != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_USER_NAME_EXISTS);
        }

//        user = userService.getUserByRelatedName(email, UserType.EMAIL);
        user = userService.getUserByAllWay(email);

        if (user != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_EMAIL_REGISTED);
        }

        mailRegister = new MailRegister();
        mailRegister.setRegisterUuid(UUID.randomUUID().toString());
        mailRegister.setUserName(userName);
        mailRegister.setUserEmail(email);
        mailRegister.setUserPassword(DigestUtils.md5Hex(password));
        mailRegister.setExpireTime(time + DateUtils.MILLIS_PER_DAY); // userName占用一天
        mailRegister.setCreateTime(time);
        registerService.addMailRegister(mailRegister);
        
        sendRegisterMailInternal(mailRegister);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", mailRegister.getRegisterUuid());

        return jsonResponse.setData(data).setSuccessful();
    }

    @RequestMapping(value = "/mail/sendmail", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse sendRegisterMail(@RequestParam("id") String registerUuid) {
        JsonResponse jsonResponse = new JsonResponse();

        MailRegister mailRegister = registerService.getMailRegister(registerUuid);
        if (mailRegister == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        } else if (mailRegister.getExpireTime() < System.currentTimeMillis()) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_REGISTER_EXPIRE);
        }

        sendRegisterMailInternal(mailRegister);

        return jsonResponse.setSuccessful();
    }

    private static final String REGISTER_TOKEN_ENCRYPT_PASSWORD = "3j&*u&^JK@#6R()";
    
    private boolean sendRegisterMailInternal(MailRegister mailRegister) {
        JSONObject params = new JSONObject();

        JSONArray emails = new JSONArray();
        emails.add(mailRegister.getUserEmail());
        params.put("%email%", emails);

        JSONArray links = new JSONArray();
        String token = AESUtil.encrypt(mailRegister.getRegisterUuid(), "UTF-8", REGISTER_TOKEN_ENCRYPT_PASSWORD);
        links.add(new UriBuilder(Constants.SERVER_URL + "/register/mail/active.do").set("token", token).toUri());
        params.put("%link%", links);

        return mailService.sendMail("俞姐姐账户激活邮件", params, mailRegister.getUserEmail(), "mail_active_template");
    }

    @RequestMapping(value = "/mail/active")
    public String mailActive(@RequestParam("token") String token, Map<String, Object> model, ClientPlatform clientPlatform) {
        String registerUuid = AESUtil.decrypt(token, "UTF-8", REGISTER_TOKEN_ENCRYPT_PASSWORD);
        MailRegister mailRegister = registerService.getMailRegister(registerUuid);
        if (mailRegister == null) {
            model.put("msg", "注册信息不存在");
            return "register/mail/active_fail";
        }
        long time = System.currentTimeMillis();
        if (time > mailRegister.getExpireTime()) {
            model.put("msg", "链接已失效");
            return "register/mail/active_fail";
        }

//        User user = userService.getUserByRelatedName(mailRegister.getUserEmail(), UserType.EMAIL);
        User user = userService.getUserByAllWay(mailRegister.getUserEmail());

        if (user != null) {
            model.put("msg", "账号已激活");
            return "register/mail/active_fail";
        }

        user = User.coryFrom(mailRegister);
        registerFacade.addUser(user, null, clientPlatform);

        return "register/mail/active_success";
    }

}
