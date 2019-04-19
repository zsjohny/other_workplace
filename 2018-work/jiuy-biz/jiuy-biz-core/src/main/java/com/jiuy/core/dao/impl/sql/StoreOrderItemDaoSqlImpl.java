package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreOrderItemDao;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuyuan.entity.storeorder.StoreOrderItem;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午7:31:06
*/
@Repository
public class StoreOrderItemDaoSqlImpl implements StoreOrderItemDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<StoreOrderItem> orderItemsOfSkuIds(Collection<Long> skuIds, Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();

        params.put("skuIds", skuIds);
        params.put("orderNos", orderNos);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.orderItemsOfSkuIds", params);
	}

	@Override
	public List<Map<String, Object>> buyCountMapOfOrderNo(Collection<Long> allOrderNos) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderNos", allOrderNos);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.buyCountMapOfOrderNo", params);
	}

	@Override
	public List<StoreOrderItem> orderItemsOfIds(Set<Long> orderItemIds) {
		Map<String, Object> params = new HashMap<>();

        params.put("Ids", orderItemIds);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.orderItemsOfIds", params);
	}

	@Override
	public List<Map<String, Object>> srchSelfParamsOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderNos", orderNos);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.srchSelfParamsOfOrderNos", params);
	}

	@Override
	public int getBuyCountOfOrderStatus(long storeId, Collection<Integer> orderStatus) {
		Map<String, Object> params = new HashMap<>();

        params.put("storeId", storeId);
        params.put("groupOrderStatus", orderStatus);
        Integer i = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.getBuyCountOfOrderStatus", params);
        if (i == null) {
			return 0;
		} else {
			return i;
		}
	}

	@Override
	public List<StoreOrderItem> orderItemsOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.orderItemsOfOrderNos", params);
	}

	@Override
	public List<StoreOrderItem> orderItemsOfIds(List<Long> createList) {
		Map<String, Object> params = new HashMap<>();

        params.put("Ids", createList);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.orderItemsOfIds", params);
	}

	@Override
	public List<StoreOrderItem> getByOrderNo(long orderNo) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderNo", orderNo);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.getByOrderNo", params);
	}

	@Override
	public Map<Long, Integer> getProductCountMap(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderNos", orderNos);

        return sqlSessionTemplate.selectMap("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.getProductCountMap", params, "orderNo");
	}

	@Override
	public List<StoreOrderItem> orderItemsOfProductIds(Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<>();

        params.put("productIds", productIds);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.orderItemsOfProductIds", params);
	}

	@Override
	public List<StoreOrderItem> getOrderItemsBySkuIds(Set<Long> skuIds) {
		Map<String, Object> params = new HashMap<>();

        params.put("skuIds", skuIds);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.getOrderItemsBySkuIds", params);
	}

}
