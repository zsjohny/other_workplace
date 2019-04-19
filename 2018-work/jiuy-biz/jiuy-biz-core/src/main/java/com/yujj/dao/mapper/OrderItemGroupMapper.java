package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.order.OrderItemGroup;

@DBMaster
public interface OrderItemGroupMapper {
//删除旧表
//    int insertOrderItemGroups(@Param("orderId") long orderId, @Param("orderStatus") OrderStatus orderStatus,
//                              @Param("orderItemGroups") List<OrderItemGroup> orderItemGroups);
//删除旧表
//    List<OrderItemGroup> getOrderItemGroups(@Param("userId") long userId, @Param("orderIds") Collection<Long> orderIds);
//删除旧表
//    int cancelOrder(@Param("orderId") long orderId, @Param("time") long time);
//删除旧表
//    int updateOrderStatus(@Param("orderId") long orderId, @Param("newStatus") OrderStatus newStatus,
//                          @Param("oldStatus") OrderStatus oldStatus, @Param("time") long time);

    int insertOrderItemGroup(OrderItemGroup orderItemGroup);

}
