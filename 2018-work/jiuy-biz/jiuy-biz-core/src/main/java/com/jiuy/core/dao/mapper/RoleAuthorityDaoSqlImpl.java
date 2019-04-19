package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.admin.Authority;
import com.jiuy.core.meta.admin.RoleAuthority;

@Repository
public class RoleAuthorityDaoSqlImpl implements RoleAuthorityDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int add(Collection<RoleAuthority> roleAuthorities) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("roleAuthorities", roleAuthorities);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.RoleAuthorityDaoSqlImpl.add", params);
	}

	@Override
	public int remove(long roleId) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("roleId", roleId);
		params.put("status", -1);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.RoleAuthorityDaoSqlImpl.update", params);
	}

	@Override
	public List<RoleAuthority> getAuthority(Long roleId) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("roleId", roleId);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.RoleAuthorityDaoSqlImpl.getAuthority", params);
	}

	@Override
	public RoleAuthority search(Long roleId, Long authorityId) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("roleId", roleId);
		params.put("authorityId", authorityId);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.RoleAuthorityDaoSqlImpl.search", params);
	}

	@Override
	public RoleAuthority findAuthority(Long roleId, String url) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("roleId", roleId);
		params.put("url", url);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.RoleAuthorityDaoSqlImpl.findAuthority", params);
	}
	
}
