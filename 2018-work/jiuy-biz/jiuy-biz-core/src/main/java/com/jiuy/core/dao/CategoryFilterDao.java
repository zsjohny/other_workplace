package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.product.CategoryFilter;


public interface CategoryFilterDao {

	int add(List<CategoryFilter> categoryFilters);

	int delete(Long categoryId);

	List<Map<String, Object>> getFilterInfo(Long categoryId, int type);

	List<CategoryFilter> search(Collection<Long> categoryIds, Integer type, String sort);

}
