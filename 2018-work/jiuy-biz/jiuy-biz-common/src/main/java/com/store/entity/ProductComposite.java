package com.store.entity;

import com.jiuyuan.entity.Product;

public interface ProductComposite {

    long getProductId();
    
    void assemble(Product product);
    

}
