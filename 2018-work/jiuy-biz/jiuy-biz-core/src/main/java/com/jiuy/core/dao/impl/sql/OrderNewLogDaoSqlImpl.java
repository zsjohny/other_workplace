package com.jiuy.core.dao.impl.sql;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.OrderNewLogDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.OrderNewLog;

public class OrderNewLogDaoSqlImpl extends SqlSupport implements OrderNewLogDao {

	@Override
	public List<OrderNewLog> orderNewLogOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", orderNos);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.orderNewLogOfOrderNos", params);
	}

	@Override
	public List<OrderNewLog> orderNewLogPayOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", orderNos);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.orderNewLogPayOfOrderNos", params);
	}

    @Override
    public int updateLog(long userId, Collection<Long> orderNos, int oldOrderSatus, int newOrderStatus,
                         long currentTime) {
        Map<String, Object> params = new HashMap<>();

        params.put("userId", userId);
        params.put("orderNos", orderNos);
        params.put("oldStatus", oldOrderSatus);
        params.put("newStatus", newOrderStatus);
        params.put("createTime", currentTime);

        return getSqlSession().insert("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.updateLog", params);
    }

	@Override
	public List<OrderNewLog> getByOrderNos(Collection<Long> orderNos, OrderStatus oldStatus, OrderStatus newStatus) {
        Map<String, Object> params = new HashMap<>();

        params.put("orderNos", orderNos);
        params.put("oldStatus", oldStatus.getIntValue());
        params.put("newStatus", newStatus.getIntValue());

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.getByOrderNos", params);
	}

	@Override
	public int getNewUserBuyCountByTime(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.getNewUserBuyCountByTime",
            params);
	}

	@Override
	public int getSaleNumByTime(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.getSaleNumByTime", params);
	}

	@Override
	public int getSaleProductCountByTime(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        
        Integer sum = getSqlSession().selectOne(
            "com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.getSaleProductCountByTime", params);
        
        return sum == null ? 0 : sum;
	}

	@Override
	public List<Map<String, Object>> saleCountPerProduct(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.saleCountPerProduct", params);
	}

	@Override
	public List<Map<String, Object>> saleCountPerCategory(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.saleCountPerCategory", params);
	}

	@Override
	public List<Map<String, Object>> saleCountPerBrand(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.saleCountPerBrand", params);
	}

	@Override
	public List<Map<String, Object>> rankSaleLocation(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.rankSaleLocation", params);
	}

	@Override
	public List<Map<String, Object>> rankSaleLocationUser(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.rankSaleLocationUser", params);
	}

	@Override
	public List<Map<String, Object>> rankBuyers(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.rankBuyers", params);
	}

	@Override
	public List<Map<String, Object>> rankBuyersRecordsTime(long startTime, long endTime, int sequence) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("sequence", sequence);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.rankBuyersRecordsTime",
            params);
	}

	@Override
	public List<Map<String, Object>> hotSaleCategory(long startTime, long endTime, int count, long categoryId) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("count", count);
        params.put("categoryId", categoryId);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.hotSaleCategory", params);
	}

	@Override
	public List<Map<String, Object>> hotSaleOfActivity(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.hotSaleOfActivity", params);
	}

	@Override
	public List<Map<String, Object>> saleOrderCountPerDay(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.saleOrderCountPerDay", params);
	}

	@Override
	public List<Map<String, Object>> saleProductCountPerDay(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.saleProductCountPerDay",
            params);
	}

	@Override
	public List<Map<String, Object>> saleOrderMoneytPerDay(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.saleOrderMoneytPerDay",
            params);
	}

	@Override
	public List<Map<String, Object>> rankProductHotsale(long startTime, long endTime, Collection<Long> seasonIds) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("seasonIds", seasonIds);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.rankProductHotsale", params);
	}

	@Override
	public int getNewUserOrderCountPerDay(long newUserStartTime, long newUserEndTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("startTime", newUserStartTime);
		params.put("endTime", newUserEndTime);
		
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.getNewUserOrderCountPerDay",
            params);
	}

	@Override
	public Map<String, BigDecimal> getNewUserProductCountPerDay(long newUserStartTime, long newUserEndTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("startTime", newUserStartTime);
		params.put("endTime", newUserEndTime);
		
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.getNewUserProductCountPerDay",
            params);
	}

	/* (non-Javadoc)
	 * @see com.jiuy.core.dao.OrderNewLogDao#productsale(java.util.Collection)
	 */
	@Override
	public List<Map<String, Object>> productsale(Collection<Long> seasonIds) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("seasonIds", seasonIds);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.productsale", params);
	}

	@Override
	public List<Map<String, Object>> productSkuSale(Collection<Long> seasonIds) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("seasonIds", seasonIds);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewLogDaoSqlImpl.productSkuSale", params);
	}
}
