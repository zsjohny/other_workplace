package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.modelv2.ProductCategoryMapper;
import com.jiuyuan.entity.ProductCategory;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	
	@Autowired
	private ProductCategoryMapper productCategoryMapper;

	@Override
	public Map<Long, String> erpCatByProductId(Collection<Long> productIds) {
		Map<Long, String> erpCatByProductId = new HashMap<Long, String>();
		
		List<Map<String, Object>> list = productCategoryMapper.getErpCat();
		for(Map<String, Object> map : list) {
			long productId = Long.parseLong(map.get("ProductId").toString());
			String categoryName = (String) map.get("CategoryName");

			if(StringUtils.equals(null, categoryName)) {
				erpCatByProductId.put(productId, "无");
			} else {
				erpCatByProductId.put(productId, categoryName);
			}
		}
		
		return erpCatByProductId;
	}

	@Override
	public Map<Long, List<ProductCategory>> itemsOfCategoryIdMap(Collection<Long> categoryIds) {
		if(categoryIds.size() < 1) {
			return new HashMap<Long, List<ProductCategory>>();
		}
		
		List<ProductCategory> productCategories = productCategoryMapper.itemsOfCategoryIds(categoryIds);
		
		Map<Long, List<ProductCategory>> itemsOfCategoryIdMap = new HashMap<Long, List<ProductCategory>>();
		long lastCategoryId = 0;
		List<ProductCategory> productCategoriesList = null;
		for(ProductCategory productCategory : productCategories) {
			long categoryId = productCategory.getCategoryId();
			if(lastCategoryId != categoryId) {
				lastCategoryId = categoryId;
				productCategoriesList = new ArrayList<ProductCategory>();
				itemsOfCategoryIdMap.put(lastCategoryId, productCategoriesList);
			}
			productCategoriesList.add(productCategory);
		}
		
		return itemsOfCategoryIdMap;
	}

	@Override
	public List<ProductCategory> itemsOfCategoryIds(Collection<Long> categoryIds) {
		if(categoryIds.size() < 1) {
			return new ArrayList<ProductCategory>();
		}
		
		return productCategoryMapper.itemsOfCategoryIds(categoryIds);
	}

	@Override
	public List<ProductCategory> search(Long productId) {
		return productCategoryMapper.search(productId);
	}
	
}
