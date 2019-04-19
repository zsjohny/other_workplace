package com.yujj.entity.product;

import com.yujj.business.assembler.composite.ProductPropComposite;
import com.yujj.entity.product.ProductProp;
import com.yujj.entity.product.ProductPropName;
import com.yujj.entity.product.ProductPropValue;


public class ProductPropVO extends ProductProp implements ProductPropComposite {

    private static final long serialVersionUID = -7978615925366448744L;

    private ProductPropName propName;

    private ProductPropValue propValue;

    public ProductPropName getPropName() {
        return propName;
    }

    public void setPropName(ProductPropName propName) {
        this.propName = propName;
    }

    public ProductPropValue getPropValue() {
        return propValue;
    }

    public void setPropValue(ProductPropValue propValue) {
        this.propValue = propValue;
    }

    @Override
    public void assemble(ProductPropName productPropName) {
        setPropName(productPropName);
    }

    @Override
    public void assemble(ProductPropValue productPropValue) {
        setPropValue(productPropValue);
    }

    @Override
    public String toString() {
        return getPropName() + ":" + getPropValue();
    }

}
