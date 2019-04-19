package com.e_commerce.miscroservice.crm.controller;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户
 *
 * @author hyf
 * @version V1.0
 * @date 2018/9/18 14:03
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登陆
     *
     * @param userName phone
     * @param password password
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyf
     * @date 2018/9/18 14:12
     */
    @RequestMapping("load")
    public Response load(String userName, String password, HttpServletRequest request, HttpServletResponse response) {

        return userService.load(userName, password, request, response);
    }

    /**
     * 注册
     *
     * @param phone    phone
     * @param password password
     * @param name     name
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyf
     * @date 2018/9/18 14:11
     */
    @RequestMapping("register")
    public Response register(String userName, String phone, String password, String name, HttpServletRequest request, HttpServletResponse response) {
        return userService.register(userName, phone, password, name, request, response);
    }
}
