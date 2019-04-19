package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.admin.Authority;
import com.jiuy.core.meta.admin.RoleAuthority;

public interface RoleAuthorityDao {

	int add(Collection<RoleAuthority> roleAuthorities);

	int remove(long id);

	List<RoleAuthority> getAuthority(Long roleId);

	RoleAuthority search(Long roleId, Long authorityId);

	RoleAuthority findAuthority(Long roleId, String url);

}
