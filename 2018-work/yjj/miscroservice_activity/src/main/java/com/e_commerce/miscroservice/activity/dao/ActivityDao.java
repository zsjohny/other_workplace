package com.e_commerce.miscroservice.activity.dao;


import com.e_commerce.miscroservice.activity.entity.ActivityUser;

/**
 * Create by hyf on 2018/10/8
 */
public interface ActivityDao {

    /**
     * 根据手机号和活动类型查询
     * @param phone
     * @param code
     * @return
     */
    ActivityUser findActivityUser(String phone, Integer code);

    /**
     * 保存活动用户
     * @param user
     */
    void addActivityUser(ActivityUser user);
}
