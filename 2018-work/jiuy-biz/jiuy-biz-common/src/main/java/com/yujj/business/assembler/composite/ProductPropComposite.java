package com.yujj.business.assembler.composite;

import com.yujj.entity.product.ProductPropName;
import com.yujj.entity.product.ProductPropValue;

public interface ProductPropComposite {
    long getPropertyNameId();
    
    void assemble(ProductPropName productPropName);

    long getPropertyValueId();

    void assemble(ProductPropValue productPropValue);
}
