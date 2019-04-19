package com.jiuy.core.business.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.service.CategoryFilterService;
import com.jiuyuan.entity.product.CategoryFilter;

@Service
public class CategoryFiltersFacade {
	
	@Autowired
	private CategoryFilterService categoryFilterService;

	@Transactional(rollbackFor = Exception.class)
	public void add(Long categoryId, List<CategoryFilter> categoryFilters) {
		if (categoryId != null) {
			categoryFilterService.delete(categoryId);
		}
		
		categoryFilterService.add(categoryFilters);
	}
}

