package com.jiuy.core.business.facade;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.service.storeaftersale.StoreFinanceTicketService;
import com.jiuy.core.service.storeaftersale.StoreServiceTicketService;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicket;

@Service
public class StoreFinanceTicketFacade {
	
	@Resource
	private StoreFinanceTicketService storeFinanceTicketService;
	
	@Resource
	private StoreServiceTicketService storeServiceTicketService;
	
	@Transactional(rollbackFor = Exception.class)
	public void updateFinanceTicket(StoreFinanceTicket financeTicket) {
		storeFinanceTicketService.updateFinanceTicket(financeTicket);
		storeServiceTicketService.updateServiceTicket(financeTicket.getServiceId(), 6, null);
		
	}

}
