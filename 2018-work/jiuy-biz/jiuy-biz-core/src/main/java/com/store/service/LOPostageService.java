package com.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.logistics.LOPostage;
import com.store.dao.mapper.LOPostageMapper;

@Service
public class LOPostageService {

	@Autowired
	private LOPostageMapper loPostageMapper;

	public LOPostage getPostage(int deliveryLocation, int distributionLocationId) {
		return loPostageMapper.getPostage(deliveryLocation, distributionLocationId);
	}
}
