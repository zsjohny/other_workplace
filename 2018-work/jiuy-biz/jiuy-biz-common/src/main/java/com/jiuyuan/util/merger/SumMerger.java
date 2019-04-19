package com.jiuyuan.util.merger;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.util.NumberUtils;

import com.jiuyuan.util.instantiate.TypeDescriptor;
import com.jiuyuan.util.merger.ObjectMerger;


/**
 * Compute sum of several numbers.
 * 
 */
public class SumMerger implements ObjectMerger {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object merge(TypeDescriptor typeDescriptor, List<?> parts) {
        BigDecimal result = new BigDecimal(0);
        if (parts != null) {
            for (Object part : parts) {
                if (part != null) {
                    result = result.add(new BigDecimal(part.toString()));
                }
            }
        }
        
        Class clazz = ClassUtils.primitiveToWrapper(typeDescriptor.getType());
        return NumberUtils.convertNumberToTargetClass(result, clazz);
    }
}
