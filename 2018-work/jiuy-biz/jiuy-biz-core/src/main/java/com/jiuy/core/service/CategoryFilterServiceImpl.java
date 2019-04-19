package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.CategoryFilterDao;
import com.jiuyuan.entity.product.CategoryFilter;

@Service
public class CategoryFilterServiceImpl implements CategoryFilterService {
	
	@Autowired
	private CategoryFilterDao categoryFilterDao;

	@Override
	public int add(List<CategoryFilter> categoryFilters) {
		if (categoryFilters.size() < 1) {
			return 0;
		}
		return categoryFilterDao.add(categoryFilters);
	}

	@Override
	public int delete(Long categoryId) {
		return categoryFilterDao.delete(categoryId);
	}

	@Override
	public List<Map<String, Object>> getFilterInfo(Long id, int type) {
		return categoryFilterDao.getFilterInfo(id, type);
	}

	@Override
	public List<CategoryFilter> search(Collection<Long> categoryIds, int type, String sort) {
		if (categoryIds.size() < 1) {
			return new ArrayList<>();
		}
		return categoryFilterDao.search(categoryIds, type, sort);
	}

}
