package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreServiceTicketDao;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicket;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicketVO;
@Repository
public class StoreServiceTicketDaoSqlImpl implements StoreServiceTicketDao{
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int updateServiceTicket(long serviceId, int status, String message, long time) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("status", status);
		params.put("examineMemo", message);
		params.put("examineTime", time);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.updateServiceTicket", params);
	}

	@Override
	public int searchCount(StoreServiceTicketVO serviceTicket) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("params", serviceTicket);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, StoreServiceTicketVO serviceTicket) {
		Map<String, Object> params = new  HashMap<String, Object>();
		params.put("pageQuery", pageQuery);
		params.put("params", serviceTicket);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.search", params);
	}

	@Override
	public StoreServiceTicket ServiceTicketOfId(long serviceId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.ServiceTicketOfId", params);
	}

	@Override
	public int updateServiceTicket(long serviceId, int processResult, double processMoney, double processExpressMoney,
			String message, long time, int status) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("processResult", processResult);
		params.put("processMoney", processMoney);
		params.put("processExpressMoney", processExpressMoney);
		params.put("processReturnMemo", message);
		params.put("processTime", time);
		params.put("status", status);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.updateServiceTicket", params);
	}

	@Override
	public int updateServiceTicket(long serviceId, long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("orderNo", orderNo);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.updateServiceTicket", params);
	}
	
	@Override
	public int updateServiceTicket(long serviceId, int processResult, double sellerExpressMoney,
			String sellerExpressCom, String sellerExpressNo, String message, long time, int status) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("processResult", processResult);
		params.put("sellerExpressMoney", sellerExpressMoney);
		params.put("sellerExpressCom", sellerExpressCom);
		params.put("sellerExpressNo", sellerExpressNo);
		params.put("sellerMemo", message);
		params.put("sellerTime", time);
		params.put("status", status);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.updateServiceTicket", params);
	}

	@Override
	public List<StoreServiceTicket> getByNotStatus(List<Integer> status_list) {
		Map<String, Object> params = new  HashMap<String, Object>();
		params.put("status_list", status_list);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.getByNotStatus", params);
	}

	@Override
	public int addFromRevoke(StoreServiceTicket storeServiceTicket) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreServiceTicketDaoSqlImpl.addFromRevoke",storeServiceTicket);
	}
}
