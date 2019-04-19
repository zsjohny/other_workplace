package com.jiuy.core.dao.impl.sql;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.FinanceTicketDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.aftersale.FinanceTicket;
import com.jiuy.core.meta.aftersale.FinanceTicketVO;
import com.jiuyuan.entity.query.PageQuery;

public class FinanceTicketDaoSqlImpl extends DomainDaoSqlSupport<FinanceTicket, Long> implements FinanceTicketDao {
	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, FinanceTicketVO financeTicketVO) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("params", financeTicketVO);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.FinanceTicketDaoSqlImpl.search", params);
	}

	@Override
	public int searchCount(FinanceTicketVO financeTicketVO) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", financeTicketVO);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.FinanceTicketDaoSqlImpl.searchCount", params);
	}

	@Override
	public int addFinance(long serviceId, int returnType, long time,int resourceType,long yjjNumber) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("time", time);
		params.put("returnType", returnType);
		params.put("returnSource", resourceType);
		params.put("returnUser", String.valueOf(yjjNumber));
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.FinanceTicketDaoSqlImpl.addFinance", params);
	}

	@Override
	public int updateFinanceTicket(FinanceTicket financeTicket) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("params", financeTicket);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.FinanceTicketDaoSqlImpl.updateFinanceTicket", params);
	}

	@Override
	public int addFromRevoke(FinanceTicket financeTicket) {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.FinanceTicketDaoSqlImpl.addFromRevoke", financeTicket);
	}
}
