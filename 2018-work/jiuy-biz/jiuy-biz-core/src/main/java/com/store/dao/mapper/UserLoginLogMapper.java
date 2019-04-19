package com.store.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.entity.account.UserLoginLog;


public interface UserLoginLogMapper {

    int addUserLoginLog(UserLoginLog userLoginLog);
    
    UserLoginLog getUserNewestLoginLog(@Param(value = "businessNumber") String businessNumber);

}
