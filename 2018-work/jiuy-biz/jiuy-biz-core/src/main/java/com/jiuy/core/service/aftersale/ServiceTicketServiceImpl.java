package com.jiuy.core.service.aftersale;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.ServiceTicketDao;
import com.jiuy.core.meta.aftersale.ServiceTicketVO;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class ServiceTicketServiceImpl implements ServiceTicketService{

	@Resource
	private ServiceTicketDao serviceTicketDao;
	
	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, ServiceTicketVO serviceTicket) {
		return serviceTicketDao.search(pageQuery, serviceTicket);
	}

	@Override
	public int searchCount(ServiceTicketVO serviceTicket) {
		return serviceTicketDao.searchCount(serviceTicket);
	}

	@Override
	public int updateServiceTicket(long serviceId, int status, String message) {
		long time = System.currentTimeMillis();
		return serviceTicketDao.updateServiceTicket(serviceId, status, message, time);
	}

	@Override
	public int updateServiceTicket(long serviceId, int processResult, double processMoney, double processExpressMoney,
			int processReturnJiuCoin, String message, int status) {
		long time = System.currentTimeMillis();
		return serviceTicketDao.updateServiceTicket(serviceId, processResult, processMoney, processExpressMoney, processReturnJiuCoin, message, time, status);
	}

	@Override
	public int updateServiceTicket(long serviceId, int processResult, double sellerExpressMoney,
			String sellerExpressCom, String sellerExpressNo, String message, int status) {
		long time = System.currentTimeMillis();
		return serviceTicketDao.updateServiceTicket(serviceId, processResult, sellerExpressMoney, sellerExpressCom, sellerExpressNo, message, time, status);
	}

	@Override
	public ServiceTicket ServiceTicketOfId(long serviceId) {
		return serviceTicketDao.ServiceTicketOfId(serviceId);
	}

	@Override
	public int updateServiceTicket(long serviceId, long orderNo) {
		return serviceTicketDao.updateServiceTicket(serviceId, orderNo);
	}

    @Override
    public List<ServiceTicket> getItems(int status, int type) {
        return serviceTicketDao.getItems(status, type);
    }

    @Override
    public int updateServiceTicket(Collection<Long> serviceIds, int status) {
        return serviceTicketDao.updateServiceTicket(serviceIds, status);
    }

	@Override
	public int addFromRevoke(ServiceTicket serviceTicket) {
        return serviceTicketDao.addFromRevoke(serviceTicket);
	}

}
