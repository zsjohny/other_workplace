package com.e_commerce.miscroservice.crm.dao.impl;

import com.e_commerce.miscroservice.crm.dao.UserDao;
import com.e_commerce.miscroservice.crm.entity.User;
import com.e_commerce.miscroservice.crm.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/18 14:14
 * @Copyright 玖远网络
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Resource
    private UserMapper userMapper;
    @Override
    public User findUserByPhone(String phone) {
        User user = userMapper.findUserByPhone(phone);
        return user;
    }

    @Override
    public void insertUser(String userName,String phone, String doubleMd5Pass, String name) {
        userMapper.insertUser(userName,phone,doubleMd5Pass,name);
    }

    @Override
    public User findUserByName(String allotName) {
        return userMapper.findUserByName(allotName);
    }

    @Override
    public User findUserByUserId(Long userId) {
        return userMapper.findUserByUserId(userId);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userMapper.findUserByUserName(userName);
    }
}
