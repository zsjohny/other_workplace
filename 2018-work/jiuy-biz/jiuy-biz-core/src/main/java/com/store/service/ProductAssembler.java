package com.store.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.UserDetail;
import com.store.entity.Brand;
import com.store.entity.ProductComposite;
import com.store.entity.ProductVOShop;
import com.store.service.brand.ShopBrandService;

@Service
public class ProductAssembler {

    @Autowired
    private ProductServiceShop productService;
    
    @Autowired
    private ShopBrandService shopBrandService;

    public void assemble(Collection<? extends ProductComposite> composites, UserDetail userDetail) {
        if (composites.isEmpty()) {
            return;
        }

        Set<Long> ids = new HashSet<Long>();
        for (ProductComposite composite : composites) {
            ids.add(composite.getProductId());
        }

        Map<Long, ProductVOShop> map = productService.getProductMap(ids);
        
        Set<Long> brandIds = new HashSet<Long>();
        for (Map.Entry<Long, ProductVOShop> entry : map.entrySet()) {
        	brandIds.add(entry.getValue().getBrandId());
        }
        Map<Long, Brand> brandMap = shopBrandService.getBrands();

        for (ProductComposite composite : composites) {
        	ProductVOShop product = map.get(composite.getProductId());
            if(product != null){
            	if(brandMap.get(product.getBrandId()) != null){
            		product.setBrand(brandMap.get(product.getBrandId()).toSimpleMap());
            	}
            	composite.assemble(product);
            }
        }
    }
}
