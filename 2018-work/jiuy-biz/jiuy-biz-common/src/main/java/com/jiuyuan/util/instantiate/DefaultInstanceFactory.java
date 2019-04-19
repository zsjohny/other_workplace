package com.jiuyuan.util.instantiate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.CollectionFactory;
import org.springframework.util.NumberUtils;

import com.jiuyuan.util.http.NumberUtil;


public class DefaultInstanceFactory implements InstanceFactory {

    public static final DefaultInstanceFactory INSTANCE = new DefaultInstanceFactory();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> T instantiate(Class<T> type, String hint) {
        Class wrapper = ClassUtils.primitiveToWrapper(type);

        if (type == void.class || type == Void.class) {
            return null;
        } else if (Number.class.isAssignableFrom(wrapper)) {
            String defaultValue = StringUtils.defaultIfBlank(hint, "0");
            return (T) NumberUtils.convertNumberToTargetClass(new BigDecimal(defaultValue), wrapper);
        } else if (Collection.class.isAssignableFrom(type)) {
            int capacity = NumberUtil.parseInt(hint, 0, false);
            return (T) CollectionFactory.createCollection(type, capacity);
        } else if (Map.class.isAssignableFrom(type)) {
            int capacity = NumberUtil.parseInt(hint, 0, false);
            return (T) CollectionFactory.createMap(type, capacity);
        }

        if (type.isInterface()) {
            throw new UnsupportedOperationException("Interface can't be instantiated. type: " + type);
        }

        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Can't instantiate type: " + type);
        }
    }
}
