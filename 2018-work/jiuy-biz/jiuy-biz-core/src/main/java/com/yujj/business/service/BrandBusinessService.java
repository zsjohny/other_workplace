package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.BrandBusiness;
import com.yujj.dao.mapper.BrandBusinessMapper;



/**
 * @author jeff.zhan
 * @version 2016年11月3日 上午10:41:26
 * 
 */

@Service
public class BrandBusinessService {
	
	@Autowired
	private BrandBusinessMapper brandBusinessMapper;

    

	public BrandBusiness getById(long id) {
		long time = System.currentTimeMillis();
		return brandBusinessMapper.getById(id);
	}
	
	public BrandBusiness getByBrandId(long id) {
		long time = System.currentTimeMillis();
		return brandBusinessMapper.getByBrandId(id);
	}

}
