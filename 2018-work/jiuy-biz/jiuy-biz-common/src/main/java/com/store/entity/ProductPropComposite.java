package com.store.entity;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;

public interface ProductPropComposite {
    long getPropertyNameId();
    
    void assemble(ProductPropName productPropName);

    long getPropertyValueId();

    void assemble(ProductPropValue productPropValue);
}
