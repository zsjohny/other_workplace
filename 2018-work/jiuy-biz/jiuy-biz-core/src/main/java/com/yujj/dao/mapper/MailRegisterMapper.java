package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.account.MailRegister;

@DBMaster
public interface MailRegisterMapper {

    MailRegister getMailRegister(@Param("userName") String userName, @Param("time") long time);

    MailRegister getMailRegisterByUuid(String registerUuid);

    int addMailRegister(MailRegister mailRegister);

}
