package com.jiuy.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.SupplierFinanceLogDao;
import com.jiuyuan.entity.newentity.FinanceLogNew;

@Service
public class SupplierFinanceLogServiceImpl implements SupplierFinanceLogService {
    
	@Autowired
	private SupplierFinanceLogDao supplierFinanceLogDao;
	
	@Override
	public int addSupplierFinanceLog(FinanceLogNew supplierFinanceLog) {
		return supplierFinanceLogDao.addSupplierFinanceLog(supplierFinanceLog);
	}

}
