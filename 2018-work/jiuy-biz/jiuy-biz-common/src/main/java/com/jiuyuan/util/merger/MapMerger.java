package com.jiuyuan.util.merger;

import java.util.List;
import java.util.Map;

import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.instantiate.TypeDescriptor;
import com.jiuyuan.util.merger.ObjectMerger;

/**
 * Merge multiple map objects as one.
 * 
 */
public class MapMerger implements ObjectMerger {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object merge(TypeDescriptor typeDescriptor, List<?> parts) {
        if (CollectionUtil.isAllNull(parts)) {
            return null;
        }

        Map result = (Map) typeDescriptor.createDefaultInstance();
        if (parts != null) {
            for (Object part : parts) {
                if (part != null) {
                    result.putAll((Map) part);
                }
            }
        }
        return result;
    }
}
