package com.e_commerce.miscroservice.publicaccount.service.user;


import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/21 18:23
 * @Copyright 玖远网络
 */
public interface PublicAccountUserService{


    /**
     * 创建一个公众号用户
     *
     * @param user   user
     * @param authCode 短信验证码
     * @param response
     * @param openToken
     * @param openId
     * @param isSubjectAccount
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/24 14:58
     */
    PublicAccountUser loginUserByPhoneIfNullCreate(PublicAccountUser user, String authCode, HttpServletResponse response, String openToken, String openId, boolean isSubjectAccount);

    /**
     * 通过手机号查询
     *
     * @param phone phone
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/24 17:25
     */
    PublicAccountUser findByPhone(String phone);

    PublicAccountUser findByUserId(long userId);


    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/9/24 23:34
     */
    int updateById(PublicAccountUser updInfo);


    /**
     * 通过手机号查询
     *
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/24 17:25
     */
    PageInfo<PublicAccountUserQuery> listUser(PublicAccountUserQuery query);


    /**
     * 手机认证
     *
     * @param refereeUserId 推荐人id
     * @param phone phone
     * @param authCode authCode
     * @param openToken openToken
     * @param openId openId
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/25 14:33
     */
    Map<String, Object> authentication(Long refereeUserId, String phone, String authCode, String openToken, String openId, HttpServletResponse response);
    /**
     * 手机认证
     *
     * @param refereeUserId 推荐人id
     * @param phone phone
     * @param authCode authCode
     * @param openToken openToken
     * @param openId openId
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/25 14:33
     */
    PublicAccountUser doAuthentication(Long refereeUserId, String phone, String authCode, HttpServletResponse response, String openToken, String openId);


    /**
     * 退出登录
     * @param userId userId
     * @return boolean
     * @author Charlie
     * @date 2018/9/28 13:53
     */
    boolean logout(Long userId);



    /**
     * 用户是否登录
     *
     * @param request request
     * @return null 没有登录
     * @author Charlie
     * @date 2018/9/28 19:54
     */
    PublicAccountUser isLogin(HttpServletRequest request);



    /**
     * 用户信息放入缓存.token返回给前端
     *
     * @param user user
     * @param response
     * @author Charlie
     * @date 2018/9/29 11:54
     */
    String putInCacheAndResponse(PublicAccountUser user, HttpServletResponse response);

    PublicAccountUser updateByPhone(PublicAccountUser updateInfo);

    /**
     * 用微信登录
     * @param code 微信认证code
     * @return
     */
    Map<String, Object> loginByWeiXin(String code);


    /**
     * 用户是否登录
     *
     * @param openId openId
     * @param openToken openToken
     * @return null 没有登录
     * @author Charlie
     * @date 2018/9/28 19:54
     */
    PublicAccountUser isLogin(String openId, String openToken);

    /**
     * 描述 禁用代理
     * @param userId
     * @author hyq
     * @date 2018/10/17 11:20
     * @return int
     */
    int stopCustomer(Long userId, Integer type);
}