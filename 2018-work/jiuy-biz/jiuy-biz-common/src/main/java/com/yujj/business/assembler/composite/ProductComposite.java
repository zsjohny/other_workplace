package com.yujj.business.assembler.composite;

import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductSKU;

public interface ProductComposite {

    long getProductId();
    
    void assemble(Product product);
    

}
