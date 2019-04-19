package com.jiuy.core.ext.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class StringEnumCache {

    private static final ConcurrentHashMap<Class<?>, HashMap<String, Enum<?>>> cache =
        new ConcurrentHashMap<Class<?>, HashMap<String, Enum<?>>>();

    public static <E extends Enum<E> & StringEnum> E getEnumValue(Class<E> enumClass, String value) {
        HashMap<String, Enum<?>> lookup = cache.get(enumClass);
        if (lookup == null) {
            lookup = new HashMap<String, Enum<?>>();
            EnumSet<E> enumValues = EnumSet.allOf(enumClass);
            for (E enumValue : enumValues) {
                StringEnum stringEnum = enumValue;
                lookup.put(stringEnum.getStringValue(), enumValue);
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
