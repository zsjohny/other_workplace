package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.LOPostageDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.logistics.LOLPostageVO;

public class LOPostageDaoSqlImpl extends SqlSupport implements LOPostageDao {

	@Override
	public List<LOLPostageVO> srchLogistics(int deliveryLocation) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("deliveryLocation", deliveryLocation);
		
        return getSqlSession().selectList("LOPostageMapper.srchLogistics", params);
	}

	@Override
	public int savePostage(int id, double postage) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("postage", postage);
		
        return getSqlSession().update("LOPostageMapper.savePostage", params);
	}

}
