package com.jiuy.core.dao;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicket;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicketVO;

public interface StoreFinanceTicketDao {

	int searchCount(StoreFinanceTicketVO financeTicket);

	List<Map<String, Object>> search(PageQuery pageQuery, StoreFinanceTicketVO financeTicket);

	int updateWithDraw(StoreFinanceTicket financeTicket);

	int addFinance(long serviceId, int returnType, long time,int sourceType,long storeId);

	int addFromRevoke(StoreFinanceTicket storeFinanceTicket);

}
