package com.jiuyuan.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.dao.mapper.supplier.FinanceLogNewMapper;
import com.jiuyuan.entity.newentity.FinanceLogNew;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class FinanceLogNewService {
	private static final Log logger = LogFactory.get("SupplierFinanceLogService");
	@Autowired
	private FinanceLogNewMapper supplierFinanceLogMapper;

	public int addSupplierFinanceLog(FinanceLogNew supplierFinanceLog) {
		return supplierFinanceLogMapper.insert(supplierFinanceLog);
	}
    
}
