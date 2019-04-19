package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.logistics.LOWarehouse;
import com.yujj.dao.mapper.YJJLOWarehouseMapper;

@Service
public class LOWarehouseService {

	@Autowired
	YJJLOWarehouseMapper loWarehouseMapper;
	
	public LOWarehouse getById(long id) {
		return loWarehouseMapper.getById(id);
	}

}
