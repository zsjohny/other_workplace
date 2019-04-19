package com.jiuy.core.service.admin;

import java.util.List;

import com.jiuy.core.meta.admin.Role;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface RoleService {

	int searchCount(String name);

	List<Role> search(PageQuery pageQuery, String name);

}
