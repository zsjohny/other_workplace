package com.e_commerce.miscroservice.publicaccount.controller;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.enums.colligate.RedisKeyEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.PubAccountLoginUtils;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.publicaccount.service.proxy.SmsService;
import com.e_commerce.miscroservice.publicaccount.service.user.PublicAccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 公账号用户
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 15:35
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "public/yOpen/publicAccountUser" )
public class PublicAccountUserController{


    @Resource( name = "strRedisTemplate" )
    private RedisTemplate<String, String> strRedisTemplate;

    @Value( "${publicaccount.appid}" )
    private String appId;

    @Autowired
    private SmsService smsService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PublicAccountUserService publicAccountUserService;

    /**
     * 登录认证
     *
     * @param openToken     public/yOpen/publicAccountUser/loginByWeiXin 获取
     * @param openId        public/yOpen/publicAccountUser/loginByWeiXin 获取
     * @param response      response
     * @param refereeUserId 推荐人id
     * @param phone         phone
     * @param authCode      手机验证码
     * @return 用户在每次访问其他需要认证的接口时, 这两个都从header传递过来进行认证
     * header:{
     * yopid : 用户openId
     * ytoken : 登录token
     * }
     * body:
     * "data": {
     * "proxyCustomerType" : 0:客户,1 市代理 2 县代理
     * "proxyCustomerName" : 代理的真实姓名
     * "wxUserIcon": "x", 微信头像
     * "wxName": "AAA" 微信昵称
     * }
     * @author Charlie
     * @date 2018/9/25 16:37
     */
    @RequestMapping( "authentication" )
    public Response authentication(
            @RequestParam( value = "refereeUserId", required = false ) Long refereeUserId,
            @RequestParam( "phone" ) String phone,
            @RequestParam( "openToken" ) String openToken,
            @RequestParam( "openId" ) String openId,
            HttpServletResponse response,
            @RequestParam( "authCode" ) String authCode) {
        try {
            return Response.success (publicAccountUserService.authentication (refereeUserId, phone, authCode, openToken, openId, response));
        } catch (ErrorHelper e) {
            return ResponseHelper.errorHandler (e);
        }
    }


    /**
     * 退出登录
     *
     * @return 是否成功 true:成功
     * @author Charlie
     * @date 2018/9/25 16:37
     */
    @RequestMapping( "logout" )
    public Response logout() {
        PublicAccountUser user = publicAccountUserService.isLogin (request);
        if (user == null) {
            return ResponseHelper.noLogin ();
        }

        Long userId = user.getId ();
        try {
            return Response.success (publicAccountUserService.logout (userId));
        } catch (Exception e) {
            return ResponseHelper.errorHandler (e);
        }
    }

    @RequestMapping( "dele" )
    public void dele(String openId) {
        strRedisTemplate.delete (PubAccountLoginUtils.userCacheKey (openId));

    }


    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param type  1:公众号登录验证码,2:公众号注册代理商
     * @return true 成功,false 失败
     * @author Charlie
     * @date 2018/9/24 10:24
     */
    @RequestMapping( "sendAuthCode" )
    public Response sendAuthCode(
            @RequestParam( "phone" ) String phone,
            @RequestParam( "type" ) Integer type) {
        boolean isSuccess = false;
        if (type == 1) {
            isSuccess = smsService.sendAuthCode (RedisKeyEnum.PUBLIC_ACCOUNT_LOGIN_AUTH_CODE, phone);
        }
        else if (type == 2) {
            isSuccess = smsService.sendAuthCode (RedisKeyEnum.PUBLIC_ACCOUNT_REGISTER_PROXY_AUTH_CODE, phone);
        }
        return Response.success (isSuccess);
    }


    /**
     * 查询用户是否已登录
     *
     * @param request header :  openToken : token
     *                uid :openId
     * @return data:{
     *     isLogin:true //是否登录
     *     phone:13849245522 //
     *     wxName: 昵稱
     *     wxUserIcon: 頭像
     * }
     * @author Charlie
     * @date 2018/9/28 19:57
     */
    @RequestMapping( "isLogin" )
    public Response isLogin(HttpServletRequest request) {
        PublicAccountUser user = publicAccountUserService.isLogin (request);
        boolean isLogin = user != null;
        Map<String, Object> response = new HashMap<> (2);
        response.put ("isLogin", isLogin);
        if (isLogin) {
            response.put ("phone", user.getPhone ());
            response.put ("wxName", user.getWxName ());
            response.put ("wxUserIcon", user.getWxUserIcon ());
        }
        return Response.success (response);
    }


    /**
     * 公众号appid
     *
     * @return data appId
     * @author Charlie
     * @date 2018/9/29 11:39
     */
    @RequestMapping( "appId" )
    public Response appId() {
        return Response.success (appId);
    }


    /**
     * 根据微信code登录
     * <p>
     * code换取openId,以及token
     *
     * </p>
     *
     * @param code 调用微信获取的code
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": {
     *             "uid": "1540206064811",
     *             "token": "publicAccount:user:openId1540206064811",
     *             "isNeedBindPhone": true //是否需要手机号登录
     *             "isSuccess": false //是否登录成功
     *             "isDel": false //账号是否被禁用, true,被禁用
     *             }
     *             }
     * @return
     * @author Charlie
     * @date 2018/10/22 18:46
     */
    @RequestMapping( "loginByWeiXin" )
    public Response loginByWeiXin(String code) {
        try {
            return Response.success (publicAccountUserService.loginByWeiXin (code));
        } catch (ErrorHelper e) {
            return ResponseHelper.errorHandler (e);
        }
    }


    /**
     * 根据用户id查询
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser
     * @author Charlie
     * @date 2018/10/22 19:34
     */
    @RequestMapping( "findByUserId" )
    PublicAccountUser findByUserId(long userId) {
        return publicAccountUserService.findByUserId (userId);
    }


}
