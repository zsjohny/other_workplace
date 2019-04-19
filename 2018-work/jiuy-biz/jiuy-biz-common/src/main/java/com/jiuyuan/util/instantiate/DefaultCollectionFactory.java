package com.jiuyuan.util.instantiate;

import java.lang.reflect.Modifier;
import java.util.Collection;

import org.springframework.core.CollectionFactory;

public class DefaultCollectionFactory {

    public static <E> Collection<E> createCollection(Class<?> declaredType, Class<?> expectedClass, int initialCapacity) {
        boolean isExpectedClassInterface = expectedClass.isInterface();
        int expectedClassModifiers = expectedClass.getModifiers();
        boolean isexpectedClassAbstract = Modifier.isAbstract(expectedClassModifiers);
        boolean isexpectedClassPublic = Modifier.isPublic(expectedClassModifiers);

        if (isexpectedClassPublic && !isExpectedClassInterface && !isexpectedClassAbstract) {
            // public non-abstract class
            try {
                @SuppressWarnings("unchecked")
                Collection<E> instance = (Collection<E>) expectedClass.newInstance();
                return instance;
            } catch (Exception e) {
                // ignore
            }
        }

        return CollectionFactory.createCollection(declaredType, initialCapacity);
    }
}
