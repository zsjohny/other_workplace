package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreProductDao;
import com.jiuyuan.entity.storeorder.StoreOrderItem;

/**
 * @author jeff.zhan
 * @version 2016年12月31日 下午10:19:58
 * 
 */

@Repository
public class StoreProductDaoSqlImpl implements StoreProductDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int insertStoreProduct(List<StoreOrderItem> storeOrderItems) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("storeOrderItems", storeOrderItems);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreProductDaoSqlImpl.insertStoreProduct", params);
	}
	
	
}
