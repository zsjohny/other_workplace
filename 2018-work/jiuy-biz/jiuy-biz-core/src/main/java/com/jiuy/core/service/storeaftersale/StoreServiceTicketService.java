package com.jiuy.core.service.storeaftersale;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicket;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicketVO;

public interface StoreServiceTicketService {

	int updateServiceTicket(long serviceId, int i, String message);

	int searchCount(StoreServiceTicketVO serviceTicket);

	List<Map<String, Object>> search(PageQuery pageQuery, StoreServiceTicketVO serviceTicket);

	StoreServiceTicket ServiceTicketOfId(long serviceId);

	int updateServiceTicket(long serviceId, int processResult, double processMoney, double processExpressMoney,
			String message, int status);

	int updateServiceTicket(long serviceId, int processResult, double sellerExpressMoney, String sellerExpressCom,
			String sellerExpressNo, String message, int status);
	
	int updateServiceTicket(long serviceId, long orderNo);

	int addFromRevoke(StoreServiceTicket storeServiceTicket);

}
