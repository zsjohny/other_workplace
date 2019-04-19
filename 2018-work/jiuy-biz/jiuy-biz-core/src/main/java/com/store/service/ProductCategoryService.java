package com.store.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.ProductCategory;
import com.store.dao.mapper.ProductCategoryMapper;
import com.store.entity.ProductCategoryVO;
import com.store.entity.ShopCategory;
import com.store.entity.ShopCategoryV0;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;


@Service
public class ProductCategoryService {
	
	private static final Log logger = LogFactory.get();

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

	public Map<Long, ShopCategory> getProductVirtualCategory(Collection<Long> productIds) {
		Map<Long, ShopCategory> map = new HashMap<Long, ShopCategory>();
		if(productIds.size() == 0) {
			return map;
		}
		List<ShopCategoryV0> categoryV0s = productCategoryMapper.getProductVirtualCategory(productIds);
		
		for(ShopCategoryV0 caV0 : categoryV0s) {
			map.put(caV0.getProductId(), caV0);
		}
		
		return map;
	}
	
    public  List<ProductCategory> getProductCategoryListByProductIds(Collection<Long> productIds){
    	return productCategoryMapper.getProductCategoryListByProductIds(productIds);
    }


	public Map<Long, ProductCategoryVO> getProductCategorys() {	
		return productCategoryMapper.getProductCategorys();
	}
}