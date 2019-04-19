package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.meta.logistics.LOLPostageVO;
import com.jiuy.core.service.logistics.LOPostageService;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.logistics.LOPostage;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class LogisticsFacade {
	
	@Autowired
	private LOWarehouseService loWarehouseService;
	
	@Autowired
	private LOPostageService loPostageService;

	public List<LOWarehouse> srchWarehouse(PageQuery pageQuery, String name) {
		return loWarehouseService.srchWarehouse(pageQuery, name, false);
	}

	public int srcWarehouseCount(String name) {
		return loWarehouseService.srcWarehouseCount(name);
	}
	
	public int equalWarehouseCount(String name){
		return loWarehouseService.equalWarehouseCount(name);
	}

	public int updateEqualWarehouseCount(long id, String name) {
		return loWarehouseService.updateEqualWarehouseCount(id,name);
	}
	
	public ResultCode addWarehouse(LOWarehouse warehouse) {
		loWarehouseService.add(warehouse);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public ResultCode updateWarehouse(LOWarehouse warehouse) {
		loWarehouseService.update(warehouse);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public ResultCode removeWarehouse(Long[] ids2) {
		List<Long> ids = new ArrayList<Long>();
		Collections.addAll(ids, ids2);
		
		loWarehouseService.remove(ids);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public List<LOLPostageVO> srchLogistics(int deliveryLocation) {
		return loPostageService.srchLogistics(deliveryLocation);
	}

	public ResultCode savePostage(String postageJson) {
		
		List<LOPostage> list = JSON.parseArray(postageJson, LOPostage.class);
		for (LOPostage loPostage : list) {
			loPostageService.savePostage(loPostage.getId(), loPostage.getPostage());
		}
		return ResultCode.COMMON_SUCCESS;
	}
	
}
