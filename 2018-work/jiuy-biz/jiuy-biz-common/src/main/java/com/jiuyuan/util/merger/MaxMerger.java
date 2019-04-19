package com.jiuyuan.util.merger;

import java.util.List;

import com.jiuyuan.util.instantiate.TypeDescriptor;
import com.jiuyuan.util.merger.ObjectMerger;


/**
 * Compare and return the maximum number.
 * 
 */
public class MaxMerger implements ObjectMerger {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object merge(TypeDescriptor typeDescriptor, List<?> parts) {
        Number result = null;
        if (parts != null) {
            for (Object part : parts) {
                Number number = (Number) part;
                if (number instanceof Comparable) {
                    Comparable comparable = (Comparable) number;
                    if (result == null || comparable.compareTo(result) > 0) {
                        result = number;
                    }
                } else if (number != null) {
                    throw new IllegalArgumentException();
                }
            }
        }
        return result;
    }
}
