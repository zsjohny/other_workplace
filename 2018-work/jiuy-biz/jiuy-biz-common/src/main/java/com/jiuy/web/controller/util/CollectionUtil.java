package com.jiuy.web.controller.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

    public static Map<String, Object> createMap(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        return map;
    }

    @SuppressWarnings("unchecked")
	public static <T> List<T> createList(T... objs) {
        List<T> list = new ArrayList<T>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
	public static <T> Set<T> createSet(T... objs) {
        Set<T> set = new HashSet<T>();
        for (T obj : objs) {
            set.add(obj);
        }
        return set;
    }

    public static <T> List<List<T>> splitCollection(Collection<T> original, int sizeOfEach) {
        List<List<T>> result = new ArrayList<List<T>>();
        List<T> last = null;
        for (T t : original) {
            if (last == null || last.size() >= sizeOfEach) {
                last = new ArrayList<T>();
                result.add(last);
            }
            last.add(t);
        }
        return result;
    }

    public static <T> List<T> subList(List<T> list, int fromIndex, int size) {
        return list.subList(fromIndex, Math.min(fromIndex + size, list.size()));
    }

    public static StringBuilder join(Collection<String> collection, String separator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String string : collection) {
            if (!first) {
                builder.append(separator);
            }
            builder.append(string);
            first = false;
        }
        return builder;
    }

    public static boolean isAllNull(Collection<?> collection) {
        if (collection != null) {
            for (Object element : collection) {
                if (element != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <K extends Comparable<? super K>, V> LinkedHashMap<K, V> createSortedMap(Map<K, V> map) {
        List<K> keys = new ArrayList<K>(map.keySet());
        Collections.sort(keys);

        LinkedHashMap<K, V> result = new LinkedHashMap<K, V>();
        for (K key : keys) {
            result.put(key, map.get(key));
        }

        return result;
    }

    public static int size(Collection<?> collection) {
        if (collection == null) {
            return 0;
        } else {
            return collection.size();
        }
    }
}
