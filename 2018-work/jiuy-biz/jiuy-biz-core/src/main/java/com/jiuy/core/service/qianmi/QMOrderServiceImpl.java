package com.jiuy.core.service.qianmi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.QMOrderDao;
import com.jiuyuan.entity.qianmi.QMOrder;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Service
public class QMOrderServiceImpl implements QMOrderService {

	@Autowired
	private QMOrderDao qMOrderDao;
	
	@Override
	public Map<String, List<QMOrder>> getOrdersOfBuyers(Integer orderStatus, Long mergedId) {
		List<QMOrder> qmOrders = qMOrderDao.search(orderStatus, mergedId, " order By BuyerNick asc");

		String lastBuyerNick = "none";
		List<QMOrder> list = null;
		Map<String, List<QMOrder>> qmMap = new HashMap<>(); 
		for (QMOrder qmOrder : qmOrders) {
			String buyerNick = qmOrder.getBuyerNick();
			if (!StringUtils.equals(lastBuyerNick, buyerNick)) {
				lastBuyerNick = buyerNick;
				list = new ArrayList<>();
				qmMap.put(lastBuyerNick, list);
			}
			list.add(qmOrder);
		}
		return qmMap;
	}

	@Override
	public List<QMOrder> search(Collection<Long> mergedIds) {
		if (mergedIds != null && mergedIds.size() < 1) {
			return new ArrayList<>();
		}
		return qMOrderDao.search(mergedIds, null);
	}

	@Override
	public Map<Long, QMOrder> qMOrderOfNos(Collection<Long> orderNos) {
		if (orderNos != null && orderNos.size() < 1) {
			return new HashMap<>();
		}
		
		Map<Long, QMOrder> map = new HashMap<>();
		List<QMOrder> qmOrders = qMOrderDao.search(null, orderNos);
		for (QMOrder qmOrder : qmOrders) {
			map.put(qmOrder.getOrderNo(), qmOrder);
		}
		
		return map;
	}

}
