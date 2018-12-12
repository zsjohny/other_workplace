package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.user.dao.AccountCashDao;
import com.finace.miscroservice.user.mapper.AccountCashMapper;
import com.finace.miscroservice.user.po.AccountCashPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class AccountCashDaoImpl implements AccountCashDao {


    @Autowired
    private AccountCashMapper accountCashMapper;


    @Override
    public AccountCashPO getAccountCashById(int id) {
        return accountCashMapper.getAccountCashById(id);
    }

    @Override
    public int addAccountCash(AccountCashPO accountCashPO) {
       return accountCashMapper.addAccountCash(accountCashPO);
    }

    @Override
    public int updateAccountCash(AccountCashPO accountCashPO) {
        return accountCashMapper.updateAccountCash(accountCashPO);
    }
}
