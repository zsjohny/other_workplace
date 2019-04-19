/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.order.Order;

/**
 * @author LWS
 *
 */
@DBMaster
public interface OrderMapper {
//删除旧表
//    int insertOrder(Order order);

//    Order getOrderByNo(@Param("orderNo") String orderNo);
    
//    Order getOrderById(@Param("id") long id);
    
    //删除旧表
//    Order getOrderByNoAllStatus(@Param("orderNo") String orderNo);
//删除旧表
//    Order getUserOrderByNo(@Param("userId") long userId, @Param("orderNo") String orderNo);
    //删除旧表
//    Order getUserOrderByNoAll(@Param("userId") long userId, @Param("orderNo") String orderNo);
//    /**
//     * 获取指定订单状态订单数量
//     * @param userId
//     * @param orderStatus
//     * @return
//     */
//    int getUserOrderCount(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus);
    
//    int getUserOrderNewCount(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus);
    /**
     * 获取指定订单状态订单
     * @param userId
     * @param orderStatus
     * @param pageQuery
     * @return
     */
//    List<Order> getUserOrders(@Param("userId") long userId, @Param("orderStatus") OrderStatus orderStatus,@Param("pageQuery") PageQuery pageQuery);
    //删除旧表
//    List<Map<String, Integer>> getOrderCountMap(@Param("userId") long userId);
    //删除旧表
//    int updateOrderPayStatus(@Param("id") long id, @Param("paymentNo") String paymentNo,
//                             @Param("paymentType") PaymentType paymentType, @Param("newStatus") OrderStatus newStatus,
//                             @Param("oldStatus") OrderStatus oldStatus, @Param("time") long time);

    //删除旧表
//    int cancelOrder(@Param("id") long id, @Param("time") long time);
    	//删除旧表
//    int updateOrderAsSended(@Param("id") long id, @Param("time") long time);
    //删除旧表
//    int updateOrderStatus(@Param("id") long id, @Param("newStatus") OrderStatus newStatus,
//                          @Param("oldStatus") OrderStatus oldStatus, @Param("time") long time);

//	List<Order> getUserDeliverOrders(@Param("userId")long userId, @Param("pageQuery") PageQuery pageQuery);
	//获取用户已发货状态订单数量
    //删除旧表
//	int getUserDeliverOrderCount(@Param("userId")long userId);

}
