package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.RoleDao;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.admin.AdminUserVO;
import com.jiuy.core.meta.admin.Role;
import com.jiuy.core.service.admin.AdminUserService;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Service
public class AdminUserFacade {
	
	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private RoleDao roleDao;

	public List<AdminUserVO> search(PageQuery query, Long userId, String userName, Long roleId, String userPhone) {
		Map<Long, Role> map = roleDao.roleOfId();
		List<AdminUserVO> adminUserVOs = new ArrayList<>();
		List<AdminUser> adminUsers = adminUserService.search(query, userId, userName, roleId, userPhone);
		for (AdminUser adminUser : adminUsers) {
			AdminUserVO adminUserVO = new AdminUserVO();
			BeanUtils.copyProperties(adminUser, adminUserVO);
		
			Role role = map.get(adminUser.getRoleId());
			adminUserVO.setRole(role);
			
			adminUserVOs.add(adminUserVO);
		}
		
		return adminUserVOs;
	}
}
