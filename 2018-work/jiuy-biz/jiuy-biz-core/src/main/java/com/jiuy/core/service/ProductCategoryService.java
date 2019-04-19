package com.jiuy.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.ProductCategory;


public interface ProductCategoryService {

	Map<Long, String> erpCatByProductId(Collection<Long> productIds);

	Map<Long, List<ProductCategory>> itemsOfCategoryIdMap(Collection<Long> categoryIds);

	List<ProductCategory> itemsOfCategoryIds(Collection<Long> categoryIds);

	List<ProductCategory> search(Long productId);

}
