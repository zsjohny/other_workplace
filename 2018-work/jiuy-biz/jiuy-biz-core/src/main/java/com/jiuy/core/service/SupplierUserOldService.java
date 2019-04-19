package com.jiuy.core.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.SupplierUserDao;

@Service
public class SupplierUserOldService {
	@Autowired
	private SupplierUserDao supplierUserDao;

	public int handleSupplierAvailableBalance(long supplierId, BigDecimal availableBalance) {
		return supplierUserDao.handleSupplierAvailableBalance(supplierId,availableBalance);
	}

	public BigDecimal getAvailableBalanceById(long supplierId) {
		return supplierUserDao.getAvailableBalanceById(supplierId);
		
	}
     
}
