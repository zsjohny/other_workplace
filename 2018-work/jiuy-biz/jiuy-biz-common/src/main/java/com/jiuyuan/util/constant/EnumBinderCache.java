package com.jiuyuan.util.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jiuyuan.util.constant.ConstantBinder;

public class EnumBinderCache {

    public static Map<Class<?>, Map<String, ?>> cache = new ConcurrentHashMap<Class<?>, Map<String, ?>>();

    public static <V> Map<String, V> getValueMap(Class<? extends Enum<?>> enumClass, Class<V> valueType,
                                                 String charset, String... classpathLocations) {
        @SuppressWarnings("unchecked")
        Map<String, V> map = (Map<String, V>) cache.get(enumClass);

        if (map == null) {
            map = ConstantBinder.bind(enumClass, valueType, charset, classpathLocations);
            cache.put(enumClass, map);
        }

        return map;
    }
}
