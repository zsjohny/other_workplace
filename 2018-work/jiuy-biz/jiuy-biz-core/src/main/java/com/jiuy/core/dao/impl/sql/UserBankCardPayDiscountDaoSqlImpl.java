package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.Map;

import com.jiuy.core.dao.UserBankCardPayDiscountDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.UserBankCardPayDiscount;

public class UserBankCardPayDiscountDaoSqlImpl extends SqlSupport implements UserBankCardPayDiscountDao {

	@Override
	public UserBankCardPayDiscount search(long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNo", orderNo);
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.UserBankCardPayDiscountDaoSqlImpl.search", params);
	}
	
}
