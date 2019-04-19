package com.jiuy.core.service.aftersale;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.aftersale.ServiceTicketVO;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.query.PageQuery;

public interface ServiceTicketService {
	
	List<Map<String, Object>> search(PageQuery pageQuery, ServiceTicketVO serviceTicket);
	
	int searchCount(ServiceTicketVO serviceTicket);
	
	int updateServiceTicket(long serviceId, int status, String message);
	
	int updateServiceTicket(long serviceId, int processResult, double processMoney, double processExpressMoney,
			int processReturnJiuCoin, String message, int status);
	
	int updateServiceTicket(long serviceId, int processResult, double sellerExpressMoney, String sellerExpressCom,
			String sellerExpressNo, String message, int status);
	
	ServiceTicket ServiceTicketOfId(long serviceId);

	int updateServiceTicket(long serviceId, long orderNo);

    /**
     * @param status
     * @param type 0:退货 1:换货
     * @return
     */
    List<ServiceTicket> getItems(int status, int type);

    int updateServiceTicket(Collection<Long> serviceIds, int status);

	int addFromRevoke(ServiceTicket serviceTicket);

}
