package com.jiuyuan.util.intercept.prevalid.validator;

import java.lang.annotation.Annotation;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.util.intercept.prevalid.ExecutionFlow;
import com.jiuyuan.util.intercept.prevalid.annotation.NotNull;

public class NotNullValidator implements PreValidator {

    @Override
    public ExecutionFlow validate(Annotation preValidAA, Object argument) throws Exception {
        NotNull notNull = (NotNull) preValidAA;
        Object value = argument;

        String property = notNull.property();
        if (StringUtils.isNotBlank(property) && argument != null) {
            value = PropertyUtils.getProperty(argument, property);
        }

        if (value != null) {
            return ExecutionFlow.CONTINUE;
        } else if (notNull.exception()) {
            return ExecutionFlow.THROW;
        } else {
            return ExecutionFlow.RETURN;
        }
    }
}
