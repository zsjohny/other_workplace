package com.jiuyuan.util.intercept.prevalid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.jiuyuan.util.instantiate.CachingInstanciator;
import com.jiuyuan.util.intercept.prevalid.annotation.PreValidate;
import com.jiuyuan.util.intercept.prevalid.validator.PreValidator;

public class PreValidateUtil {

    public static List<List<PreValidateComposite>> searchPreValidateComposites(Method method) {
        List<List<PreValidateComposite>> result = new ArrayList<List<PreValidateComposite>>();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int paramIndex = 0; paramIndex < parameterAnnotations.length; paramIndex++) {
            List<PreValidateComposite> composites = new ArrayList<PreValidateComposite>();
            result.add(composites);

            Annotation[] annotations = parameterAnnotations[paramIndex];
            if (annotations == null) {
                continue;
            }

            for (Annotation annotation : annotations) {
                PreValidate preValidate = annotation.annotationType().getAnnotation(PreValidate.class);
                if (preValidate == null) {
                    continue;
                }

                PreValidator preValidator = CachingInstanciator.INSTANCE.instantiate(preValidate.by());
                composites.add(new PreValidateComposite(annotation, preValidator));
            }
        }

        return result;
    }
}
