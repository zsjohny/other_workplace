package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.ShopMemberOrderLogDao;
import com.jiuyuan.entity.order.ShopMemberOrderLog;

@Repository
public class ShopMemberOrderLogDaoSqlImpl implements ShopMemberOrderLogDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	@Override
	public int addShopMemberOrderLog(ShopMemberOrderLog shopMemberOrderLog) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("shopMemberOrderLog", shopMemberOrderLog);
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.ShopMemberOrderLogDaoSqlImpl.addShopMemberOrderLog", params);
	}
}