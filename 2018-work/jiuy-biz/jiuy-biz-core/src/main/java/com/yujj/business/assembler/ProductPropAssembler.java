package com.yujj.business.assembler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.business.assembler.composite.ProductPropComposite;
import com.yujj.business.service.PropertyService;
import com.yujj.entity.product.ProductPropName;
import com.yujj.entity.product.ProductPropValue;

@Service
public class ProductPropAssembler {

    @Autowired
    private PropertyService propertyService;

    public void assemble(Collection<? extends ProductPropComposite> composites) {
        if (composites.isEmpty()) {
            return;
        }

        Set<Long> nameIds = new HashSet<Long>();
        Set<Long> valueIds = new HashSet<Long>();
        for (ProductPropComposite composite : composites) {
            nameIds.add(composite.getPropertyNameId());
            valueIds.add(composite.getPropertyValueId());
        }

        Map<Long, ProductPropName> nameMap = propertyService.getPropertyNames(nameIds);
        Map<Long, ProductPropValue> valueMap = propertyService.getPropertyValues(valueIds);

        for (ProductPropComposite composite : composites) {
            ProductPropName productPropName = nameMap.get(composite.getPropertyNameId());
            composite.assemble(productPropName);
            ProductPropValue productPropValue = valueMap.get(composite.getPropertyValueId());
            composite.assemble(productPropValue);
        }
    }
}
