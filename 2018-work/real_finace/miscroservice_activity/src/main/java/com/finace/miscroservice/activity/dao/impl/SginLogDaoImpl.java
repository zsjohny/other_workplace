package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.SginLogDao;
import com.finace.miscroservice.activity.mapper.SginLogMapper;
import com.finace.miscroservice.activity.po.SginLogPO;
import com.finace.miscroservice.commons.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 签到日志dao层实现类
 */
@Component
public class SginLogDaoImpl implements SginLogDao {

    @Autowired
    private SginLogMapper sginLogMapper;


    @Override
    public int saveSginLog(SginLogPO sginLogPO) {
        return sginLogMapper.saveSginLog(sginLogPO);
    }

    @Override
    public List<String> getSginLogMonthByUser(String userId) {
        return sginLogMapper.getSginLogMonthByUser(userId);
    }


}
