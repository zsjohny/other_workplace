package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.brandorder.BrandOrderItem;

/**
 * @author jeff.zhan
 * @version 2017年1月3日 上午11:15:27
 * 
 */

@Repository
public class BrandOrderItemDaoSqlImpl implements BrandOrderItemDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<BrandOrderItem> add(List<BrandOrderItem> brandOrderItems) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("brandOrderItems", brandOrderItems);
		
		sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.BrandOrderItemDaoSqlImpl.add", params);
		return brandOrderItems;
	}
	
}
