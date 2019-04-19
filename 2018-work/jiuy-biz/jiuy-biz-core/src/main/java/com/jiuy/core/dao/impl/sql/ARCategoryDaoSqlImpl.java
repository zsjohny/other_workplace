package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.ARCategoryDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.artical.ARCategoryVO;
import com.jiuyuan.entity.article.ARCategory;
import com.jiuyuan.entity.query.PageQuery;

public class ARCategoryDaoSqlImpl extends DomainDaoSqlSupport<ARCategory, Long> implements ARCategoryDao {

	@Override
	public ARCategory addcat(ARCategory arCategory) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.ARCategoryDaoSqlImpl.addcat", arCategory);
		
		return arCategory;
	}

	@Override
	public int rmCategory(long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ARCategoryDaoSqlImpl.rmCategory", params);
	}

	@Override
	public int updateCategory(ARCategory arCategory) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ARCategoryDaoSqlImpl.updateCategory", arCategory);
	}

	@Override
	public List<ARCategoryVO> searchCat(PageQuery query, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("query", query);
		params.put("name", name);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ARCategoryDaoSqlImpl.searchCat", params);
	}

	@Override
	public int searchCatCount(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ARCategoryDaoSqlImpl.searchCatCount", params);
	}

	@Override
	public List<ARCategory> loadParentCat() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ARCategoryDaoSqlImpl.loadParentCat");
	}

	@Override
	public List<Long> getSubCats(long parentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("parentId", parentId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ARCategoryDaoSqlImpl.getSubCats", params);
	}


}
