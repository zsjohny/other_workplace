package com.yujj.entity.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ProductPropNameValuesPair implements Serializable {

    private static final long serialVersionUID = -2808162710463420365L;

    private ProductPropName propName;

    private List<ProductPropValue> propValues = new ArrayList<ProductPropValue>();

    public ProductPropNameValuesPair(ProductPropName propName) {
        this.propName = propName;
    }

    public ProductPropName getPropName() {
        return propName;
    }

    public void setPropName(ProductPropName propName) {
        this.propName = propName;
    }

    public List<ProductPropValue> getPropValues() {
        return propValues;
    }

    public void setPropValues(List<ProductPropValue> propValues) {
        this.propValues = propValues;
    }

    public void add(ProductPropValue propValue) {
        if (!this.propValues.contains(propValue)) {
            this.propValues.add(propValue);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getPropName()).append(":");
        builder.append(StringUtils.join(getPropValues(), ","));
        return builder.toString();
    }

}
