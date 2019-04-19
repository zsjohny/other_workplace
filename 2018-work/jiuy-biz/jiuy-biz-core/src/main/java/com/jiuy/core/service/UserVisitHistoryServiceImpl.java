package com.jiuy.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.UserVisitHistoryDao;

@Service
public class UserVisitHistoryServiceImpl implements UserVisitHistoryService{
	
	@Autowired
	private UserVisitHistoryDao userVisitHistoryDao;

	@Override
	public Map<Long, Integer> visitCountById(Collection<Long> relatedIds, int type) {
		List<Map<String, Object>> list = userVisitHistoryDao.searchGroupCount(relatedIds, type);
		Map<Long, Integer> visitCountById = new HashMap<>();
		for (Map<String, Object> map : list) {
			visitCountById.put(Long.parseLong(map.get("RelatedId").toString()), Integer.parseInt(map.get("sumCount").toString()));
		}
		
		return visitCountById;
	}
}
