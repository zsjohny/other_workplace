package com.yujj.business.assembler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.business.assembler.composite.ProductComposite;
import com.yujj.business.service.BrandService;
import com.yujj.business.service.ProductService;
import com.yujj.entity.Brand;
import com.yujj.entity.product.Product;

@Service
public class ProductAssembler {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private BrandService brandService;

    public void assemble(Collection<? extends ProductComposite> composites) {
        if (composites.isEmpty()) {
            return;
        }

        Set<Long> ids = new HashSet<Long>();
        for (ProductComposite composite : composites) {
            ids.add(composite.getProductId());
        }

        Map<Long, Product> map = productService.getProductMap(ids);
        
        Set<Long> brandIds = new HashSet<Long>();
        for (Map.Entry<Long, Product> entry : map.entrySet()) {
        	brandIds.add(entry.getValue().getBrandId());
        }
        
        if (brandIds.isEmpty()) return;
        
        Map<Long, Brand> brandMap = brandService.getBrandMap(brandIds);

        for (ProductComposite composite : composites) {
            Product product = map.get(composite.getProductId());
            if(product != null){
            	
            	Brand brand = brandMap.get(product.getBrandId());
            	product.setBrand(brand);
            	composite.assemble(product);
            }
        }
    }
}
