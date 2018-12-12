package com.finace.miscroservice.user.rpc.impl;

import com.finace.miscroservice.commons.entity.UserAuth;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.user.rpc.AuthorizeRpcService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizeRpcServiceImpl implements AuthorizeRpcService {
    private Log log = Log.getInstance(AuthorizeRpcServiceImpl.class);

    @Override
    public Boolean insertUsers(List<UserAuth> users) {
        log.warn("插入用户系统没响应进入回掉");
        return false;
    }

    @Override
    public void updateUser(UserAuth user) {
        log.warn("更新用户={}系统没响应进入回掉", user);
    }

    @Override
    public Boolean checkUserPass(UserAuth user) {
        log.warn("验证用户={}系统没响应进入回掉", user);
        return false;
    }

    @Override
    public UserAuth findUserByName(String userName) {
        log.warn("查询用户={}系统没响应进入回掉", userName);
        return null;
    }
}
