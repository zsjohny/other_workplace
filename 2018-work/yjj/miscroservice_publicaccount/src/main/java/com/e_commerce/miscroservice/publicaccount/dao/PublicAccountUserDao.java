package com.e_commerce.miscroservice.publicaccount.dao;


import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/24 14:57
 * @Copyright 玖远网络
 */
public interface PublicAccountUserDao{


    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/9/24 23:34
     */
    int updateByPrimaryKeySelective(PublicAccountUser updInfo);


    /**
     * 根据id查询账号
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser
     * @author Charlie
     * @date 2018/9/28 14:00
     */
    PublicAccountUser findById(Long userId);

    List<PublicAccountUserQuery> listUser(PublicAccountUserQuery query);



    /**
     * 根据openId查找用户,按登录时间limit one
     *
     * @param openId openId
     * @return com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser
     * @author Charlie
     * @date 2018/10/22 17:23
     */
    PublicAccountUser findSubjectByOpenId(String openId);



    /**
     * 关闭主体账号
     *
     * @param openId openId
     * @author Charlie
     * @date 2018/10/24 20:08
     */
    void closeSubjectAccountByOpenId(String openId);


    /**
     * 根据手机号将账号设为主体账号
     *
     * @param phone phone
     * @return int
     * @author Charlie
     * @date 2018/10/24 20:50
     */
    int openSubjectAccountByPhone(String phone);
}
