package com.jiuy.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.product.CategoryFilter;


public interface CategoryFilterService {

	int add(List<CategoryFilter> categoryFilters);

	int delete(Long categoryId);

	List<Map<String, Object>> getFilterInfo(Long id, int type);

	List<CategoryFilter> search(Collection<Long> categoryIds, int type, String sort);

}
