package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.order.OrderLog;
import com.yujj.entity.order.OrderNewLog;

@DBMaster
public interface OrderNewLogMapper {

    int addOrderLog(OrderNewLog orderLog);
    
    OrderNewLog selectOrderLogByOrderNoAndStatus(@Param("orderNo") long orderNo, @Param("orderStatus") OrderStatus orderStatus);
}
