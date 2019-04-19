package com.jiuy.core.service.admin;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.admin.Authority;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface AuthorityService {

	List<Authority> search(Collection<Long> ids);

	Collection<Authority> getTree(Collection<Authority> authorities);

	Collection<Authority> getByParentIdRoleId(Long parentId, Long roleId);

}
