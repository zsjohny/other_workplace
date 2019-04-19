package com.jiuy.core.service.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.mapper.RoleAuthorityDao;
import com.jiuy.core.meta.admin.RoleAuthority;

@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

	@Resource 
	private RoleAuthorityDao roleAuthorityDaoSqlImpl;
	

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(long roleId, List<Long> authorityIds) {
		roleAuthorityDaoSqlImpl.remove(roleId);
		
		if (authorityIds.size() < 1) {
			return 0;
		}
		
		long time = System.currentTimeMillis();
		
		List<RoleAuthority> roleAuthorities = new ArrayList<>();
		for (Long authorityId : authorityIds) {
			RoleAuthority roleAuthority = new RoleAuthority();
			roleAuthority.setAuthorityId(authorityId);
			roleAuthority.setRoleId(roleId);
			roleAuthority.setStatus(0);
			roleAuthority.setUpdateTime(time);
			roleAuthority.setCreateTime(time);
			
			roleAuthorities.add(roleAuthority);
		}
		
		return roleAuthorityDaoSqlImpl.add(roleAuthorities);
	}

	@Override
	public int remove(long roleId) {
		return roleAuthorityDaoSqlImpl.remove(roleId);
	}
	
}
