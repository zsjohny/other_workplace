package com.jiuyuan.util.intercept.merge.handler;

import java.lang.annotation.Annotation;
import java.util.List;

import com.jiuyuan.util.instantiate.TypeDescriptor;


public interface MergeCapableHandler {

    /**
     * Merge the patial results as one single object.
     * 
     * @param mergeCapableAA a MergeCapable-annotated annotation.
     */
    Object merge(Annotation mergeCapableAA, TypeDescriptor returnType, List<?> partialResults) throws Exception;
}
