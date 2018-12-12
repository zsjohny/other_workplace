package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.CreditDao;
import com.finace.miscroservice.activity.dao.CreditLogDao;
import com.finace.miscroservice.activity.mapper.CreditMapper;
import com.finace.miscroservice.activity.po.CreditPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * user Dao层实现类
 */
@Component
public class CreditDaoImpl implements CreditDao {

    @Autowired
    private CreditMapper creditMapper;


    @Override
    public int saveCredit(CreditPO creditPO) {
        return creditMapper.saveCredit(creditPO);
    }


    @Override
    public CreditPO getCreditByUserId(String userId) {
        return creditMapper.getCreditByUserId(userId);
    }

    @Override
    public int updateCreditAddByUserId(String userId, String val) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("value", val);
        return creditMapper.updateCreditAddByUserId(map);
    }



}
