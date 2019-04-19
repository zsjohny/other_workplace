package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.ActivityPlace;
import com.yujj.dao.mapper.ActivityPlaceMapper;

@Service
public class ActivityPlaceService {
	@Autowired
	private ActivityPlaceMapper activityPlaceMapper;

	public ActivityPlace getById(long activityPlaceId) {
		return activityPlaceMapper.getById(activityPlaceId);
	}
	
	
}
