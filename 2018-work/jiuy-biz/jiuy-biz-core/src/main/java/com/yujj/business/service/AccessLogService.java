package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.entity.visit.AccessLog;
import com.yujj.dao.mapper.AccessLogMapper;

@Service
public class AccessLogService {

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public int addAccessLog(AccessLog accessLog) {
        return accessLogMapper.addAccessLog(accessLog);
    }

}
