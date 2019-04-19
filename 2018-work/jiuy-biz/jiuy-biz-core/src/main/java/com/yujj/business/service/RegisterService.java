package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.account.MailRegister;
import com.yujj.dao.mapper.MailRegisterMapper;

@Service
public class RegisterService {
    
    @Autowired
    private MailRegisterMapper mailRegisterMapper;

    public MailRegister getMailRegister(String userName, long time) {
        return mailRegisterMapper.getMailRegister(userName, time);
    }

    public MailRegister getMailRegister(String registerUuid) {
        return mailRegisterMapper.getMailRegisterByUuid(registerUuid);
    }

    public int addMailRegister(MailRegister mailRegister) {
        return mailRegisterMapper.addMailRegister(mailRegister);
    }

}
