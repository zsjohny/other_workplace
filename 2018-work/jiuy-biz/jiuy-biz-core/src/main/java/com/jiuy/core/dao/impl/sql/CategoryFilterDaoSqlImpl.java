package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.CategoryFilterDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.product.CategoryFilter;

public class CategoryFilterDaoSqlImpl extends SqlSupport implements CategoryFilterDao {

	@Override
	public int add(List<CategoryFilter> categoryFilters) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categoryFilters", categoryFilters);
		params.put("now", System.currentTimeMillis());
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.CategoryFilterDaoSqlImpl.add", params);
	}

	@Override
	public int delete(Long categoryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categoryId", categoryId);
		params.put("now", System.currentTimeMillis());
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.CategoryFilterDaoSqlImpl.delete", params);
	}

	@Override
	public List<Map<String, Object>> getFilterInfo(Long id, int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categoryId", id);
		params.put("type", type);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryFilterDaoSqlImpl.getFilterInfo", params);
	}

	@Override
	public List<CategoryFilter> search(Collection<Long> categoryIds, Integer type, String sort) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("categoryIds", categoryIds);
		params.put("type", type);
		params.put("sort", sort);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CategoryFilterDaoSqlImpl.search", params);
	}


}
