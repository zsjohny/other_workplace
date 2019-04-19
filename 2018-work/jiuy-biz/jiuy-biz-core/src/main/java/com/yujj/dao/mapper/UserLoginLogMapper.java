package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.account.UserLoginLog;

@DBMaster
public interface UserLoginLogMapper {

    int addUserLoginLog(UserLoginLog userLoginLog);
    
    UserLoginLog getUserNewestLoginLog(@Param(value = "userName") String userName);

}
