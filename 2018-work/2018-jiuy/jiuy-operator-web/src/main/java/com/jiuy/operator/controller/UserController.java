package com.jiuy.operator.controller;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.user.enums.UserType;
import com.jiuy.user.model.OperatorUser;
import com.jiuy.user.model.StoreUser;
import com.jiuy.user.model.SupplierUser;
import com.jiuy.user.model.User;
import com.jiuy.user.servie.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户相关的controller
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/8 16:10
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/operator/admin")
public class UserController {



    @Resource(name = "userService")
    private IUserService userService;

    /**
     * 添加后台用户
     * @param user 主表
     * @param operatorUser 后台用户表
     * @author Aison
     * @date 2018/6/8 18:18
     */
    @RequestMapping("/addOptUser")
    public ResponseResult addOptUser(User user, OperatorUser operatorUser) {

        user.setUserType(UserType.OPERATOR_USER.getCode());
        userService.addUser(user,operatorUser,null,null,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }

    /**
     * 添加供应商用户
     * @param user 主表
     * @param supplierUser 供应商用户
     * @author Aison
     * @date 2018/6/8 18:18
     */
    @RequestMapping("/addSupplierUser")
    public ResponseResult addSupplier(User user, SupplierUser supplierUser) {

        user.setUserType(UserType.OPERATOR_USER.getCode());
        userService.addUser(user,null,supplierUser,null,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }
}
