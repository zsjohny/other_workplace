package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.ProductTagDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.product.ProductTag;
import com.jiuyuan.entity.product.Tag;

public class ProductTagDaoSqlImpl extends SqlSupport implements ProductTagDao {

	@Override
	public List<Map<String, Object>> productCountOfTagMap(Collection<Long> tagIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("tagIds", tagIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductTagDaoSqlImpl.productCountOfTagMap", params);
	}

	@Override
	public List<ProductTag> search(Long tagId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("tagId", tagId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductTagDaoSqlImpl.search", params);
	}

	@Override
	public int add(Long productId, Collection<Long> tagIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productId", productId);
		params.put("tagIds", tagIds);
		params.put("now", System.currentTimeMillis());
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ProductTagDaoSqlImpl.add", params);
	}

	@Override
	public int delete(Long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productId", productId);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ProductTagDaoSqlImpl.delete", params);
	}

	@Override
	public List<Tag> tagsOfProductId(Long productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("productId", productId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductTagDaoSqlImpl.tagsOfProductId", params);
	}

	@Override
	public List<Map<String, Object>> productCountOfGroupTagMap(Collection<Long> tagIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("groupTagIds", tagIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductTagDaoSqlImpl.productCountOfGroupTagMap", params);
	}

	@Override
	public List<ProductTag> search(Collection<Long> tagIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("tagIds", tagIds);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ProductTagDaoSqlImpl.search", params);
	}

}
