package com.jiuyuan.util.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class EnumCache {

    private static final ConcurrentHashMap<Class<?>, HashMap<String, Enum<?>>> cache =
        new ConcurrentHashMap<Class<?>, HashMap<String, Enum<?>>>();

    public static <E extends Enum<E>> E getEnumValue(Class<E> enumClass, String value) {
        HashMap<String, Enum<?>> lookup = cache.get(enumClass);
        if (lookup == null) {
            lookup = new HashMap<String, Enum<?>>();
            EnumSet<E> enumValues = EnumSet.allOf(enumClass);
            for (E enumValue : enumValues) {
                lookup.put(enumValue.name(), enumValue);
            }
            HashMap<String, Enum<?>> oldLookup = cache.putIfAbsent(enumClass, lookup);
            if(oldLookup != null) {
                lookup = oldLookup;
            }
        }

        @SuppressWarnings("unchecked")
        E enumValue = (E) lookup.get(value);
        return enumValue;
    }
}
