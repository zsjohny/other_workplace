package com.jiuy.core.service.storeaftersale;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicket;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicketVO;

public interface StoreFinanceTicketService {

	int searchCount(StoreFinanceTicketVO financeTicket);

	List<Map<String, Object>> search(PageQuery pageQuery, StoreFinanceTicketVO financeTicket);

	int updateFinanceTicket(StoreFinanceTicket financeTicket);

	int add(long serviceId, int returnType,int sourceType,long storeId);

	int addFromRevoke(StoreFinanceTicket storeFinanceTicket);

}
