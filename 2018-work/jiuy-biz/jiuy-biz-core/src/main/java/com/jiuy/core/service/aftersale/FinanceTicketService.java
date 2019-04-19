package com.jiuy.core.service.aftersale;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.aftersale.FinanceTicket;
import com.jiuy.core.meta.aftersale.FinanceTicketVO;
import com.jiuyuan.entity.query.PageQuery;

public interface FinanceTicketService {

	List<Map<String, Object>> search(PageQuery pageQuery, FinanceTicketVO financeTicket);

	int searchCount(FinanceTicketVO financeTicket);

	int add(long serviceId, int returnType,int resourceType,long yjjNumber);

	int updateFinanceTicket(FinanceTicket financeTicket);

	int addFromRevoke(FinanceTicket financeTicket);

}
