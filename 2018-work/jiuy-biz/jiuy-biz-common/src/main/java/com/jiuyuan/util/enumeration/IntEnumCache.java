package com.jiuyuan.util.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class IntEnumCache {

    private static final ConcurrentHashMap<Class<?>, HashMap<Integer, Enum<?>>> cache =
        new ConcurrentHashMap<Class<?>, HashMap<Integer, Enum<?>>>();

    public static <E extends Enum<E> & IntEnum> E getEnumValue(Class<E> enumClass, Integer value) {
        HashMap<Integer, Enum<?>> lookup = cache.get(enumClass);
        if (lookup == null) {
            lookup = new HashMap<Integer, Enum<?>>();
            EnumSet<E> enumValues = EnumSet.allOf(enumClass);
            for (E enumValue : enumValues) {
                IntEnum intEnum = enumValue;
                lookup.put(intEnum.getIntValue(), enumValue);
            }
            HashMap<Integer, Enum<?>> oldLookup = cache.putIfAbsent(enumClass, lookup);
            if(oldLookup != null) {
                lookup = oldLookup;
            }
        }

        @SuppressWarnings("unchecked")
        E enumValue = (E) lookup.get(value);
        return enumValue;
    }
}
