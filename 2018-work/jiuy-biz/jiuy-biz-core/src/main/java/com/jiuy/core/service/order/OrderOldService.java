package com.jiuy.core.service.order;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.meta.order.OrderNewSO;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.query.PageQuery;

public interface OrderOldService {

    List<OrderNew> childOfOrderNew(long startTime, long endTime);

    int updateMegerdSelf(Collection<Long> singleMerge);

    OrderNew addMergerdOrderNew(OrderNew orderNew);

    int updateMegerdChild(Collection<Long> orderNos, long orderNo);

    List<OrderNew> selfMergedOrderNew(long startTime, long endTime, int orderType);

    List<OrderNew> getParentMergedOrderNews(long startTime, long endTime, int orderType);

    Map<Long, List<OrderNew>> parentMergedMap(Collection<Long> parentMergedOrderNos);

    Map<Long, OrderNew> orderNewMapOfOrderNos(Collection<Long> orderNos);

    int updateOrderStatus(Collection<Long> orderNos, int orderStatus);

	List<Long> getOrderNosByOrderStatus(int intValue);

    List<OrderNew> searchOrderNews(PageQuery pageQuery, String orderNo, int orderType, long userId, String receiver,
                                   String phone,
			long startTime, long endTime, int orderStatus, Collection<Long> orderNos,int sendType);
    
    int searchOrderNewsCount(String orderNo, int orderType, long userId, String receiver, String phone, long startTime,
    		long endTime, int orderStatus, Collection<Long> orderNos,int sendType);

	Map<Long, List<OrderNew>> splitMapOfOrderNos(Collection<Long> splitOrderNos);

	List<OrderNew> childOfCombinationOrderNos(Collection<Long> combinationOrderNos);

	List<OrderNew> childOfSplitOrderNos(Collection<Long> splitOrderNos);

	List<OrderNew> searchUndelivered(PageQuery pageQuery, String orderNo, long userId, String receiver, String phone,
			long startTime, long endTime, Set<Long> orderNos,int orderType);

	int searchUndeliveredCount(String orderNo, long userId, String receiver, String phone, long startTime, long endTime,
			Set<Long> orderNos,int orderType);

    int addPushTime(Collection<Long> allOrderNos, long erpTime);

    List<OrderNew> orderNewsOfOrderNos(Collection<Long> orderNos);

	OrderNew orderNewOfServiceId(long serviceId);

	int updateCommission(OrderNew orderNew);

	OrderNew orderNewOfReturnNo(long parentId);

	List<Long> searchOfParentId(long orderNo);

	/**
	 * @return
	 */
	List<OrderNew> allUnpaidFacepayOrderNew();

	List<OrderNew> getOrderNewOfTime(long startTime, long endTime);

	List<OrderNew> getByUserIdStatus(long userId, OrderStatus paid);

	Map<Long, OrderNew> searchUndeliveredMap(OrderNewSO so, PageQuery pageQuery, Set<Long> orderNos);

	int searchUndeliveredNewCount(OrderNewSO so, Set<Long> orderNos);

	Map<Long, List<OrderNew>> getMergedChildren(Collection<Long> combine_order_nos);

	OrderNew orderNewOfOrderNo(long orderNo);


}
