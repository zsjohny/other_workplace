package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.qianmi.QMProductSKU;

/**
 * @author jeff.zhan
 * @version 2016年10月21日 下午7:05:01
 * 
 */
@Repository
public class QMProductSKUDaoSqlImpl implements QMProductSKUDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int batchAdd(Collection<QMProductSKU> qmProductSKUs) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("qmProductSKUs", qmProductSKUs);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.QMProductSKUDaoSqlImpl.batchAdd", params);
	}
}
