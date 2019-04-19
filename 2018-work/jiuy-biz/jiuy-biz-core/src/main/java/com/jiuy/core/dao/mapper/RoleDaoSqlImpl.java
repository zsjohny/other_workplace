package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.admin.Role;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Repository
public class RoleDaoSqlImpl implements RoleDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int searchCount(String name) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("name", name);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.RoleDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<Role> search(PageQuery pageQuery, String name) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("pageQuery", pageQuery);
		params.put("name", name);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.RoleDaoSqlImpl.search", params);
	}

	@Override
	public int update(Long id, String name, String description) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", id);
		params.put("name", name);
		params.put("description", description);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.RoleDaoSqlImpl.update", params);
	}

	@Override
	public int update(Long id, Integer status) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", id);
		params.put("status", status);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.RoleDaoSqlImpl.update", params);
	}

	@Override
	public int add(String name, String description) {
		Map<String, Object> params = new HashMap<>();
		
		long time = System.currentTimeMillis();
		
		params.put("name", name);
		params.put("description", description);
		params.put("createTime", time);
		params.put("updateTime", time);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.RoleDaoSqlImpl.add", params);
	}

	@Override
	public Map<Long, Role> roleOfId() {
		return sqlSessionTemplate.selectMap("com.jiuy.core.dao.mapper.RoleDaoSqlImpl.roleOfId", "id");
	}

	@Override
	public List<Role> getByName(String name) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("name", name);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.RoleDaoSqlImpl.getByName", params);
	}
}
