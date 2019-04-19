package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.admin.Authority;
import com.jiuy.core.meta.admin.AuthorityVO;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface AuthorityDao {

	List<Authority> search(Collection<Long> ids);

	int add(Authority authority);

	int remove(Long id);

	int searchCount(Long parentId, String moduleName);

	List<Authority> search(PageQuery pageQuery, Long parentId, String moduleName);

	int update(Authority authority);

	Authority searchOne(String url);

	Authority search(String uri);

	List<AuthorityVO> searchVO(PageQuery pageQuery, Long parentId, String moduleName);

	Collection<Authority> getByParentIdRoleId(Long parentId, Long roleId);

	Authority containsUrl(String string);


}
