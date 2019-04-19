package com.jiuy.core.service.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.OrderNewLogDao;
import com.jiuyuan.entity.OrderNewLog;

@Service
public class OrderNewLogServiceImpl implements OrderNewLogService {

	@Autowired
	private OrderNewLogDao orderNewLogDao;
	
	@Override
	public List<OrderNewLog> orderNewLogOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new ArrayList<OrderNewLog>();
		}
		
		return orderNewLogDao.orderNewLogOfOrderNos(orderNos);
	}

	@Override
	public List<OrderNewLog> orderNewLogPayOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new ArrayList<OrderNewLog>();
		}
		
		return orderNewLogDao.orderNewLogPayOfOrderNos(orderNos);
	}

	@Override
	public List<Map<String, Object>> hotSaleCategory(long startTime, long endTime, int count, long categoryId) {
		return orderNewLogDao.hotSaleCategory(startTime, endTime, count, categoryId);
	}

	@Override
	public List<Map<String, Object>> hotSaleOfActivity(long startTime, long endTime) {
		return orderNewLogDao.hotSaleOfActivity(startTime, endTime);
	}

	@Override
	public Map<String, Object> saleOrderCountPerDay(long startTime, long endTime) {
        List<Map<String, Object>> list = orderNewLogDao.saleOrderCountPerDay(startTime, endTime);

        Map<String, Object> map = new HashMap<String, Object>();
        for (Map<String, Object> subMap : list) {
            map.put((String) subMap.get("day"), subMap.get("count"));
        }

        return map;
	}

	@Override
	public Map<String, Object> saleProductCountPerDay(long startTime, long endTime) {
        List<Map<String, Object>> list = orderNewLogDao.saleProductCountPerDay(startTime, endTime);

        Map<String, Object> map = new HashMap<String, Object>();
        for (Map<String, Object> subMap : list) {
            map.put((String) subMap.get("day"), subMap.get("count"));
        }

        return map;
	}

	@Override
	public Map<String, Object> saleOrderMoneytPerDay(long startTime, long endTime) {
		List<Map<String, Object>> list = orderNewLogDao.saleOrderMoneytPerDay(startTime, endTime);

        Map<String, Object> map = new HashMap<String, Object>();
        for (Map<String, Object> subMap : list) {
            map.put((String) subMap.get("day"), Double.parseDouble(subMap.get("orderMoney").toString()) + Double.parseDouble(subMap.get("expressMoney").toString()));
        }

        return map;
	}

	@Override
	public List<Map<String, Object>> rankProductHotsale(long startTime, long endTime, Collection<Long> seasonIds) {
		return orderNewLogDao.rankProductHotsale(startTime, endTime, seasonIds);
	}

	@Override
	public int getNewUserOrderCountPerDay(long newUserStartTime, long newUserEndTime) {
		return orderNewLogDao.getNewUserOrderCountPerDay(newUserStartTime, newUserEndTime);
	}

	@Override
	public int getNewUserProductCountPerDay(long newUserStartTime, long newUserEndTime) {
	Map<String, BigDecimal> map = orderNewLogDao.getNewUserProductCountPerDay(newUserStartTime, newUserEndTime);
		return map.get("count").intValue();
	}

	/* (non-Javadoc)
	 * @see com.jiuy.core.service.order.OrderNewLogService#productSale(java.util.Collection)
	 */
	@Override
	public Map<Long, Map<String, Object>> productSale(Collection<Long> seasonIds) {

		Map<Long, Map<String, Object>> maps = new HashMap<Long, Map<String, Object>>();
		
		List<Map<String, Object>> list = orderNewLogDao.productsale(seasonIds);
		
		for(Map<String, Object> map : list) {
			maps.put((Long) map.get("ProductId"), map);
		}
		
		return maps;		
	}
	

	/* (non-Javadoc)
	 * @see com.jiuy.core.service.order.OrderNewLogService#productSale(java.util.Collection)
	 */
	@Override
	public Map<Long, Map<String, Object>> productSkuSale(Collection<Long> seasonIds) {
		Map<Long, Map<String, Object>> maps = new HashMap<Long, Map<String, Object>>();
		
		List<Map<String, Object>> list = orderNewLogDao.productSkuSale(seasonIds);
		
		for(Map<String, Object> map : list) {
			maps.put((Long) map.get("SkuId"), map);
		}
		
		return maps;		
	}
}
