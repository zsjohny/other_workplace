package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.HongbaoLogDao;
import com.finace.miscroservice.activity.mapper.HongbaoLogMapper;
import com.finace.miscroservice.activity.po.HongbaoLogPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 */
@Component
public class HongbaoLogDaoImpl implements HongbaoLogDao {


    @Autowired
    private HongbaoLogMapper hongbaoLogMapper;

    @Override
    public HongbaoLogPO getHongbaoLogByUserId(Map<String, Object> map) {
        return hongbaoLogMapper.getHongbaoLogByUserId(map);
    }


    @Override
    public void addHongbaoLog(HongbaoLogPO hongbaoLogPO) {
        hongbaoLogMapper.addHongbaoLog(hongbaoLogPO);
    }

    @Override
    public void updateHongbaoLog(HongbaoLogPO hongbaoLogPO) {
        hongbaoLogMapper.updateHongbaoLog(hongbaoLogPO);
    }





    
}
