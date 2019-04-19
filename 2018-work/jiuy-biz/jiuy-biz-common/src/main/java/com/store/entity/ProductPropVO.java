package com.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.ProductProp;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;

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
        StringBuilder builder = new StringBuilder ();
        if (propName != null) {
            builder.append (propName.getPropertyName ());
        }
        if (propValue != null) {
            builder.append (propValue.getPropertyValue ());
        }
        return builder.toString ();
    }
    
    @JsonIgnore
    public String getValue() {
    	if(propValue==null){
    		return "";
    	}
    	return propValue.getPropertyValue();
    }

	

	


}
