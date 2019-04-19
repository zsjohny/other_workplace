/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.afterSale.FinanceTicket;
import com.yujj.entity.afterSale.ServiceTicket;

/**
 * @author LWS
 *
 */
@DBMaster
public interface ServiceTicketMapper {

    int insertServiceTicket(ServiceTicket serviceTicket);

    ServiceTicket getServiceTicketById(@Param("userId") long userId, @Param("id") long id);
    
    ServiceTicket getServiceTicketByOrderNo(@Param("userId") long userId, @Param("orderNo") long orderNo);
    
    FinanceTicket getFinanceTicketById(@Param("serviceId") long serviceId);
   
//    Order getUserOrderByNo(@Param("userId") long userId, @Param("orderNo") String orderNo);
//
    int getUserOrderCount(@Param("userId") long userId, @Param("id") long id, @Param("status") int status);
    
    int getItemAfterSaleValidCount(@Param("userId") long userId, @Param("itemId") long itemId , @Param("orderNo") long orderNo );
    
    int updateServiceTicketById(@Param("userId") long userId, @Param("serviceTicket") ServiceTicket serviceTicket);
    
    int updateServiceExchangeSuccess(@Param("userId") long userId, @Param("id") long id, @Param("newStatus") int newStatus, @Param("oldStatus") int oldStatus,  @Param("sysTime") long sysTime);
    
    int updateServiceOrderPaid(@Param("processOrderNo") long processOrderNo, @Param("newStatus") int newStatus, @Param("oldStatus") int oldStatus, @Param("sysTime") long sysTime );
    
    int updateServiceBuyerExpress(@Param("serviceId") long serviceId, @Param("buyerExpressCom") String buyerExpressCom, @Param("buyerExpressNo") String buyerExpressNo,@Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus, @Param("memo") String memo, @Param("sysTime") long sysTime);
//
	List<ServiceTicket> getUserServiceTicketList(@Param("pageQuery") PageQuery pageQuery, @Param("userId") long userId, @Param("orderSearchNo") String orderSearchNo);
	
	int countUserServiceTicketList(@Param("userId") long userId, @Param("orderSearchNo") String orderSearchNo);

}
