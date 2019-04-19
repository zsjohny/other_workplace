package com.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.store.dao.mapper.ExpressSupplierMapper;

@Service
public class ExpressSupplierService {

	@Autowired
	private ExpressSupplierMapper expressSupplierMapper;
	
    public ExpressSupplier getExpressSupplierByEngName(String name) {
        return expressSupplierMapper.getExpressSupplierByEngName(name);
	}

}