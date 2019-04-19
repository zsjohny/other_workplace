package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.OrderItemDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuyuan.entity.query.PageQuery;

public class OrderItemDaoSqlImpl extends DomainDaoSqlSupport<OrderItem, Long>implements OrderItemDao {

    @Override
    public List<OrderItem> getByOrderId(long orderId) {
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.getByOrderId", orderId);
    }

    @Override
    public List<OrderItem> getOrderItemGroup(long orderId, long brandId) {
        Map<String, Object> param = new HashMap<>();

        param.put("orderId", orderId);
        param.put("brandId", brandId);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.getOrderItemGroup", param);
    }

	@Override
	public List<OrderItem> getOrderItems(Collection<Long> orderIds) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderIds", orderIds);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.getOrderItems", params);
	}

	@Override
	public List<OrderItem> orderItemsOfGroupIds(Collection<Long> orderGroupIds) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderGroupIds", orderGroupIds);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfGroupIds", params);
	}

	@Override
	public List<OrderItem> orderItemsOfIds(Collection<Long> orderItemIds) {
		Map<String, Object> params = new HashMap<>();

        params.put("Ids", orderItemIds);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfIds", params);
	}

	@Override
	public OrderItem orderItemsOfId(long orderItemId) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderItemId", orderItemId);

        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfId", params);
	}

	@Override
	public List<OrderItem> orderItemsOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderNos", orderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfOrderNos", params);
	}
	@Override
	public List<OrderItem> orderItemsOfOrderNo(Long orderNo) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderNo", orderNo);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfOrderNo", params);
	}
	@Override
	public List<Map<String, Object>> buyCountMapOfOrderNo(Collection<Long> allOrderNos) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderNos", allOrderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.buyCountMapOfOrderNo", params);
	}

    @Override
    public List<OrderItem> orderItemsOfSkuIds(Collection<Long> skuIds, Collection<Long> orderNos) {
        Map<String, Object> params = new HashMap<>();

        params.put("skuIds", skuIds);
        params.put("orderNos", orderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfSkuIds", params);
    }

	@Override
	public List<Map<String, Object>> srchSelfParamsOfOrderNos(Collection<Long> orderNos) {
        Map<String, Object> params = new HashMap<>();

        params.put("orderNos", orderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.srchSelfParamsOfOrderNos", params);
	}

	@Override
	public int getBuyCountOfOrderStatus(long userId, Collection<Integer> orderStatus) {
        Map<String, Object> params = new HashMap<>();

        params.put("userId", userId);
        params.put("groupOrderStatus", orderStatus);
        Integer i = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.getBuyCountOfOrderStatus", params);
        if (i == null) {
			return 0;
		} else {
			return i;
		}
	}

    @Override
    public List<OrderItem> orderItemsOfOrderId(Collection<Long> orderIds) {
        Map<String, Object> params = new HashMap<>();

        params.put("orderIds", orderIds);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfOrderId", params);
    }

    @Override
    public List<Map<String, Object>> getExpectedSaleCount() {
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.getExpectedSaleCount");
    }

	@Override
	public List<OrderItem> orderItemsOfStatisticsId(Collection<Long> statisticsIds, Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();

        params.put("statisticsIds", statisticsIds);
        params.put("orderNos", orderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfStatisticsIds", params);
	}

	@Override
	public List<Map<String, Object>> getMonthSales(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.getMonthSales", params);
	}

	@Override
	public List<Map<String, Object>> getPerdaySalesVolume(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.getPerdaySalesVolume", params);
	}

	@Override
	public List<Map<String, Object>> getPerdaySalesAmount(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.getPerdaySalesAmount", params);
	}

	@Override
	public int searchOfOrderNos(List<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", orderNos);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.searchOfOrderNos", params);
	}

	@Override
	public List<OrderItem> orderItemsOfProductIds(Collection<Long> productIds) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("productIds", productIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.orderItemsOfProductIds", params);
	}

//	@Override
//	public OrderItem searchOfOrderNo(long orderNo) {
//		Map<String, Object> params = new HashMap<>();
//		
//		params.put("orderNo", orderNo);
//		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.searchOfOrderNo", params);
//	}
	
	

	@Override
	public Map<Long, Integer> getProductCountMap(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();

        params.put("orderNos", orderNos);

        return getSqlSession().selectMap("com.jiuy.core.dao.impl.sql.StoreOrderItemDaoSqlImpl.getProductCountMap", params, "orderNo");
	}

	@Override
	public List<Map<String, Object>> searchDeductDetailRecord(PageQuery pageQuery,Collection<Long> productIds, Collection<Long> userIds,
			long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("pageQuery", pageQuery);
		params.put("productIds", productIds);
		params.put("userIds", userIds);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.searchDeductDetailRecord", params);
	}

	@Override
	public int searchDeductDetailRecordCount(Collection<Long> productIds, Collection<Long> userIds, long startTime,
			long endTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("productIds", productIds);
		params.put("userIds", userIds);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderItemDaoSqlImpl.searchDeductDetailRecordCount", params);
	}
}
