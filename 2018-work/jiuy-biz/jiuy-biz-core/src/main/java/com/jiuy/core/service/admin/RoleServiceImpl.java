package com.jiuy.core.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.RoleDao;
import com.jiuy.core.meta.admin.Role;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleDao roleDao;

	@Override
	public int searchCount(String name) {
		return roleDao.searchCount(name);
	}

	@Override
	public List<Role> search(PageQuery pageQuery, String name) {
		return roleDao.search(pageQuery, name);
	}
}
