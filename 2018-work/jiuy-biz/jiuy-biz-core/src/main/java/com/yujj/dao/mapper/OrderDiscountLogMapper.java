package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.order.OrderDiscountLog;

@DBMaster
public interface OrderDiscountLogMapper {

	int insertOrderDiscountLogs(@Param("orderDiscountLogs") List<OrderDiscountLog> orderDiscountLogs);
	
}
