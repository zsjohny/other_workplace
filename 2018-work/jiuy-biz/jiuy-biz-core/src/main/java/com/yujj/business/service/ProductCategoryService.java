package com.yujj.business.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.product.CategoryV0;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.dao.mapper.ProductCategoryMapper;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.ProductCategory;
import com.yujj.entity.product.ProductCategoryVO;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    public int getProductCount(Collection<Long> categoryIds) {
        return productCategoryMapper.getProductCount(categoryIds);
    }
    
    public List<CategoryV0> getProductVirtualCategoryList(Collection<Long> productIds) {
    	return productCategoryMapper.getProductVirtualCategory(productIds);
    }

    public List<Long> getProductIds(Collection<Long> categoryIds, PageQuery pageQuery) {
        return productCategoryMapper.getProductIds(categoryIds, pageQuery);
    }
    
    public List<Long> getCategoryIdsAll(Collection<Long> productIds) {
    	return productCategoryMapper.getCategoryIdsAll(productIds);
    }
    
    public  List<ProductCategory> getProductCategoryListByProductIds(Collection<Long> productIds){
    	return productCategoryMapper.getProductCategoryListByProductIds(productIds);
    }

	public Map<Long, Category> getProductVirtualCategory(Collection<Long> productIds) {
		Map<Long, Category> map = new HashMap<Long, Category>();
		if(productIds.size() == 0) {
			return map;
		}
		List<CategoryV0> categoryV0s = productCategoryMapper.getProductVirtualCategory(productIds);
		
		for(CategoryV0 caV0 : categoryV0s) {
			map.put(caV0.getProductId(), caV0);
		}
		
		return map;
	}

	public Map<Long, ProductCategoryVO> getProductCategorys() {	
		return productCategoryMapper.getProductCategorys();
	}
}
