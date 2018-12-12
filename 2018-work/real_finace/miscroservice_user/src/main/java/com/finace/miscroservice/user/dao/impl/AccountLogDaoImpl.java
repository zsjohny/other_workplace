package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.user.dao.AccountLogDao;
import com.finace.miscroservice.user.mapper.AccountLogMapper;
import com.finace.miscroservice.user.po.AccountLogPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AccountLogDaoImpl implements AccountLogDao {


    @Autowired
    private AccountLogMapper accountLogMapper;

    @Override
    public List<AccountLogPO> getZjmxByUserId(Map<String, Object> map, int page) {
        BasePage.setPage(page);
        return accountLogMapper.getZjmxByUserId(map);
    }


    @Override
    public int addAccountLog(AccountLogPO accountLogPO) {
        return accountLogMapper.addAccountLog(accountLogPO);
    }

    @Override
    public void addAccountLogSeqNo(AccountLogPO accountLog) {
        accountLogMapper.addAccountLogSeqNo(accountLog);
    }

    @Override
    public void upAccountLogBySeqNo(String seqNo, String code, String remark) {
        accountLogMapper.upAccountLogBySeqNo(seqNo,code,remark);
    }
}
