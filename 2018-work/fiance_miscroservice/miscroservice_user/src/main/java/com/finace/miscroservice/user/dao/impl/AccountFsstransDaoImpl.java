package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.user.dao.AccountFsstransDao;
import com.finace.miscroservice.user.mapper.AccountFsstransMapper;
import com.finace.miscroservice.user.po.AccountFsstransPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *生利宝dao层实现类
 */
@Component
public class AccountFsstransDaoImpl implements AccountFsstransDao{

    @Autowired
    private AccountFsstransMapper accountFsstransMapper;

    @Override
    public AccountFsstransPO getFsstransByUserId(String userId) {
        return accountFsstransMapper.getFsstransByUserId(userId);
    }
}
