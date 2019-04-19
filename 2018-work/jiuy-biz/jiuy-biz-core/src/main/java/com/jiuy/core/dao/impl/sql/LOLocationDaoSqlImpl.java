package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.LOLocationDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.logistics.LOLocation;

public class LOLocationDaoSqlImpl extends SqlSupport implements LOLocationDao {

	@Override
	public List<LOLocation> search(int type) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("type", type);

		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.LOLocationDaoSqlImpl.search", params);
	}

	@Override
	public long OnDuplicateKeyUpd(LOLocation lOLocation) {
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.LOLocationDaoSqlImpl.OnDuplicateKeyUpd", lOLocation);
	}

	@Override
	public LOLocation getById(long deliveryLocation) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", deliveryLocation);

		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.LOLocationDaoSqlImpl.getById", params);
	}

}
