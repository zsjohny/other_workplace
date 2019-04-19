package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.dao.mapper.ExpressSupplierMapper;
import com.yujj.entity.ExpressSupplier;

@Service
public class ExpressSupplierService {

	@Autowired
	private ExpressSupplierMapper expressSupplierMapper;
	
    public ExpressSupplier getExpressSupplierByEngName(String name) {
        return expressSupplierMapper.getExpressSupplierByEngName(name);
	}

    public List<ExpressSupplier> getExpressSupplierList(int status) {
    	return expressSupplierMapper.getExpressSupplierList(status);
    }

}
