package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.aftersale.FinanceTicket;
import com.jiuy.core.meta.aftersale.FinanceTicketVO;
import com.jiuyuan.entity.query.PageQuery;

public interface FinanceTicketDao extends DomainDao<FinanceTicket, Long>{


	List<Map<String, Object>> search(PageQuery query, FinanceTicketVO ft);

	int searchCount(FinanceTicketVO ft);

	int addFinance(long serviceId, int returnType, long time,int resourceType,long yjjNumber);

	int updateFinanceTicket(FinanceTicket financeTicket);

	int addFromRevoke(FinanceTicket financeTicket);
	
}
