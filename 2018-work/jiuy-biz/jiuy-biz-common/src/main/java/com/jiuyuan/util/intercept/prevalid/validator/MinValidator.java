package com.jiuyuan.util.intercept.prevalid.validator;

import java.lang.annotation.Annotation;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.util.intercept.prevalid.ExecutionFlow;
import com.jiuyuan.util.intercept.prevalid.annotation.Min;

public class MinValidator implements PreValidator {

    @Override
    public ExecutionFlow validate(Annotation preValidAA, Object argument) throws Exception {
        Min min = (Min) preValidAA;
        Object value = argument;

        String property = min.property();
        if (StringUtils.isNotBlank(property)) {
            value = PropertyUtils.getProperty(argument, property);
        }

        long longValue = (Long) value;
        if (longValue >= min.value()) {
            return ExecutionFlow.CONTINUE;
        } else if (min.exception()) {
            return ExecutionFlow.THROW;
        } else {
            return ExecutionFlow.RETURN;
        }
    }
}
