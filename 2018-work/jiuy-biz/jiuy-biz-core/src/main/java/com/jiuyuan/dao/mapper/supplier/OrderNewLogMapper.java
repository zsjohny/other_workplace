package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.OrderNewLog;

@DBMaster
public interface OrderNewLogMapper {
	
	int addOrderLog(OrderNewLog orderLog);

	OrderNewLog selectOrderLogByOrderNoAndStatus(@Param("orderNo") long orderNo, @Param("orderStatus") int orderStatus);

}
