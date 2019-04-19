package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.dao.OrderNewDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.meta.order.OrderNewSO;
import com.jiuy.core.meta.order.OrderNewUO;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.query.PageQuery;

public class OrderNewDaoSqlImpl extends SqlSupport implements OrderNewDao {

    @Override
    public List<OrderNew> childOfOrderNew(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.childOfOrderNew", params);
    }

    @Override
    public int updateMegerdSelf(Collection<Long> singleMerge) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNos", singleMerge);

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.updateMegerdSelf", params);
    }

    @Override
    public OrderNew addMergerdOrderNew(OrderNew orderNew) {
        getSqlSession().insert("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.addMergerdOrderNew", orderNew);

        return orderNew;
    }

    @Override
    public int updateMegerdChild(Collection<Long> orderNos, long orderNo) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNos", orderNos);
        params.put("mergedId", orderNo);

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.updateMegerdChild", params);
    }

    @Override
    public List<OrderNew> selfMergedOrderNew(long startTime, long endTime, int orderType) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("orderType", orderType);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.selfMergedOrderNew", params);
    }

    @Override
    public List<OrderNew> getParentMergedOrderNews(long startTime, long endTime,int orderType) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("orderType", orderType);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getParentMergedOrderNews", params);
    }

    @Override
    public List<OrderNew> orderNewsOfOrderNos(Collection<Long> orderNos) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNos", orderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.orderNewsOfOrderNos", params);
    }

	@Override
	public OrderNew orderNewOfOrderNo(long orderNo) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNo", orderNo);

        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.orderNewOfOrderNo", params);
	}

	@Override
	public List<OrderNew> orderNewsOfSplitOrderNos(Collection<Long> splitOrderNos) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("parentIds", splitOrderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.orderNewsOfSplitOrderNos", params);
	}
    
	@Override
	public List<OrderNew> orderNewsOfParentMergedOrderNos(Collection<Long> parentMergedOrderNos) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("mergedIds", parentMergedOrderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.orderNewsOfParentMergedOrderNos", params);
	}

	@Override
    public int updateOrderStatus(Collection<Long> orderNos, int orderStatus) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNos", orderNos);
        params.put("orderStatus", orderStatus);

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.updateOrderStatus", params);
	}

	@Override
	public List<Long> getOrderNosByOrderStatus(int orderStatus, List<Integer> afterSaleStatus_list) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderStatus", orderStatus);
        params.put("afterSaleStatus_list", afterSaleStatus_list);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getOrderNosByOrderStatus", params);
	}

	@Override
	public List<OrderNew> allUnpaidFacepayOrderNew() {
		Map<String, Object> params = new HashMap<String, Object>();
		long now = System.currentTimeMillis();
        params.put("time", now);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.allUnpaidFacepayOrderNew", params);
	}

	@Override
	   public List<OrderNew> searchOrderNews(PageQuery pageQuery, String orderNo, int orderType, long userId,
               String receiver, String phone, long startTime, long endTime, int orderStatus, Collection<Long> orderNos,
               int sendType) {
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("pageQuery", pageQuery);
		params.put("orderStatus", orderStatus);
		params.put("orderNo", orderNo);
		params.put("orderType", orderType);
		params.put("userId", userId);
		params.put("receiver", receiver);
		params.put("phone", phone);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orderNos", orderNos);
		params.put("sendType", sendType);
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.searchOrderNews", params);
	}
	
	@Override
	public int searchOrderNewsCount(String orderNo, int orderType, long userId, String receiver, String phone, long startTime,
    		long endTime, int orderStatus, Collection<Long> orderNos,int sendType) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderNo", orderNo);
		params.put("orderStatus", orderStatus);
		params.put("orderType", orderType);
		params.put("userId", userId);
		params.put("receiver", receiver);
		params.put("phone", phone);	
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orderNos", orderNos);
		params.put("sendType", sendType);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.searchOrderNewsCount", params);
	}

	@Override
	public List<OrderNew> searchUndelivered(PageQuery pageQuery, String orderNo, long userId, String receiver,
			String phone, long startTime, long endTime, Set<Long> orderNos,int orderType) {
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("pageQuery", pageQuery);
		params.put("orderNo", orderNo);
		params.put("userId", userId);
		params.put("receiver", receiver);
		params.put("phone", phone);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orderNos", orderNos);
		params.put("orderType", orderType);
		
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.searchUndelivered", params);
	}
	

	@Override
	public int searchUndeliveredCount(String orderNo, long userId, String receiver, String phone, long startTime,
			long endTime, Set<Long> orderNos,int orderType) {
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderNo", orderNo);
		params.put("userId", userId);
		params.put("receiver", receiver);
		params.put("phone", phone);	
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orderNos", orderNos);
		params.put("orderType", orderType);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.searchUndeliveredCount", params);
	}



	@Override
	public List<OrderNew> childOfCombinationOrderNos(Collection<Long> combinationOrderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("mergedIds", combinationOrderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.childOfCombinationOrderNos", params);
	}

    @Override
    public int addPushTime(Collection<Long> allOrderNos, long erpTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderNos", allOrderNos);
        params.put("pushTime", erpTime);

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.addPushTime", params);
    }

	@Override
	public List<OrderNew> childOfSplitOrderNos(Collection<Long> splitOrderNos) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("parentIds", splitOrderNos);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.childOfSplitOrderNos", params);
	}

    @Override
    public List<OrderNew> getOrderNewByOrderStatus(int orderStatus) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderStatus", orderStatus);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getOrderNewByOrderStatus", params);
    }

	@Override
	public OrderNew insert(OrderNew orderNew2) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.insert", orderNew2);
		
		return orderNew2;
	}

	@Override
	public OrderNew orderNewOfServiceId(long serviceId) {

        return  getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.orderNewOfServiceId", serviceId);
	}

	@Override
	public int updateCommission(OrderNew orderNew) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.updateCommission",orderNew);
	}

	@Override
	public int freezeAfterSales(Collection<Long> freezeOrderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("freezeOrderNos", freezeOrderNos);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.freezeAfterSales", params);
	}

	@Override
	public OrderNew orderNewOfReturnNo(long parentId) {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.orderNewOfReturnNo", parentId);
	}

	@Override
	public List<Long>  searchOfParentId(long orderNo) {
		return  getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.searchOfParentId", orderNo);
	}

	@Override
	public int update(OrderNewUO uo, long orderNo) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("uo", uo);
		params.put("orderNo", orderNo);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.update", params);
	}

	@Override
	public List<OrderNew> getOrderNewOfTime(long startTime, long endTime) {
		 Map<String, Object> params = new HashMap<String, Object>();

	        params.put("startTime", startTime);
	        params.put("endTime", endTime);

	        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getOrderNewOfTime", params);
	}

	@Override
	public List<OrderNew> getByUserIdStatus(long userId, OrderStatus orderStatus) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("userId", userId);
		params.put("orderStatus", orderStatus);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getByUserIdStatus", params);
	}

	@Override
	public Map<Long, OrderNew> searchUndeliveredMap(OrderNewSO so, PageQuery pageQuery, Set<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("so", so);
		params.put("pageQuery", pageQuery);
		params.put("orderNos", orderNos);
		
		return getSqlSession().selectMap("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.searchUndeliveredMap", params, "orderNo");
	}

	@Override
	public int searchUndeliveredNewCount(OrderNewSO so, Set<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("so", so);
		params.put("orderNos", orderNos);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.searchUndeliveredNewCount", params);
	}

	@Override
	public List<OrderNew> getByMergedNos(Collection<Long> combineOrderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("combineOrderNos", combineOrderNos);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getByMergedNos", params);
	}

	@Override
	public int increseAvailableCommission(long orderNo, double commission,double returnCommission) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNo", orderNo);
		params.put("commission", commission);
		params.put("returnCommission", returnCommission);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.increseAvailableCommission", params);
	}

	@Override
	public Map<String, Object> getOrderAndReturnMoneyForTime(long startTimeL, long endTimeL) {
		 Map<String, Object> params = new HashMap<String, Object>();

	     params.put("startTime", startTimeL);
	     params.put("endTime", endTimeL);

	     return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getOrderAndReturnMoneyForTime", params);
	}

	@Override
	public Map<String, Object> getTotalDataForTime(long startTimeL, long endTimeL) {
		 Map<String, Object> params = new HashMap<String, Object>();

	     params.put("startTime", startTimeL);
	     params.put("endTime", endTimeL);

	     return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getTotalDataForTime", params);
	}

	@Override
	public Map<String, Object> getStoreTotalDataForTime(long startTimeL, long endTimeL) {
		Map<String, Object> params = new HashMap<String, Object>();

	     params.put("startTime", startTimeL);
	     params.put("endTime", endTimeL);

	     return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.OrderNewDaoSqlImpl.getStoreTotalDataForTime", params);
	}

}
