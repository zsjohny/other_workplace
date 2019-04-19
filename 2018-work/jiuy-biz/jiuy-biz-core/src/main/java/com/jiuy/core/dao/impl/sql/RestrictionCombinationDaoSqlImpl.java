package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.RestrictionCombinationDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.query.PageQuery;

public class RestrictionCombinationDaoSqlImpl extends SqlSupport implements RestrictionCombinationDao{

	@Override
	public List<RestrictionCombination> search(PageQuery pageQuery, String name) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("pageQuery", pageQuery);
		params.put("name", name);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.RestrictionCombinationDaoSqlImpl.search", params);
	}

	@Override
	public int add(RestrictionCombination restrictionCombination) {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.RestrictionCombinationDaoSqlImpl.add", restrictionCombination);
	}

	@Override
    public int update(RestrictionCombination restrictionCombination) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.RestrictionCombinationDaoSqlImpl.update", restrictionCombination);
	}

	@Override
	public int searchCount(String name) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("name", name);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.RestrictionCombinationDaoSqlImpl.searchCount", params);
	}

}
