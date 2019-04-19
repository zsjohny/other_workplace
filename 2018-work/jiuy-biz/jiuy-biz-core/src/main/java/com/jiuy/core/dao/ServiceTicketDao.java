package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.aftersale.ServiceTicketVO;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.query.PageQuery;

public interface ServiceTicketDao extends DomainDao<ServiceTicket, Long>{

	List<Map<String, Object>> search(PageQuery pageQuery, ServiceTicketVO serviceTicket);

	int searchCount(ServiceTicketVO serviceTicket);

	int updateServiceTicket(long serviceId, int status, String message, long time);

	int updateServiceTicket(long serviceId, int processResult, double processMoney, double processExpressMoney,
			int processReturnJiuCoin, String message, long time, int status);

	int updateServiceTicket(long serviceId, int processResult, double sellerExpressMoney, String sellerExpressCom,
			String sellerExpressNo, String message, long time, int status);

	ServiceTicket ServiceTicketOfId(long serviceId);

	int updateServiceTicket(long serviceId, long orderNo);

    List<ServiceTicket> getItems(int status, int type);

    int updateServiceTicket(Collection<Long> serviceIds, int status);

	List<ServiceTicket> getByStatus(List<Integer> status_list);

	List<ServiceTicket> getByNotStatus(List<Integer> status_list);

	int addFromRevoke(ServiceTicket serviceTicket);
	
}
