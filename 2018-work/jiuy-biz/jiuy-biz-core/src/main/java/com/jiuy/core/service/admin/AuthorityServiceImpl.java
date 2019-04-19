package com.jiuy.core.service.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.mapper.AuthorityDao;
import com.jiuy.core.meta.admin.Authority;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityDao authorityDao;
	
	@Override
	public List<Authority> search(Collection<Long> ids) {
		if (ids != null && ids.size() < 1) {
			return new ArrayList<>();
		}
				
		return authorityDao.search(ids);
	}

	@Override
	public Collection<Authority> getTree(Collection<Authority> authorities) {
		
		Map<Long, Authority> allMap = new HashMap<>();
		Map<Long, Authority> parentAuthorityMap = new HashMap<>();
		for (Authority authority : authorities) {
			allMap.put(authority.getId(), authority);
			if (authority.getParentId() == 0) {
				parentAuthorityMap.put(authority.getId(), authority);
			}
		}
		
		for (Authority authority : authorities) {
			if (authority.getParentId() != 0) {
				Authority auth = allMap.get(authority.getParentId());
				if (auth != null) {					
					auth.getAuthorities().add(authority);
				}
			}
		}
		
		List<Authority> aList = new ArrayList<>();
		for (Authority authority : parentAuthorityMap.values()) {
			aList.add(authority);
		}
		
		//顶级排序
		Collections.sort(aList, (Authority a, Authority b) -> {
		    return a.compareTo(b);
		});
		
		//排序
		for (Authority authority : authorities) {
			if (authority.getAuthorities().size() > 1) {
				Collections.sort(authority.getAuthorities(), (Authority a, Authority b) -> {
				    return a.compareTo(b);
				});
			}
		}
		
		return aList;
	}

	@Override
	public Collection<Authority> getByParentIdRoleId(Long parentId, Long roleId) {
		return authorityDao.getByParentIdRoleId(parentId, roleId);
	}

	
}
