package com.e_commerce.miscroservice.crm.service.impl;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.crm.dao.UserDao;
import com.e_commerce.miscroservice.crm.entity.User;
import com.e_commerce.miscroservice.crm.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/18 14:09
 * @Copyright 玖远网络
 */
@Service
public class UserServiceImpl implements UserService {
    private Log logger = Log.getInstance(UserServiceImpl.class);
    @Resource
    private UserDao userDao;


    @Override
    public Response load(String userName, String password, HttpServletRequest request, HttpServletResponse response) {
        logger.info("登陆phone={}，password={}", userName, password);
        User user = userDao.findUserByUserName(userName);
        if (user == null) {
            logger.warn("该用户不存在");
            return Response.error("该用户不存在");
        }
        String md5Pass = Md5Util.md5(password);
        if (!md5Pass.equals(user.getPassword())) {
            logger.warn("userName={}密码错误", userName);
            return Response.error("密码错误");
        }
        response.addHeader("token", String.valueOf(user.getUserId()));
        return Response.success();

    }

    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param name
     * @param request
     * @param response
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response register(String userName, String phone, String password, String name, HttpServletRequest request, HttpServletResponse response) {

        logger.info("注册phone={}，password={}，name={}", phone, password, name);
        String md5Pass = Md5Util.md5(password);
        User user = userDao.findUserByUserName(userName);
        if (user != null) {
            logger.warn("该用户已存在");
            return Response.error("该用户已存在");
        }
        userDao.insertUser(userName, phone, md5Pass, name);
        return Response.success();
    }
}
