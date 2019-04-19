package com.store.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.service.common.MemcachedService;
import com.store.dao.mapper.CategoryMapper;
import com.store.entity.ShopCategory;


@Service
public class ShopCategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private MemcachedService memcachedService;

    @SuppressWarnings("unchecked")
    public List<ShopCategory> getCategories() {
        String groupKey = MemcachedKey.GROUP_KEY_CATEGORY;
        String key = "all";
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (List<ShopCategory>) obj;
        }

        List<ShopCategory> categories = categoryMapper.getCategories();
        if (CollectionUtils.isNotEmpty(categories)) {
            memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, categories);
        }

        return categories;
    }

	public List<ShopCategory> getCategoriesByIdsArr(String [] arr) {
    	return categoryMapper.getCategoriesByIdsArr( arr);
    }
    public List<ShopCategory> getParentCategories() {
    	String groupKey = MemcachedKey.GROUP_KEY_CATEGORY;
    	String key = "parent";
    	Object obj = memcachedService.get(groupKey, key);
    	if (obj != null) {
    		return (List<ShopCategory>) obj;
    	}
    	
    	List<ShopCategory> categories = categoryMapper.getParentCategories();
    	if (CollectionUtils.isNotEmpty(categories)) {
    		memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, categories);
    	}
    	
    	return categories;
    }
    
    public List<ShopCategory> getAllParentCategories() {
    	List<ShopCategory> categories = categoryMapper.getAllParentCategories();
    	return categories;
    }

	public List<ShopCategory> getChildCategoryByParentId(long parentId) {
    	List<ShopCategory> childCategories = categoryMapper.getChildCategoryByParentId(parentId);
    	return childCategories;
	}
}