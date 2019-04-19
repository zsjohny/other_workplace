package com.jiuy.core.business.assemble.composite;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.service.PropertyService;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;
import com.store.entity.ProductPropComposite;

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

        Map<Long, ProductPropName> nameMap = propertyService.getPropertyNames();
        Map<Long, ProductPropValue> valueMap = propertyService.getPropertyValues();

        for (ProductPropComposite composite : composites) {
            ProductPropName productPropName = nameMap.get(composite.getPropertyNameId());
            composite.assemble(productPropName);
            ProductPropValue productPropValue = valueMap.get(composite.getPropertyValueId());
            composite.assemble(productPropValue);
        }
    }

	
}
