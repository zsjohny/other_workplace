/**
 * 
 */
package com.jiuy.core.business.assemble.impl;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuy.core.business.assemble.Assembler;
import com.jiuy.core.service.PropertyService;
import com.jiuyuan.entity.ProductPropValue;

/**
 * @author LWS
 *
 */
@Component
public class ProductPropValueAssembler implements Assembler<ProductPropValue> {

    @Autowired
    private PropertyService propertyService;
    
    @Override
    public ProductPropValue assemble(ProductPropValue toBeAssemble) {
        Map<Long,ProductPropValue> results = propertyService.getPropertyValues();
        BeanUtils.copyProperties(results.get(toBeAssemble.getIdentity()), toBeAssemble);
        return toBeAssemble;
    }

}
