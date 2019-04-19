package com.jiuyuan.util.intercept.merge.handler;

import java.lang.annotation.Annotation;
import java.util.List;

import com.jiuyuan.util.instantiate.CachingInstanciator;
import com.jiuyuan.util.instantiate.TypeDescriptor;
import com.jiuyuan.util.intercept.merge.annotation.MergeNumber;
import com.jiuyuan.util.intercept.merge.handler.MergeCapableHandler;
import com.jiuyuan.util.merger.ObjectMerger;

public class MergeNumberHandler implements MergeCapableHandler {

    @Override
    public Object merge(Annotation mergeCapableAA, TypeDescriptor returnType, List<?> partialResults) {
        MergeNumber mergeAggregation = (MergeNumber) mergeCapableAA;
        ObjectMerger resultMerger = CachingInstanciator.INSTANCE.instantiate(mergeAggregation.resultMerger());
        return resultMerger.merge(returnType, partialResults);
    }
}