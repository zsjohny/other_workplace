package com.finace.miscroservice.user.service.impl;

import com.finace.miscroservice.user.dao.AccountFsstransDao;
import com.finace.miscroservice.user.po.AccountFsstransPO;
import com.finace.miscroservice.user.service.AccountFsstransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 生利宝service实现类
 */
@Service
public class AccountFsstransServiceImpl implements AccountFsstransService {


    @Autowired
    private AccountFsstransDao accountFsstransDao;


    @Override
    public AccountFsstransPO getFsstransByUserId(String userId) {
        return accountFsstransDao.getFsstransByUserId(userId);
    }
}
