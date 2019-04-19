package com.jiuy.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreExpressInfoDao;
import com.jiuyuan.entity.newentity.StoreExpressInfo;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月8日 下午8:20:25
*/
@Service
public class StoreExpressInfoServiceImpl implements StoreExpressInfoService{

	@Resource
	private StoreExpressInfoDao storeExpressInfoDao;
	
	@Override
	public Map<Long, StoreExpressInfo> expressInfoMapOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new HashMap<Long, StoreExpressInfo>();
		}
		
		List<StoreExpressInfo> storeExpressInfos =  storeExpressInfoDao.expressInfoOfOrderNos(orderNos);
		Map<Long, StoreExpressInfo> map = new HashMap<Long, StoreExpressInfo>();
		for(StoreExpressInfo storeExpressInfo : storeExpressInfos){
			map.put(storeExpressInfo.getOrderNo(), storeExpressInfo);
		}
		return map;
	}

	@Override
	public int remove(Collection<Long> orderNos) {
		if(orderNos.size() < 1){
			return 0;
		}
		return storeExpressInfoDao.remove(orderNos);
	}

	@Override
	public int addItem(StoreExpressInfo storeExpressInfo) {
		return storeExpressInfoDao.addItem(storeExpressInfo);
	}

}
