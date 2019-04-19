package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicket;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicketVO;

public interface StoreServiceTicketDao {

	int updateServiceTicket(long serviceId, int status, String message, long time);

	int searchCount(StoreServiceTicketVO serviceTicket);

	List<Map<String, Object>> search(PageQuery pageQuery, StoreServiceTicketVO serviceTicket);

	StoreServiceTicket ServiceTicketOfId(long serviceId);

	int updateServiceTicket(long serviceId, int processResult, double processMoney, double processExpressMoney,
			String message, long time, int status);

	int updateServiceTicket(long serviceId, long orderNo);
	
	int updateServiceTicket(long serviceId, int processResult, double sellerExpressMoney, String sellerExpressCom,
			String sellerExpressNo, String message, long time, int status);

	List<StoreServiceTicket> getByNotStatus(List<Integer> status_list);

	int addFromRevoke(StoreServiceTicket storeServiceTicket);
}
