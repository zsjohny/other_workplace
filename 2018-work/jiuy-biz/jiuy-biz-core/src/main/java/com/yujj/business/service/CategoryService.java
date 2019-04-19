package com.yujj.business.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.dao.mapper.CategoryMapper;
import com.yujj.entity.product.Category;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private MemcachedService memcachedService;

    public List<Category> getCategoryTree(boolean onlyNormal) {
        List<Category> result = new ArrayList<Category>();
        Map<Long, Category> categoryMap = new HashMap<Long, Category>();
        List<Category> categories = getCategories();
        for (Iterator<Category> it = categories.iterator(); it.hasNext();) {
            Category cate = it.next();
            if (onlyNormal && cate.getStatus() != 0) {
                it.remove();
                continue;
            }

            categoryMap.put(cate.getId(), cate);
            if (cate.getParentId() == 0) {
                result.add(cate);
                it.remove();
            }
        }

        for (Category cate : categories) {
            Category parentCate = categoryMap.get(cate.getParentId());
            if (parentCate != null) {
                parentCate.getChildCategories().add(cate);
            }
        }

        return result;
    }

    public Map<Long, Category> getCategoryTreeMap(boolean onlyNormal) {
        Map<Long, Category> map = new HashMap<Long, Category>();
        List<Category> categories = getCategoryTree(onlyNormal);
        for (Category category : categories) {
            map.putAll(getCategoryTreeMap(category));
        }
        return map;
    }

    private Map<Long, Category> getCategoryTreeMap(Category category) {
        Map<Long, Category> map = new HashMap<Long, Category>();
        map.put(category.getId(), category);
        if (!category.getChildCategories().isEmpty()) {
            for (Category cate : category.getChildCategories()) {
                map.putAll(getCategoryTreeMap(cate));
            }
        }
        return map;
    }

    public Category getCategoryById(long categoryId) {
        return getCategoryTreeMap(false).get(categoryId);
    }
    
 
    
    public List<Category> getCategoriesByIdsArr(String [] arr) {
    	return categoryMapper.getCategoriesByIdsArr( arr);
    }

    @SuppressWarnings("unchecked")
    public List<Category> getCategories() {
        String groupKey = MemcachedKey.GROUP_KEY_CATEGORY;
        String key = "all";
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (List<Category>) obj;
        }

        List<Category> categories = categoryMapper.getCategories();
        if (CollectionUtils.isNotEmpty(categories)) {
            memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, categories);
        }

        return categories;
    }
    
    public List<Category> getParentCategories() {
    	String groupKey = MemcachedKey.GROUP_KEY_CATEGORY;
    	String key = "parent";
    	Object obj = memcachedService.get(groupKey, key);
    	if (obj != null) {
    		return (List<Category>) obj;
    	}
    	
    	List<Category> categories = categoryMapper.getParentCategories();
    	if (CollectionUtils.isNotEmpty(categories)) {
    		memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, categories);
    	}
    	
    	return categories;
    }
}
