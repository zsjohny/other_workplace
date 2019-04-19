package com.jiuy.core.service.storeaftersale;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreServiceTicketDao;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicket;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicketVO;

@Service
public class StoreServiceTicketServiceImpl implements StoreServiceTicketService{

	@Resource
	private StoreServiceTicketDao storeServiceTicketDao;
	
	@Override
	public int updateServiceTicket(long serviceId, int status, String message) {
		long time = System.currentTimeMillis();
		return storeServiceTicketDao.updateServiceTicket(serviceId, status, message, time);	
	}

	@Override
	public int searchCount(StoreServiceTicketVO serviceTicket) {
		return storeServiceTicketDao.searchCount(serviceTicket);
	}

	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, StoreServiceTicketVO serviceTicket) {
		return storeServiceTicketDao.search(pageQuery,serviceTicket);
	}

	@Override
	public StoreServiceTicket ServiceTicketOfId(long serviceId) {
		return  storeServiceTicketDao.ServiceTicketOfId(serviceId);
	}

	@Override
	public int updateServiceTicket(long serviceId, int processResult, double processMoney, double processExpressMoney,
			String message, int status) {
		long time = System.currentTimeMillis();
		return storeServiceTicketDao.updateServiceTicket(serviceId, processResult, processMoney, processExpressMoney, message, time, status);
	}

	@Override
	public int updateServiceTicket(long serviceId, long orderNo) {
		return storeServiceTicketDao.updateServiceTicket(serviceId, orderNo);
	}
	
	@Override
	public int updateServiceTicket(long serviceId, int processResult, double sellerExpressMoney,
			String sellerExpressCom, String sellerExpressNo, String message, int status) {
		long time = System.currentTimeMillis();
		return storeServiceTicketDao.updateServiceTicket(serviceId, processResult, sellerExpressMoney, sellerExpressCom, sellerExpressNo, message, time, status);
	}

	@Override
	public int addFromRevoke(StoreServiceTicket storeServiceTicket) {
		return storeServiceTicketDao.addFromRevoke(storeServiceTicket);
	}
}
