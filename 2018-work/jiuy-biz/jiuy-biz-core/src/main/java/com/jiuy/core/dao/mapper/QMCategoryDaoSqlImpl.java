package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.qianmi.QMCategory;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Repository
public class QMCategoryDaoSqlImpl implements QMCategoryDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public QMCategory search(Long categoryId) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("categoryId", categoryId);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.QMCategoryDaoSqlImpl.search", params);
	}

	@Override
	public QMCategory add(QMCategory qmCategory) {
		sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.QMCategoryDaoSqlImpl.add", qmCategory);
		return qmCategory;
	}
	
	
}
