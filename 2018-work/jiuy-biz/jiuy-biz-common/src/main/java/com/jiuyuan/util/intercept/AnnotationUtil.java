package com.jiuyuan.util.intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.jiuyuan.util.intercept.AnnotatedAnnotation;

public class AnnotationUtil {

    public static Annotation getAnnotation(Method method, Class<? extends Annotation> type, boolean searchMetaAnnotation) {
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType() == type) {
                return annotation;
            }

            if (searchMetaAnnotation) {
                for (Annotation meta : annotation.annotationType().getAnnotations()) {
                    if (meta.annotationType() == type) {
                        return meta;
                    }
                }
            }
        }
        return null;
    }

    public static <T extends Annotation> AnnotatedAnnotation<T> getAnnotationAnnotatedBy(Method method, Class<T> type) {
        for (Annotation annotation : method.getAnnotations()) {
            for (Annotation meta : annotation.annotationType().getAnnotations()) {
                if (meta.annotationType() == type) {
                    @SuppressWarnings("unchecked")
                    AnnotatedAnnotation<T> result = new AnnotatedAnnotation<T>(annotation, (T) meta);
                    return result;
                }
            }
        }
        return null;
    }

    public static <T extends Annotation> IndexedAnnotation<T> findFirstParameterAnnotation(Method method, Class<T> type) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int paramIndex = 0; paramIndex < parameterAnnotations.length; paramIndex++) {
            Annotation[] annotations = parameterAnnotations[paramIndex];
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == type) {
                        @SuppressWarnings("unchecked")
                        IndexedAnnotation<T> result = new IndexedAnnotation<T>(paramIndex, (T) annotation);
                        return result;
                    }
                }
            }
        }

        return null;
    }
}
