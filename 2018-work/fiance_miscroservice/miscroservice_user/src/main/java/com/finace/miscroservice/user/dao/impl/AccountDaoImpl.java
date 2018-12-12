package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.user.dao.AccountDao;
import com.finace.miscroservice.user.mapper.AccountMapper;
import com.finace.miscroservice.user.po.AccountPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 账户dao层实现类
 */
@Component
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public AccountPO getAccountByUserId(String userId) {
        return accountMapper.getAccountByUserId(userId);
    }

    @Override
    public int updateAccount(AccountPO accountPO) {
        return accountMapper.updateAccount(accountPO);
    }
}
