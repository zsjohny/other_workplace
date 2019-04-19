package com.e_commerce.miscroservice.crm.dao;

import com.e_commerce.miscroservice.crm.entity.User;

/**
 * Create by hyf on 2018/9/18
 */
public interface UserDao {

    /**
     * 根据用户名查找
     * @param phone
     * @return
     */
    User findUserByPhone(String phone);


    /**
     * 注册
     * @param phone
     * @param doubleMd5Pass
     * @param name
     */
    void insertUser(String userName,String phone, String doubleMd5Pass, String name);

    /**
     * 根据姓名查询
     * @return
     * @author hyf
     * @date 2018/9/18 15:03
     */
    User findUserByName(String allotName);

    /**
     * 根据用户id 查询
     * @param userId
     * @return
     */
    User findUserByUserId(Long userId);

    /**
     * 根据用户名查找
     * @param userName
     * @return
     */
    User findUserByUserName(String userName);
}
