package com.jiuy.core.dao.mapper;

import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.admin.Role;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface RoleDao {

	int searchCount(String name);

	List<Role> search(PageQuery pageQuery, String name);

	int update(Long id, String name, String description);

	int update(Long id, Integer status);

	int add(String name, String description);

	Map<Long, Role> roleOfId();

	List<Role> getByName(String name);

}
