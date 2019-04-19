package com.jiuyuan.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.dao.mapper.supplier.ShopExpressSupplierMapper;
import com.yujj.entity.ExpressSupplier;

@Service
public class ShopExpressSupplierService {

	@Autowired
	private ShopExpressSupplierMapper expressSupplierMapper;
	
    public ExpressSupplier getExpressSupplierByEngName(String name) {
        return expressSupplierMapper.getExpressSupplierByEngName(name);
	}

    public List<ExpressSupplier> getExpressSupplierList(int status) {
    	return expressSupplierMapper.getExpressSupplierList(status);
    }

}
