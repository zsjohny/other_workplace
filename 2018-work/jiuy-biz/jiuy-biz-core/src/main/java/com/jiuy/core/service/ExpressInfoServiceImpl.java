package com.jiuy.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.ExpressInfoDao;
import com.jiuyuan.entity.ExpressInfo;

@Service
public class ExpressInfoServiceImpl implements ExpressInfoService{
	
	@Autowired
	private ExpressInfoDao expressInfoDao;

	@Override
	public Map<Long, ExpressInfo> expressInfoMapOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new HashMap<Long, ExpressInfo>();
		}
		
		List<ExpressInfo> expressInfos = expressInfoDao.expressInfoOfOrderNos(orderNos);
		Map<Long, ExpressInfo> map = new HashMap<Long, ExpressInfo>();
		for(ExpressInfo expressInfo : expressInfos) {
			map.put(expressInfo.getOrderNo(), expressInfo);
		}
		
		return map;
	}

	@Override
	public int remove(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return 0;
		}
		return expressInfoDao.remove(orderNos);
	}
}
