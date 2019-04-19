package com.jiuyuan.util.intercept.merge.handler;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.instantiate.CachingInstanciator;
import com.jiuyuan.util.instantiate.TypeDescriptor;
import com.jiuyuan.util.intercept.merge.annotation.MergeCollection;
import com.jiuyuan.util.intercept.merge.handler.MergeCapableHandler;
import com.jiuyuan.util.merger.ObjectMerger;
import com.jiuyuan.util.sort.CollectionSorter;

public class MergeCollectionHandler implements MergeCapableHandler {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object merge(Annotation mergeCapableAA, TypeDescriptor returnType, List<?> partialResults) throws Exception {
        if (CollectionUtil.isAllNull(partialResults)) {
            return null;
        }

        MergeCollection mergeCollection = (MergeCollection) mergeCapableAA;
        ObjectMerger resultMerger = CachingInstanciator.INSTANCE.instantiate(mergeCollection.resultMerger());
        Collection<?> result = (Collection<?>) resultMerger.merge(returnType, partialResults);

        String[] uniKeys = mergeCollection.uniKeys();
        if (uniKeys != null && uniKeys.length > 0) {
            Set<List<Object>> keysSet = new HashSet<List<Object>>();
            Iterator<?> iterator = result.iterator();
            while (iterator.hasNext()) {
                Object element = iterator.next();
                List<Object> keys = new ArrayList<Object>();
                for (String uniKey : uniKeys) {
                    keys.add(PropertyUtils.getProperty(element, uniKey));
                }
                if (keysSet.contains(keys)) {
                    iterator.remove();
                } else {
                    keysSet.add(keys);
                }
            }
        }

        String order = mergeCollection.order();
        if (StringUtils.isNotBlank(order)) {
            List sortedList = CollectionSorter.sort(result, order);
            Collection<?> sortedResult = (Collection<?>) returnType.createDefaultInstance();
            sortedResult.addAll(sortedList);
            return sortedResult;
        }

        return result;
    }
}