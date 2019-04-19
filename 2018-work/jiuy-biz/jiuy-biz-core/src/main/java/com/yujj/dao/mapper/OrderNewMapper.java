/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.order.OrderNewVO;

/**
 * @author LWS
 *
 */
@DBMaster
public interface OrderNewMapper {

    int insertOrder(OrderNew orderNew);

    OrderNew getOrderByNo(@Param("orderNo") long orderNo);
    
    OrderNew getOrderByNoAllStatus(@Param("orderNo") long orderNo);

    OrderNewVO getUserOrderByNo(@Param("userId") long userId, @Param("orderNo") String orderNo);
    
    OrderNew getUserOrderByNoOnly(@Param("orderNo") String orderNo);
    
    OrderNew getUserOrderByNoAll(@Param("userId") long userId, @Param("orderNo") String orderNo);
    
//    int getUserOrderCountByOrderStatus(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus);
    int getUserOrderCount(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus);
    
    int getUserOrderCountAll(@Param("userId") long userId);
    
    int getUserOrderDeductCoinNum(long orderNo);
    
    int getUserOrderCountForFirstDiscount(@Param("userId") long userId);
    
    int updateOrderAddressAfterSale(@Param("userId")long userId, @Param("orderNo") long orderNo, @Param("expressInfo") String expressInfo, @Param("time") long time);
    
    int getUserOrderCountWithoutParent(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus);
    
    int getUserOrderCountByStatuses(@Param("userId") long userId, @Param("statuses") Collection<Integer> statuses, @Param("orderSearchNo") String orderSearchNo);
    
    int getUserNewOrdersCountAfterSale(@Param("userId") long userId, @Param("statuses") Collection<Integer> statuses, @Param("orderSearchNo") String orderSearchNo, @Param("validTime") long validTime);
    
    List<OrderNewVO> getUserOrders(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus, @Param("pageQuery") PageQuery pageQuery);
//    List<OrderNewVO> getUserOrdersByOrderStatus(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus, @Param("pageQuery") PageQuery pageQuery);
    
    List<OrderNewVO> getUserOrdersWithoutParent(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus,
    		@Param("pageQuery") PageQuery pageQuery);
    
    List<OrderNew> getUserOrdersNewByStatuses(@Param("userId") long userId, @Param("statuses") Collection<Integer> statuses,
    		@Param("pageQuery") PageQuery pageQuery , @Param("orderSearchNo") String orderSearchNo);
    
    List<OrderNew> getUserOrdersNewAfterSale(@Param("userId") long userId, @Param("statuses") Collection<Integer> statuses,
    		@Param("pageQuery") PageQuery pageQuery , @Param("orderSearchNo") String orderSearchNo, @Param("validTime") long validTime);
    
    List<OrderNewVO> getChildOrderList(@Param("userId") long userId, @Param("orderNOs") Collection<Long> orderNOs);

    List<Map<String, Integer>> getOrderCountMap(@Param("userId") long userId);

    int updateOrderPayStatus(@Param("orderNo") long orderNo, @Param("paymentNo") String paymentNo,
                             @Param("paymentType") PaymentType paymentType, @Param("newStatus") OrderStatus newStatus,
                             @Param("oldStatus") OrderStatus oldStatus, @Param("time") long time);
    
    int updateOrderCommission(@Param("orderNew") OrderNew orderNew, @Param("time") long time);

    int cancelOrder(@Param("id") long id, @Param("time") long time, @Param("cancelReason") String cancelReason);

    int updateOrderAsSended(@Param("id") long id, @Param("time") long time);
    
    int updateOrderParentId(@Param("orderNo") long orderNo, @Param("parentId") long parentId , @Param("lOWarehouseId") long lOWarehouseId,    @Param("time") long time);

    int updateOrderStatus(@Param("orderNo") long orderNo, @Param("newStatus") OrderStatus newStatus,
                          @Param("oldStatus") OrderStatus oldStatus, @Param("time") long time);

	List<OrderNew> getUserDeliverOrders(@Param("userId")long userId, @Param("pageQuery") PageQuery pageQuery);

	int getUserDeliverOrderCount(@Param("userId")long userId);
	
	int updateAfterSellStatusAndNum(@Param("orderNo") long orderNo, @Param("time") long time);

}
