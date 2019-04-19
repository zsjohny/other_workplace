package com.store.service.brand;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.BrandMapper;
import com.jiuyuan.service.common.MemcachedService;
import com.store.entity.Brand;

@Service
public class ShopBrandService {
	
	@Autowired
	private BrandMapper brandMapper;

    @Autowired
    private MemcachedService memcachedService;
    
    public Map<Long, Brand> getBrands() {
    	String groupKey = MemcachedKey.GROUP_KEY_PRODUCT;
    	String key = "brands";
    	
    	Object obj = memcachedService.get(groupKey, key);
    	if (obj != null) {
    		return (Map<Long, Brand>)obj;
    	}
    	
    	Map<Long, Brand> brands = brandMapper.getBrands();
    	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, brands);
    		
        return brands;
    }
    
    public Brand getBrand(Long brandId) {
        return getBrands().get(brandId);
	}

}
