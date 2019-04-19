package com.jiuyuan.util.intercept.prevalid.validator;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.util.intercept.prevalid.ExecutionFlow;
import com.jiuyuan.util.intercept.prevalid.annotation.NotEmpty;

public class NotEmptyValidator implements PreValidator {

    @Override
    public ExecutionFlow validate(Annotation preValidAA, Object argument) throws Exception {
        NotEmpty notEmpty = (NotEmpty) preValidAA;
        Object value = argument;

        String property = notEmpty.property();
        if (StringUtils.isNotBlank(property) && argument != null) {
            value = PropertyUtils.getProperty(argument, property);
        }

        boolean empty = false;

        if (value == null) {
            empty = true;
        } else {
            if (value instanceof String) {
                empty = ((String) value).length() == 0;
            } else if (value instanceof Collection) {
                empty = ((Collection<?>) value).isEmpty();
            } else if (value instanceof Map) {
                empty = ((Map<?, ?>) value).isEmpty();
            } else {
                throw new IllegalArgumentException("Unsupported type for NotEmptyValidator, argument: " + argument);
            }
        }

        if (!empty) {
            return ExecutionFlow.CONTINUE;
        } else if (notEmpty.exception()) {
            return ExecutionFlow.THROW;
        } else {
            return ExecutionFlow.RETURN;
        }
    }
}
