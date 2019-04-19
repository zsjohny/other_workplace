package com.yujj.business.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.dao.mapper.ExpressInfoMapper;
import com.yujj.entity.order.ExpressInfo;

@Service
public class ExpressInfoService {
    
    @Autowired
    private ExpressInfoMapper expressInfoMapper;

    public Map<Long, ExpressInfo> getExpressInfoMap(Collection<Long> orderItemGroupIds) {
        return expressInfoMapper.getExpressInfoMap(orderItemGroupIds);
    }

    public ExpressInfo getUserExpressInfo(long userId, long orderItemGroupId) {
        return expressInfoMapper.getUserExpressInfo(userId, orderItemGroupId);
    }
    public ExpressInfo getUserExpressInfoByOrderNo(long userId, long orderNo) {
    	return expressInfoMapper.getUserExpressInfoByOrderNo(userId, orderNo);
    }
}
