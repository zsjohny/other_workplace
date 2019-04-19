package com.jiuyuan.util.intercept.merge.handler;

import java.lang.annotation.Annotation;
import java.util.List;

import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.instantiate.CachingInstanciator;
import com.jiuyuan.util.instantiate.TypeDescriptor;
import com.jiuyuan.util.intercept.merge.annotation.MergeMap;
import com.jiuyuan.util.intercept.merge.handler.MergeCapableHandler;
import com.jiuyuan.util.merger.ObjectMerger;

public class MergeMapHandler implements MergeCapableHandler {

    @Override
    public Object merge(Annotation mergeCapableAA, TypeDescriptor returnType, List<?> partialResults) {
        if (CollectionUtil.isAllNull(partialResults)) {
            return null;
        }

        MergeMap mergeMap = (MergeMap) mergeCapableAA;
        ObjectMerger resultMerger = CachingInstanciator.INSTANCE.instantiate(mergeMap.resultMerger());
        return resultMerger.merge(returnType, partialResults);
    }
}