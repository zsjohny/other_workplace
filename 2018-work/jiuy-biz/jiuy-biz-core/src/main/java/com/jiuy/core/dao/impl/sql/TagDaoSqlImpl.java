package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.TagDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.entity.product.Tag;
import com.jiuyuan.entity.query.PageQuery;

public class TagDaoSqlImpl extends SqlSupport implements TagDao {

	@Override
	public int addCount(Long groupId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("groupId", groupId);
		params.put("updateTime", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.addCount", params);
	}

	@Override
	public Tag add(Tag tag) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.add", tag);
		return tag;
	}

	@Override
	public int searchCount(String name, Integer countMin, Integer countMax, Integer productCountMin,
			Integer productCountMax, Collection<Long> groupIds, Integer isGroup,Integer isTop) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("countMin", countMin);
		params.put("countMax", countMax);
		params.put("productCountMin", productCountMin);
		params.put("productCountMax", productCountMax);
		params.put("groupIds", groupIds);
		params.put("isGroup", isGroup);
		params.put("status", 0);
		params.put("isTop", isTop);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<Tag> search(PageQuery pageQuery, String name, Integer countMin, Integer countMax, Integer productCountMin,
			Integer productCountMax, Collection<Long> groupIds, Integer isGroup, Integer isTop) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("name", name);
		params.put("countMin", countMin);
		params.put("countMax", countMax);
		params.put("productCountMin", productCountMin);
		params.put("productCountMax", productCountMax);
		params.put("groupIds", groupIds);
		params.put("isGroup", isGroup);
		params.put("status", 0);
		params.put("isTop", isTop);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.search", params);
	}

	@Override
	public int update(Tag tag) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.update", tag);
	}

	@Override
	public int update(int status, Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status", status);
		params.put("id", id);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.update", params);
	}

	@Override
	public List<Tag> search(Long groupId, String name, int isTop) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("groupId", groupId);
		params.put("status", 0);
		params.put("isTop", isTop);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.search", params);
	}

	@Override
	public List<Tag> search() {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status", 0);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.search", params);
	}

	@Override
	public Map<Long, Tag> itemsOfIds(Collection<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", ids);
		params.put("status", 0);
		
		return getSqlSession().selectMap("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.search", params, "id");
	}

	@Override
	public List<Tag> searchWithChild() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.searchWithChild");
	}

	@Override
	public int updTagTop(long tagId, long isTop,long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("tagId", tagId);
		params.put("isTop", isTop);
		params.put("updateTime", updateTime);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.TagDaoSqlImpl.updTagTop", params);
	}

}
