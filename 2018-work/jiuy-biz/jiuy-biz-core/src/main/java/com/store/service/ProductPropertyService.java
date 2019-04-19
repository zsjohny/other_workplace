package com.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.ProductProp;
import com.jiuyuan.service.common.MemcachedService;
import com.store.dao.mapper.ProductPropertyMapper;



@Service
public class ProductPropertyService {

    @Autowired
    private ProductPropertyMapper productPropertyMapper;
    
    @Autowired
    private MemcachedService memcachedService; 

    @SuppressWarnings("unchecked")
	public List<ProductProp> getOrderedProductProperties(long productId) {
        String groupKey = MemcachedKey.GROUP_KEY_PRODUCT_PROPERTY;
        
        String key = productId + "";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<ProductProp>) obj;
        } else {
        	List<ProductProp> productProps = productPropertyMapper.getOrderedProductProperties(productId);
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, productProps);
        	
        	return productProps;
        }
    }

    public Map<Long, ProductProp> getYearProps() {
    	return productPropertyMapper.getPropsByNameId(10);
    }
    
    public Map<Long, ProductProp> getSeasonProps() {
    	return productPropertyMapper.getPropsByNameId(9);
    }
}
