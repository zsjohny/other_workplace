package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.SginDao;
import com.finace.miscroservice.activity.mapper.SginMapper;
import com.finace.miscroservice.activity.po.SginPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 签到实现类
 */
@Component
public class SginDaoImpl implements SginDao{

    @Autowired
    private SginMapper sginMapper;


    @Override
    public Integer getNowSginByUser(String userId) {
        return sginMapper.getNowSginByUser(userId);
    }


    @Override
    public SginPO getSginByUser(String userId) {
        return sginMapper.getSginByUser(userId);
    }

    @Override
    public int saveSgin(SginPO sginPO) {
        return sginMapper.saveSgin(sginPO);
    }

    @Override
    public int updateSgin(SginPO sginPO) {
        return sginMapper.updateSgin(sginPO);
    }
}
