/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.order.OrderItem;

/**
 * @author LWS
 *
 */
@DBMaster
public interface OrderItemMapper {

    int insertOrderItems(@Param("orderItems") List<OrderItem> orderItems);

//    List<OrderItem> getOrderItems(@Param("userId") long userId, @Param("orderNos") Collection<Long> orderNos);
    
    List<OrderItem> getOrderNewItems(@Param("userId") long userId, @Param("orderNos") Collection<Long> orderNos);
    
    List<OrderItem> getOrderNewItemsByItemIds(@Param("userId") long userId, @Param("orderItemIds") Collection<Long> orderItemIds);
    
    List<OrderItem> getOrderNewItemsByOrderNO(@Param("userId") long userId, @Param("orderNo") long orderNo);
    
    OrderItem getOrderItemById(@Param("userId") long userId, @Param("orderItemId") long orderItemId);
    //删除旧表
//    List<OrderItem> getOrderItemsByOrderId(@Param("orderId") long orderId);
    //删除旧表
//    int cancelOrder(@Param("orderId") long orderId, @Param("time") long time);
    
    int updateItemsCommssion(@Param("orderNo") long orderNo, @Param("percentage") double percentage, @Param("time") long time);
    
    double getItemsCommssionTotal(@Param("orderNo") long orderNo);
    
    int cancelOrderNew(@Param("orderNo") long orderNo, @Param("time") long time);
    
    
    int updateOrderNo(@Param("itemId") long itemId, @Param("orderNo") long orderNo,@Param("time") long time);
    

}
