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
import com.jiuyuan.entity.ProductPropName;

/**
 * @author LWS
 *
 */
@Component
public class ProductPropNameAssembler implements Assembler<ProductPropName> {

    @Autowired
    private PropertyService propertyService;
    
    @Override
    public ProductPropName assemble(ProductPropName toBeAssemble) {
        Map<Long,ProductPropName> results = propertyService.getPropertyNames();
        BeanUtils.copyProperties(results.get(toBeAssemble.getIdentity()), toBeAssemble);
        return toBeAssemble;
    }

}
