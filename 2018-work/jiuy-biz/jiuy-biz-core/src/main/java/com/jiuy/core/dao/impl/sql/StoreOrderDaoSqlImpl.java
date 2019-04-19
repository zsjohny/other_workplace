package com.jiuy.core.dao.impl.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreOrderDao;
import com.jiuyuan.constant.order.OrderStatus;
//import com.jiuyuan.entity.newentity.StoreOrder;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderSO;
import com.jiuyuan.entity.storeorder.StoreOrderVO;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午4:00:17
*/
@Repository
public class StoreOrderDaoSqlImpl implements StoreOrderDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public StoreOrder orderOfNo(String oldOrderNo) {
		return null;
	}

	@Override
	public List<StoreOrder> searchStoreOrders(PageQuery pageQuery, String orderNo, int orderType, long storeId,
			String receiver, String phone, long startTime, long endTime, int orderStatus, Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("pageQuery", pageQuery);
		params.put("orderStatus", orderStatus);
		params.put("orderNo", orderNo);
		params.put("orderType", orderType);
		params.put("storeId", storeId);
		params.put("receiver", receiver);
		params.put("phone", phone);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orderNos", orderNos);
		
        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.searchStoreOrders", params);
	}

	@Override
	public int searchStoreOrdersCount(String orderNo, int orderType, long storeId, String receiver, String phone,
			long startTime, long endTime, int orderStatus, Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderNo", orderNo);
		params.put("orderStatus", orderStatus);
		params.put("orderType", orderType);
		params.put("storeId", storeId);
		params.put("receiver", receiver);
		params.put("phone", phone);	
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orderNos", orderNos);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.searchStoreOrdersCount", params);
	}

	@Override
	public List<StoreOrder> storeOrdersOfSplitOrderNos(Collection<Long> splitOrderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("parentIds", splitOrderNos);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.storeOrdersOfSplitOrderNos", params);
	}

	@Override
	public List<StoreOrder> storeOrdersOfParentMergedOrderNos(Collection<Long> parentMergedOrderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("mergedIds", parentMergedOrderNos);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.storeOrdersOfParentMergedOrderNos", params);
	}

	@Override
	public List<StoreOrder> storeOrdersOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNos", orderNos);
        
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.storeOrdersOfOrderNos", params);
	}

	@Override
	public List<StoreOrder> childOfSplitOrderNos(Collection<Long> splitOrderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("parentIds", splitOrderNos);
        
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.childOfSplitOrderNos", params);
	}

	@Override
	public List<StoreOrder> childOfCombinationOrderNos(Collection<Long> combinationOrderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("mergedIds", combinationOrderNos);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.childOfCombinationOrderNos", params);
	}

	@Override
	public int updateOrderStatus(Collection<Long> orderNos, int orderStatus) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNos", orderNos);
        params.put("orderStatus", orderStatus);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.updateOrderStatus", params);
	}

	@Override
	public StoreOrder storeOrderOfOrderNo(long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNo", orderNo);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.storeOrderOfOrderNo", params);
	}

	@Override
	public List<StoreOrderVO> searchUndelivered(PageQuery pageQuery, String orderNo, long storeId, String receiver,
			String phone, long startTime, long endTime, Set<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("pageQuery", pageQuery);
		params.put("orderNo", orderNo);
		params.put("storeId", storeId);
		params.put("receiver", receiver);
		params.put("phone", phone);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.searchUndelivered",params);
	}

	@Override
	public int searchUndeliveredCount(String orderNo, long storeId, String receiver, String phone, long startTime,
			long endTime, Set<Long> orderNos) {
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderNo", orderNo);
		params.put("storeId", storeId);
		params.put("receiver", receiver);
		params.put("phone", phone);	
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.searchUndeliveredCount", params);
	}

	@Override
	public StoreOrder orderNewOfOrderNo(long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNo", orderNo);

        return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.orderNewOfOrderNo", params);
	}

	@Override
	public StoreOrder insert(StoreOrder orderNew2) {
		sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.insert", orderNew2);
		
		return orderNew2;
	}

	@Override
	public List<StoreOrder> allUnpaidFacepayOrderNew(long currentTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("currentTime", currentTime);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.allUnpaidFacepayOrderNew",params);
	}

	@Override
	public List<StoreOrder> getByBrandOrder(long brandOrderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("brandOrderNo", brandOrderNo);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.getByBrandOrder",params);
	}

	@Override
	public Map<Long, StoreOrder> searchUndeliveredMap(StoreOrderSO so, PageQuery pageQuery, Collection<Long> orderNos, List<Long> warehouseIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("so", so);
		params.put("pageQuery", pageQuery);
		params.put("orderNos", orderNos);
		params.put("warehouseIds", warehouseIds);
		
		return sqlSessionTemplate.selectMap("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.searchUndeliveredMap", params, "orderNo");
	}

	@Override
	public int searchUndeliveredCount2(StoreOrderSO so, Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("so", so);
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.searchUndeliveredCount2", params);
	}

	@Override
	public List<StoreOrder> getByMergedNos(Collection<Long> combineOrderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("combineOrderNos", combineOrderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.getByMergedNos", params);
	}

	@Override
	public List<StoreOrder> searchSubOrders(long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("endTime", endTime);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.searchSubOrders", params);
	}

	@Override
	public StoreOrder add(StoreOrder storeOrder) {
		sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.add", storeOrder);
		return storeOrder;
	}

	@Override
	public int updateMergedId(Collection<Long> orderNos, long mergedId, long current) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderNos", orderNos);
		params.put("mergedId", mergedId);
		params.put("currentTime", current);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.updateMergedId", params);
	}

	@Override
	public int updateSelfMergedId(Collection<Long> orderNos, long current) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderNos", orderNos);
		params.put("currentTime", current);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.updateSelfMergedId", params);
	}

	@Override
	public List<StoreOrder> getOrders(OrderStatus orderStatus) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderStatus", orderStatus);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.getOrders", params);
	}

	@Override
	public List<StoreOrder> orderNewsOfParentMergedOrderNos(List<Long> mergedIds) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("mergedIds", mergedIds);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.orderNewsOfParentMergedOrderNos", params);
	}

	@Override
	public int update(long brandOrderNo, long current, long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("brandOrderNo", brandOrderNo);
		params.put("current", current);
		params.put("orderNo", orderNo);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.update", params);
	}

	@Override
	public List<StoreOrder> ordersOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.ordersOfOrderNos", params);
	}

	@Override
	public List<Long> getOrderNosByOrderStatus(int orderStatus, List<Integer> afterSellStatus_list) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderStatus", orderStatus);
		params.put("afterSellStatus_list", afterSellStatus_list);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.getOrderNosByOrderStatus", params);
	}

	@Override
	public List<StoreOrder> getByStoreIds(Collection<Long> storeIds) {
		if (storeIds.size() < 1) {
			return new ArrayList<>();
		}
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("storeIds", storeIds);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.getByStoreIds", params);
	}
	

    @Override
    public List<StoreOrder> selfMergedStoreOrder(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.selfMergedStoreOrder", params);
    }

    @Override
    public List<StoreOrder> getParentMergedStoreOrder(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.getParentMergedStoreOrder", params);
    }

	@Override
	public List<Long> searchStoreOrderFreezeOrders(OrderStatus success, int noWithdraw) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("OrderStatus", success.getIntValue());
		params.put("noWithdraw", noWithdraw);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.searchStoreOrderFreezeOrders", params);
	}

	@Override
	public int updateHasWithdrawed(long orderNo, int withdraw) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		params.put("withdraw", withdraw);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.updateHasWithdrawed", params);
	}

	@Override
	public int getReferrerAllRctivateCount(String key) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phoneNumber", key);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.getReferrerAllRctivateCount", params);
	}

	@Override
	public int getAreaAllRctivateCount(String key) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("province", key);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreOrderDaoSqlImpl.getAreaAllRctivateCount", params);
	}
}
