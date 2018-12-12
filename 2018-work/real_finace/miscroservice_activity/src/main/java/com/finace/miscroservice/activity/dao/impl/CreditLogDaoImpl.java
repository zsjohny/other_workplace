package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.CreditDao;
import com.finace.miscroservice.activity.dao.CreditLogDao;
import com.finace.miscroservice.activity.mapper.CreditLogMapper;
import com.finace.miscroservice.activity.mapper.CreditMapper;
import com.finace.miscroservice.activity.po.CreditLogPO;
import com.finace.miscroservice.activity.po.CreditPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class CreditLogDaoImpl implements CreditLogDao {


    @Autowired
    private CreditLogMapper creditLogMapper;


    @Override
    public int saveCreditLog(CreditLogPO creditLogPO) {
        return creditLogMapper.saveCreditLog(creditLogPO);
    }

    @Override
    public List<CreditLogPO> getCreditLogByUserId(String userId) {
        return creditLogMapper.getCreditLogByUserId(userId);
    }

    @Override
    public Integer getCreditLogSizeByUserId(String userId) {
        return  creditLogMapper.getCreditLogSizeByUserId(userId);
    }
}
