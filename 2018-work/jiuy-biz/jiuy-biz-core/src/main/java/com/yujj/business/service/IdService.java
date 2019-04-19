package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yujj.dao.mapper.IdMapper;

@Service
public class IdService {

    @Autowired
    private IdMapper idMapper;

    @Transactional(rollbackFor = Exception.class)
    synchronized public long getLastLogId(int count) {
        long logId = idMapper.getLastLogId();
    	idMapper.updateLastLogId(count);
        return logId;
    }
}
