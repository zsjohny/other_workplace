package com.yujj.business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.dao.mapper.FetchCouponCenterLogMapper;

@Service
public class FetchCouponCenterLogService {

	@Autowired
	private FetchCouponCenterLogMapper fetchCouponCenterLogMapper;

	public Map<Long, Integer> getFetchCount(long userId, Long startTime, Long endTime) {
		List<Map<String, Object>> list = fetchCouponCenterLogMapper.getFetchCount(userId, startTime, endTime); 	
		Map<Long, Integer> countByCptId = new HashMap<>();
		for (Map<String, Object> map : list) {
			countByCptId.put(Long.parseLong(map.get("CouponTemplateId").toString()), Integer.parseInt(map.get("count").toString()));
		}
		
		return countByCptId;
	}
	
	
}
