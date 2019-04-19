package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreFinanceTicketDao;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicket;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicketVO;

@Repository
public class StoreFinanceTicketDaoSqlImpl implements StoreFinanceTicketDao{
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int searchCount(StoreFinanceTicketVO financeTicket) {
	Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", financeTicket);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreFinanceTicketDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, StoreFinanceTicketVO financeTicket) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageQuery", pageQuery);
		params.put("params", financeTicket);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreFinanceTicketDaoSqlImpl.search", params);
	}

	@Override
	public int updateWithDraw(StoreFinanceTicket financeTicket) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("params", financeTicket);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreFinanceTicketDaoSqlImpl.updateFinanceTicket", params);
	}

	@Override
	public int addFinance(long serviceId, int returnType, long time,int sourceType,long storeId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("time", time);
		params.put("returnType", returnType);
		params.put("returnSource", sourceType);
		params.put("returnUser", String.valueOf(storeId));
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreFinanceTicketDaoSqlImpl.addFinance", params);
	}

	@Override
	public int addFromRevoke(StoreFinanceTicket storeFinanceTicket) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreFinanceTicketDaoSqlImpl.addFromRevoke", storeFinanceTicket);
	}
}
