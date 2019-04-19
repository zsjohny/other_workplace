package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.RoleAuthorityDao;
import com.jiuy.core.meta.admin.Authority;
import com.jiuy.core.meta.admin.Role;
import com.jiuy.core.meta.admin.RoleAuthority;
import com.jiuy.core.meta.admin.RoleVO;
import com.jiuy.core.service.admin.AdminUserService;
import com.jiuy.core.service.admin.AuthorityService;
import com.jiuy.core.service.admin.RoleService;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Service
public class RoleFacade {

	@Autowired
	private RoleAuthorityDao roleAuthorityDao;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AdminUserService adminUserService;
	

	public Collection<Authority> loadAuthority(Long roleId) {
		List<Authority> authorities = authorityService.search(null);
		if (roleId == null) {
			return authorityService.getTree(authorities);
		}

		Map<Long, Authority> allMap = new HashMap<>();

		for (Authority authority : authorities) {
			allMap.put(authority.getId(), authority);
		}

		List<RoleAuthority> roleAuthorities = roleAuthorityDao.getAuthority(roleId);

		for (RoleAuthority roleAuthority : roleAuthorities) {
			//设置所有上级权限为选中
			Authority auth = allMap.get(roleAuthority.getAuthorityId());
			
			if (auth != null) {
				auth.setSelected(1);
				auth.setDisplayed(1);
				
				Authority parentAuth = allMap.get(auth.getParentId());
				while (parentAuth != null && parentAuth.getDisplayed() != 1) {
					parentAuth.setDisplayed(1);
					parentAuth = allMap.get(parentAuth.getParentId());
				}
			}
		}

		return authorityService.getTree(authorities);
	}

	public List<RoleVO> search(PageQuery pageQuery, String name) {
		Map<Long, Integer> map = adminUserService.getCountOfRole();
		
		List<Role> roles = roleService.search(pageQuery, name);
		List<RoleVO> roleVOs = new ArrayList<>();
		for (Role role : roles) {
			RoleVO roleVO = new RoleVO();
			BeanUtils.copyProperties(role, roleVO);
			
			Integer count = map.get(role.getId());
			roleVO.setCount(count == null ? 0 : count);
			
			roleVOs.add(roleVO);
		}
		
		return roleVOs;
	}

	public Collection<Authority> getSubAuthorities(Long roleId, Long parentId) {
		return authorityService.getByParentIdRoleId(parentId, roleId);
	}
	
}