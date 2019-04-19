package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.jiuy.core.dao.ArticalDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.artical.Artical;
import com.jiuy.core.meta.artical.ArticalVO;
import com.jiuyuan.entity.query.PageQuery;

public class ArticalDaoSqlImpl extends DomainDaoSqlSupport<Artical, Long> implements ArticalDao{


	@Override
	public List<ArticalVO> searchArtical(PageQuery query, String content, long aRCategoryId) {
		Map<String,Object> params = new HashMap<String,Object>();
		
	    params.put("query", query);
	    params.put("content", content);
	    params.put("aRCategoryId", aRCategoryId);
	    
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.searchArtical", params);
	}

	@Override
	public int searchArticalCount(String content, long aRCategoryId) {
		Map<String,Object> params = new HashMap<String,Object>();
		
	    params.put("content", content);
	    params.put("aRCategoryId", aRCategoryId);
	    
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.searchArticalCount", params);
	}

	@Override
	public Artical addArtical(Artical artical) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.addArtical", artical);
		
		return artical;
	}

	@Override
	public int updateArtical(Artical artical) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.updateArtical", artical);
	}

	@Override
	public ArticalVO loadCatById(long id) {
		Map<String,Object> params = new HashMap<String, Object>();
		
	    params.put("id", id);

	    return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.loadCatById", params);
	}

	@Override
	public int remove(long id) {
		Map<String,Object> params = new HashMap<String, Object>();
		
	    params.put("id", id);

	    return getSqlSession().update("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.remove", params);
	}

	@Override
	public int getCatARCount(Long catId) {
		Map<String,Object> params = new HashMap<String, Object>();
		
	    params.put("aRCategoryId", catId);

	    return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.getCatARCount", params);
	}

	@Override
	public List<ArticalVO> searchArticalByCat(PageQuery pageQuery, long aRCategoryId) {
		Map<String,Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("aRCategoryId", aRCategoryId);

	    return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.searchArticalByCat", params);
	}

	@Override
	public int searchArticalCountByCat(long aRCategoryId) {
		Map<String,Object> params = new HashMap<String, Object>();
		
		params.put("aRCategoryId", aRCategoryId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ArticalDaoSqlImpl.searchArticalCountByCat", params);
	}
	
}
