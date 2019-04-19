package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.meta.order.OrderNewSO;
import com.jiuy.core.meta.order.OrderNewUO;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.query.PageQuery;

public interface OrderNewDao {

    List<OrderNew> childOfOrderNew(long startTime, long endTime);

    int updateMegerdSelf(Collection<Long> singleMerge);

    OrderNew addMergerdOrderNew(OrderNew orderNew);

    int updateMegerdChild(Collection<Long> orderNos, long orderNo);

    List<OrderNew> selfMergedOrderNew(long startTime, long endTime, int orderType);

    List<OrderNew> getParentMergedOrderNews(long startTime, long endTime, int orderType);

    List<OrderNew> orderNewsOfOrderNos(Collection<Long> parentMergedOrderNos);

	List<OrderNew> orderNewsOfParentMergedOrderNos(Collection<Long> parentMergedOrderNos);

    int updateOrderStatus(Collection<Long> orderNos, int orderStatus);

	List<Long> getOrderNosByOrderStatus(int orderStatus, List<Integer> afterSaleStatus_list);

    List<OrderNew> searchOrderNews(PageQuery pageQuery, String orderNo, int orderType, long userId, String receiver,
                                   String phone,
			long startTime, long endTime, int orderStatus, Collection<Long> orderNos,int sendType);
    
    int searchOrderNewsCount(String orderNo, int orderType, long userId, String receiver, String phone, long startTime,
    		long endTime, int orderStatus, Collection<Long> orderNos,int sendType);

	List<OrderNew> orderNewsOfSplitOrderNos(Collection<Long> splitOrderNos);

	List<OrderNew> childOfCombinationOrderNos(Collection<Long> combinationOrderNos);

	List<OrderNew> childOfSplitOrderNos(Collection<Long> splitOrderNos);

    List<OrderNew> getOrderNewByOrderStatus(int intValue);

	OrderNew orderNewOfOrderNo(long orderNo);

	List<OrderNew> searchUndelivered(PageQuery pageQuery, String orderNo, long userId, String receiver, String phone,
			long startTime, long endTime, Set<Long> orderNos,int orderType);

	int searchUndeliveredCount(String orderNo, long userId, String receiver, String phone, long startTime, long endTime,
			Set<Long> orderNos,int orderType);
    int addPushTime(Collection<Long> allOrderNos, long erpTime);

    OrderNew insert(OrderNew orderNew2);

	OrderNew orderNewOfServiceId(long serviceId);

	int updateCommission(OrderNew orderNew);

	int freezeAfterSales(Collection<Long> freezeOrderNos);

	OrderNew orderNewOfReturnNo(long parentId);

	List<Long>  searchOfParentId(long orderNo);

	/**
	 * @return
	 */
	List<OrderNew> allUnpaidFacepayOrderNew();

	int update(OrderNewUO uo, long orderNo);

	List<OrderNew> getOrderNewOfTime(long startTime, long endTime);

	List<OrderNew> getByUserIdStatus(long userId, OrderStatus orderStatus);

	Map<Long, OrderNew> searchUndeliveredMap(OrderNewSO so, PageQuery pageQuery, Set<Long> orderNos);

	int searchUndeliveredNewCount(OrderNewSO so, Set<Long> orderNos);

	List<OrderNew> getByMergedNos(Collection<Long> combineOrderNos);

	int increseAvailableCommission(long parentId, double commission,double returnCommission);

	Map<String, Object> getOrderAndReturnMoneyForTime(long startTimeL, long endTimeL);

	Map<String, Object> getTotalDataForTime(long startTimeL, long endTimeL);

	Map<String, Object> getStoreTotalDataForTime(long startTimeL, long endTimeL);

}
