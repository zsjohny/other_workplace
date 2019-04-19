package com.jiuy.core.service.aftersale;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.FinanceTicketDao;
import com.jiuy.core.meta.aftersale.FinanceTicket;
import com.jiuy.core.meta.aftersale.FinanceTicketVO;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class FinanceTicketServiceImpl implements FinanceTicketService{
	
	@Resource
	private FinanceTicketDao financeTicketDao;
	
	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, FinanceTicketVO financeTicket) {
		return financeTicketDao.search(pageQuery, financeTicket);
	}

	@Override
	public int searchCount(FinanceTicketVO financeTicket) {
		return financeTicketDao.searchCount(financeTicket);
	}

	@Override
	public int add(long serviceId, int returnType,int resourceType,long yjjNumber) {
		long time = System.currentTimeMillis();
		return financeTicketDao.addFinance(serviceId, returnType, time,resourceType,yjjNumber);
	}

	@Override
	public int updateFinanceTicket(FinanceTicket financeTicket) {
		return financeTicketDao.updateFinanceTicket(financeTicket);
	}

	@Override
	public int addFromRevoke(FinanceTicket financeTicket) {
		return financeTicketDao.addFromRevoke(financeTicket);
	}

}
