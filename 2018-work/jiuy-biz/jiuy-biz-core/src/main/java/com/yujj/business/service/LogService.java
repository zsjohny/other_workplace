package com.yujj.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jiuyuan.entity.log.Log;
import com.jiuyuan.entity.log.RelatedOrderLog;
import com.jiuyuan.entity.log.UserLog;
import com.yujj.dao.mapper.LogMapper;

@Service
public class LogService {
	
	@Autowired
	private LogMapper logMapper;

	public int addLogs(String logs) {
		
		long time = System.currentTimeMillis();
		List<Log> logList = new ArrayList<Log>();
		
		logList = JSONArray.parseArray(logs, Log.class);
				
		return logMapper.addLogs(time, logList);		
	}
	
	public int addRelatedOrderLogs(String logs) {
		
		long time = System.currentTimeMillis();
		List<RelatedOrderLog> logList = new ArrayList<RelatedOrderLog>();
		
		logList = JSONArray.parseArray(logs, RelatedOrderLog.class);
		
		return logMapper.addRelatedOrderLogs(time, logList);		
	}	
	
	public List<Log> getProductLogs(long productId) {
		
		
		return logMapper.getProductLogs(productId);		
	}	
	
	public List<Log> getAllProductLogs(long time) {
		
		
		return logMapper.getAllProductLogs(time);		
	}	
	
	@Transactional(rollbackFor=Exception.class)
	public int addUserStartLog(String logs) {
		
		long time = System.currentTimeMillis();
			
		UserLog log = JSONArray.parseObject(logs, UserLog.class);
		log.setCreateTime(time);
		log.setUpdateTime(time);

		logMapper.addUserStartLog(log);
		
		//userId cid 组合key存在即更新		
		return logMapper.addUserLog(log);
	}
}