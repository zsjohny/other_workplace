package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreOrderLogDao;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.storeorder.StoreOrderLog1;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午9:19:12
*/
@Service
public class StoreOrderLogServiceImpl implements StoreOrderLogService {
	
	@Resource
	private StoreOrderLogDao storeOrderLogDao;

	@Override
	public List<StoreOrderLog1> storeOrderLogOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new ArrayList<StoreOrderLog1>();
		}
		return storeOrderLogDao.storeOrderLogOfOrderNos(orderNos);
	}

	@Override
	public List<StoreOrderLog1> storeOrderLogPayOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new ArrayList<StoreOrderLog1>();
		}
		
		return storeOrderLogDao.storeOrderLogPayOfOrderNos(orderNos);
	}

	@Override
	public List<StoreOrderLog1> getByOrderNos(List<Long> list, OrderStatus deliver, OrderStatus success) {
		
		return storeOrderLogDao.getByOrderNos(list,deliver,success);
	}
	
	
}
