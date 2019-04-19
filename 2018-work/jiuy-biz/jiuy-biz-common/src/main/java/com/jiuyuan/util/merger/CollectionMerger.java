package com.jiuyuan.util.merger;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.instantiate.TypeDescriptor;
import com.jiuyuan.util.merger.ObjectMerger;

/**
 * Merge multiple collection objects as one.
 * 
 */
public class CollectionMerger implements ObjectMerger {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object merge(TypeDescriptor typeDescriptor, List<?> parts) {
        if (CollectionUtil.isAllNull(parts)) {
            return null;
        }

        Collection result = (Collection) typeDescriptor.createDefaultInstance();
        if (parts != null) {
            for (Object part : parts) {
                if (part != null) {
                    result.addAll((Collection) part);
                }
            }
        }
        return result;
    }
}
