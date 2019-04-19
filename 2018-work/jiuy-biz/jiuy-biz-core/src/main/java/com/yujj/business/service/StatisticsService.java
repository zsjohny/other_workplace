package com.yujj.business.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.dao.mapper.StatisticsMapper;
import com.yujj.util.asyn.annotation.AsynExecutable;

@Service
public class StatisticsService {
	
	@Autowired
	private StatisticsMapper statisticsMapper;
	
	@AsynExecutable
	public int updateUserClick(Long id) {
		return statisticsMapper.updateUserClick(id);
	}
	@AsynExecutable
	public int updateUnKnownClick(Long id) {
		return statisticsMapper.updateUnKnownClick(id);
	}
	
	public int batchUpdateOrderCount(Collection<Long> ids) {
		return statisticsMapper.batchUpdateOrderCount(ids);
	}
}
