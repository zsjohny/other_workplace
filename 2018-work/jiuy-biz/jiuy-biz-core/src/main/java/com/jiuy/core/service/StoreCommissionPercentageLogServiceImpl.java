package com.jiuy.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreCommissionPercentageLogDao;
import com.jiuyuan.entity.StoreCommissionPercentageLog;

@Service
public class StoreCommissionPercentageLogServiceImpl implements StoreCommissionPercentageLogService{
	@Autowired
	private StoreCommissionPercentageLogDao storeCommissionPercentageLogDao;
	
	@Override
	public int insertLog(StoreCommissionPercentageLog storeCommissionPercentageLog) {
		return storeCommissionPercentageLogDao.insertLog(storeCommissionPercentageLog);
	}

}
