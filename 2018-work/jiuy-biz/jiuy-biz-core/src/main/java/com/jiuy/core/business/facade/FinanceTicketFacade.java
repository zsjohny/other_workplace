package com.jiuy.core.business.facade;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.meta.aftersale.FinanceTicket;
import com.jiuy.core.service.aftersale.FinanceTicketService;
import com.jiuy.core.service.aftersale.ServiceTicketService;

@Service
public class FinanceTicketFacade {
	
	@Resource
	private FinanceTicketService financeTicketService;
	
	@Resource
	private ServiceTicketService serviceTicketService;

	@Transactional(rollbackFor = Exception.class)
	public void updateFinanceTicket(FinanceTicket financeTicket) {
		financeTicketService.updateFinanceTicket(financeTicket);
		serviceTicketService.updateServiceTicket(financeTicket.getServiceId(), 6, null);
	}
	
}
