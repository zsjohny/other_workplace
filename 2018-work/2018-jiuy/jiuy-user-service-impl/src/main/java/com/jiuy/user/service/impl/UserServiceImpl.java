package com.jiuy.user.service.impl;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.user.mapper.OperatorUserMapper;
import com.jiuy.user.mapper.StoreUserMapper;
import com.jiuy.user.mapper.SupplierUserMapper;
import com.jiuy.user.mapper.UserMapper;
import com.jiuy.user.model.OperatorUser;
import com.jiuy.user.model.StoreUser;
import com.jiuy.user.model.SupplierUser;
import com.jiuy.user.model.User;
import com.jiuy.user.servie.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 用户service的实现类
 * @author Aison
 * @version V1.0
 * @date 2018/6/8 21:54
 * @Copyright 玖远网络
 */
@Service("userService")
public class UserServiceImpl implements IUserService {



    private final UserMapper userMapper;
    private final OperatorUserMapper operatorUserMapper;
    private final SupplierUserMapper supplierUserMapper;
    private final StoreUserMapper storeUserMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, OperatorUserMapper operatorUserMapper, SupplierUserMapper supplierUserMapper,StoreUserMapper storeUserMapper) {
        this.userMapper = userMapper;
        this.operatorUserMapper = operatorUserMapper;
        this.supplierUserMapper = supplierUserMapper;
        this.storeUserMapper = storeUserMapper;
    }

    /**
     * 添加用户的接口
     *
     * @param user 用户邓庄
     * @param operatorUser 运营平台用户
     * @param supplierUser  供应商用户
     * @param storeUser 门店用户
     * @param  userSession 操作员
     * @author Aison
     * @date 2018/6/8 21:56
     */
    @Override
    public MyLog<Long> addUser(User user, OperatorUser operatorUser, SupplierUser supplierUser, StoreUser storeUser, UserSession userSession) {

        user.setStatus(1);
        userMapper.insertSelective(user);
        operatorUserMapper.insertSelective(operatorUser);
        return new MyLog<Long>().setData(131L);
    }
}
