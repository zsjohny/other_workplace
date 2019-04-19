package com.store.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;
import com.store.entity.ProductPropVO;

@Service
public class ProductPropAssemblerShop {

    @Autowired
    private PropertyServiceShop propertyService;

    public void assemble(List<ProductPropVO> composites) {
        if (composites.isEmpty()) {
            return;
        }

        Set<Long> nameIds = new HashSet<Long>();
        Set<Long> valueIds = new HashSet<Long>();
        for (ProductPropVO composite : composites) {
            nameIds.add(composite.getPropertyNameId());
            valueIds.add(composite.getPropertyValueId());
        }

        Map<Long, ProductPropName> nameMap = propertyService.getPropertyNames(nameIds);
        Map<Long, ProductPropValue> valueMap = propertyService.getPropertyValues(valueIds);

        for (ProductPropVO composite : composites) {
            ProductPropName productPropName = nameMap.get(composite.getPropertyNameId());
            composite.assemble(productPropName);
            ProductPropValue productPropValue = valueMap.get(composite.getPropertyValueId());
            composite.assemble(productPropValue);
        }
    }
}
