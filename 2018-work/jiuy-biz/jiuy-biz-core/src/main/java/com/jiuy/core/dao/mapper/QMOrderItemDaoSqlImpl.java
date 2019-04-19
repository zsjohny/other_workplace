package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.qianmi.QMOrderItem;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Repository
public class QMOrderItemDaoSqlImpl implements QMOrderItemDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int batchAdd(List<QMOrderItem> qmOrderItems) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("qmOrderItems", qmOrderItems);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.QMOrderItemDaoSqlImpl.batchAdd", params);
	}

	@Override
	public List<QMOrderItem> search(String tid) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("tid", tid);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.QMOrderItemDaoSqlImpl.search", params);
	}
}
