package com.jiuyuan.util.instantiate;

import java.util.concurrent.ConcurrentHashMap;

public class CachingInstanciator {

    public static final CachingInstanciator INSTANCE = new CachingInstanciator();

    private InstanceFactory instanceFactory;

    private ConcurrentHashMap<String, Object> instanceCache = new ConcurrentHashMap<String, Object>();

    public CachingInstanciator() {
        this(DefaultInstanceFactory.INSTANCE);
    }

    public CachingInstanciator(InstanceFactory instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    public <T> T instantiate(Class<T> type) {
        Object object = instanceCache.get(type.getName());
        if (object == null) {
            object = this.instanceFactory.instantiate(type, null);
            Object oldObj = instanceCache.putIfAbsent(type.getName(), object);
            if(oldObj != null) {
                object = oldObj;
            }
        }

        @SuppressWarnings("unchecked")
        T instance = (T) object;
        return instance;
    }
}
