package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.SubscriptDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.query.PageQuery;

public class SubscriptDaoSqlImpl extends DomainDaoSqlSupport<Subscript, Long> implements SubscriptDao{

	@Override
	public Subscript addSubscript(Subscript subscript) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.addSubscript",subscript);
		return subscript;
	}

	@Override
	public List<Subscript> getSubscripts() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.getSubscripts");
	}

	@Override
	public int getSubscriptIdByName(String name) {
		Integer id = getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.getIdByName");
		return id == null?-1:id;
	}

	@Override
	public Subscript getSubscriptById(int id) {
		return null;
	}

	/**
	 * 更新Subscript
	 */
	@Override
	public int updateSubscript(Subscript subscript) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.updateSubscript", subscript);
	}
	
	@Override
	public int remove(Collection<Long> subscriptIds) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("ids", subscriptIds);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.remove",params);
	}

	/**
	 * 搜索
	 */
	@Override
	public List<Subscript> search(String name,String description,Long productId, PageQuery pageQuery) {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("name", name);
		params.put("description", description);
		params.put("productId", productId);
		params.put("pageQuery", pageQuery);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.search",params);
	}

	@Override
	public int deleteByIds(Collection<Long> subscriptIds) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("ids", subscriptIds);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.deleteByIds",params);
	}

	@Override
	public int searchCount(String name, String description, Long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("description", description);
		params.put("productId", productId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.searchCount", params);
	}

	@Override
	public int updateProductSum(Long id, Integer productSum) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("productSum", productSum == null ? 0 : productSum);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.SubscriptDaoSqlImpl.updateProductSum", params);
	}

}
