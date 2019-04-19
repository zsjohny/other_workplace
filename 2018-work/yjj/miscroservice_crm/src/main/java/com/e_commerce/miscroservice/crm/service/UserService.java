package com.e_commerce.miscroservice.crm.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/18 14:09
 * @Copyright 玖远网络
 */
public interface UserService {

    /**
     * 登陆
     *
     * @param phone
     * @param password
     * @return
     */
    Response load(String userName, String password, HttpServletRequest request, HttpServletResponse response);

    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param name
     * @return
     */
    Response register(String userName, String phone, String password, String name, HttpServletRequest request, HttpServletResponse response);
}
