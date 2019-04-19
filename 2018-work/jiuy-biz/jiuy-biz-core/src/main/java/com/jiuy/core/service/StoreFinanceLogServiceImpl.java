package com.jiuy.core.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreFinanceLogDao;
import com.jiuyuan.entity.StoreFinanceLog;

@Service
public class StoreFinanceLogServiceImpl implements StoreFinanceLogService{
	
	@Resource
	private StoreFinanceLogDao storeFianceLogDao;

	@Override
	public void addFinanceLogon(StoreFinanceLog storeFinanceLog) {
		storeFianceLogDao.addFinanceLogon(storeFinanceLog);
	}

	@Override
	public int updateStoreFinanceLog(StoreFinanceLog storeFinanceLog) {
		return storeFianceLogDao.updateStoreFinanceLog(storeFinanceLog);
	}

}
