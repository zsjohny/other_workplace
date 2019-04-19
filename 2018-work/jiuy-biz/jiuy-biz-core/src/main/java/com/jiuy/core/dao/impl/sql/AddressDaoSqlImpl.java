package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.AddressDao;
import com.jiuy.core.dao.support.SqlSupport;
//import com.jiuyuan.entity.Address;
import com.jiuyuan.entity.account.Address;

public class AddressDaoSqlImpl extends SqlSupport implements AddressDao {

    @Override
    public List<Address> srchAddress(long userId, String expressInfo) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("userId", userId);
        params.put("expressInfo", expressInfo);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.AddressDaoSqlImpl.srchAddress", params);
    }

	@Override
	public List<Address> AddressOfUserIds(Collection<Long> userIds) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("userIds", userIds);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.AddressDaoSqlImpl.AddressOfUserIds", params);
	}
	
	@Override
	public List<Address> AddressOfUserIdsStore(Collection<Long> userIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("userIds", userIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.AddressDaoSqlImpl.AddressOfUserIdsStore", params);
	}

	@Override
	public List<Address> searchByUserId(long userId) {

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.AddressDaoSqlImpl.searchByUserId", userId);
	}

}
