package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.logistics.LOLocation;
import com.yujj.dao.mapper.LOLocationMapper;

@Service
public class LOLocationService {
	
	@Autowired
	private LOLocationMapper loLocationMapper;

	public LOLocation getByName(String cityName) {
		return loLocationMapper.getByName(cityName);
	}
}
