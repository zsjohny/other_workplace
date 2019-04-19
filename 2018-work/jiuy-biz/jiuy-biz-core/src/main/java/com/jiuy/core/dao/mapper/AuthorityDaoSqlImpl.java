package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.admin.Authority;
import com.jiuy.core.meta.admin.AuthorityVO;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Repository
public class AuthorityDaoSqlImpl implements AuthorityDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<Authority> search(Collection<Long> ids) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("ids", ids);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.search", params);
	}

	@Override
	public int add(Authority authority) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.add", authority);
	}

	@Override
	public int remove(Long id) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", id);
		params.put("status", -1);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.update", params);
	}

	@Override
	public int searchCount(Long parentId, String moduleName) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("parentId", parentId);
		params.put("moduleName", moduleName);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<Authority> search(PageQuery pageQuery, Long parentId, String moduleName) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("parentId", parentId);
		params.put("pageQuery", pageQuery);
		params.put("moduleName", moduleName);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.search", params);
	}

	@Override
	public int update(Authority authority) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", authority.getId());
		params.put("moduleName", authority.getModuleName());
		params.put("parentId", authority.getParentId());
		params.put("url", authority.getUrl());
		params.put("menuName", authority.getMenuName());
		params.put("description", authority.getDescription());
		params.put("weight", authority.getWeight());
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.update", params);
	}

	@Override

	public Authority searchOne(String url) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("url", url);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.searchOne", params);
	}

	public Authority search(String uri) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("url", uri);
		params.put("limit", 1);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.search", params);

	}

	@Override
	public List<AuthorityVO> searchVO(PageQuery pageQuery, Long parentId, String moduleName) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("parentId", parentId);
		params.put("pageQuery", pageQuery);
		params.put("moduleName", moduleName);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.searchVO", params);
	}

	@Override
	public Collection<Authority> getByParentIdRoleId(Long parentId, Long roleId) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("parentId", parentId);
		params.put("roleId", roleId);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.getByParentIdRoleId", params);
	}

	@Override
	public Authority containsUrl(String url) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("url", url);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.AuthorityDaoSqlImpl.containsUrl", params);
	}

}
