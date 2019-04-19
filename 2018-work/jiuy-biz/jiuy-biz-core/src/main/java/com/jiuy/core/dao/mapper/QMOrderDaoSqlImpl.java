package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.qianmi.QMOrder;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Repository
public class QMOrderDaoSqlImpl implements QMOrderDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public QMOrder add(QMOrder qmOrder) {
		sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.QMOrderDaoSqlImpl.add", qmOrder);
		return  qmOrder;
	}

	@Override
	public List<QMOrder> search(Integer orderStatus, Long mergedId, String sortSql) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderStatus", orderStatus);
		params.put("mergedId", mergedId);
		params.put("sortSql", sortSql);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.QMOrderDaoSqlImpl.search", params);
	}

	@Override
	public int update(Long orderNo, String tid, Long mergedId) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNo", orderNo);
		params.put("tid", tid);
		params.put("mergedId", mergedId);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.QMOrderDaoSqlImpl.update", params);
	}

	@Override
	public int batchUpdate(Collection<Long> orderNos, Long mergedId, Long pushTime, Integer orderStatus) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", orderNos);
		params.put("mergedId", mergedId);
		params.put("pushTime", pushTime);
		params.put("orderStatus", orderStatus);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.QMOrderDaoSqlImpl.batchUpdate", params);
	}

	@Override
	public List<QMOrder> getUnpushedMergedQMOrders(Long startTime, Long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.QMOrderDaoSqlImpl.getUnpushedMergedQMOrders", params);
	}

	@Override
	public List<QMOrder> search(Collection<Long> mergedIds, Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("mergedIds", mergedIds);
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.QMOrderDaoSqlImpl.search", params);
	}

	
}
