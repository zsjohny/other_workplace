package com.store.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.ServiceTicket;
import com.store.entity.OrderAfterSaleCountVO;

/**
 * @author jeff.zhan
 * @version 2016年11月15日 下午2:45:05
 * 
 */

@DBMaster
public interface ServiceTicketMapper {

	int updateServiceOrderPaid(@Param("processOrderNo") long processOrderNo, @Param("newStatus") int newStatus, @Param("oldStatus") int oldStatus, @Param("sysTime") long sysTime );

	int getOrderAfterSaleCount(@Param("userId") long userId,  @Param("orderNo") long orderNo);

	@MapKey("orderItemId")
    Map<Long, OrderAfterSaleCountVO> getOrderAfterSaleMap(@Param("userId") long userId,  @Param("orderNo") long orderNo);

	ServiceTicket getServiceTicketById(@Param("userId") long userId, @Param("id") long id);

}