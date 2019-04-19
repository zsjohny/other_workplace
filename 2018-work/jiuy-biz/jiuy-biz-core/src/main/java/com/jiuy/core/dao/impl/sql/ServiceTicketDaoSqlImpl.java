package com.jiuy.core.dao.impl.sql;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.ServiceTicketDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.aftersale.ServiceTicketVO;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.query.PageQuery;

public class ServiceTicketDaoSqlImpl extends DomainDaoSqlSupport<ServiceTicket, Long> implements ServiceTicketDao {

	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, ServiceTicketVO serviceTicket) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("params", serviceTicket);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.search", params);
	}

	@Override
	public int searchCount(ServiceTicketVO serviceTicket) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("params", serviceTicket);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.searchCount", params);
	}

	@Override
	public int updateServiceTicket(long serviceId, int status, String message, long time) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("status", status);
		params.put("examineMemo", message);
		params.put("examineTime", time);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.updateServiceTicket", params);
	}

	@Override
	public int updateServiceTicket(long serviceId, int processResult, double processMoney, double processExpressMoney,
			int processReturnJiuCoin, String message, long time, int status) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("processResult", processResult);
		params.put("processMoney", processMoney);
		params.put("processExpressMoney", processExpressMoney);
		params.put("processReturnJiuCoin", processReturnJiuCoin);
		params.put("processReturnMemo", message);
		params.put("processTime", time);
		params.put("status", status);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.updateServiceTicket", params);
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
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.updateServiceTicket", params);
	}

	@Override
	public ServiceTicket ServiceTicketOfId(long serviceId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.ServiceTicketOfId", params);
	}

	@Override
	public int updateServiceTicket(long serviceId, long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("serviceId", serviceId);
		params.put("orderNo", orderNo);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.updateServiceTicket", params);
	}

    @Override
    public List<ServiceTicket> getItems(int status, int type) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("status", status);
        params.put("type", type);
        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.getItems",
            params);
    }

    @Override
    public int updateServiceTicket(Collection<Long> serviceIds, int status) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("serviceIds", serviceIds);
        params.put("status", status);
        params.put("exchangeReceivedTime", System.currentTimeMillis());
        return getSqlSession().update("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.updateServiceTicket", params);
    }

	@Override
	public List<ServiceTicket> getByStatus(List<Integer> status_list) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status_list", status_list);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.getByStatus", params);
	}

	@Override
	public List<ServiceTicket> getByNotStatus(List<Integer> status_list) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status_list", status_list);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.getByNotStatus", params);
	}

	@Override
	public int addFromRevoke(ServiceTicket serviceTicket) {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ServiceTicketDaoSqlImpl.addFromRevoke", serviceTicket);
	}

}
