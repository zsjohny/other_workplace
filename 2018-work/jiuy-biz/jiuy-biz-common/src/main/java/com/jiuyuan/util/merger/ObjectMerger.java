package com.jiuyuan.util.merger;

import java.util.List;

import com.jiuyuan.util.instantiate.TypeDescriptor;


/**
 * Merge a list of objects as one.
 * 
 */
public interface ObjectMerger {

    Object merge(TypeDescriptor typeDescriptor, List<?> parts);
}
