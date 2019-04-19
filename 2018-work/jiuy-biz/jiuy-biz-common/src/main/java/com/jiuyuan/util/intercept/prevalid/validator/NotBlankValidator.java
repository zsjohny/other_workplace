package com.jiuyuan.util.intercept.prevalid.validator;

import java.lang.annotation.Annotation;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.util.intercept.prevalid.ExecutionFlow;
import com.jiuyuan.util.intercept.prevalid.annotation.NotBlank;

public class NotBlankValidator implements PreValidator {

    @Override
    public ExecutionFlow validate(Annotation preValidAA, Object argument) throws Exception {
        NotBlank notBlank = (NotBlank) preValidAA;
        Object value = argument;

        String property = notBlank.property();
        if (StringUtils.isNotBlank(property) && argument != null) {
            value = PropertyUtils.getProperty(argument, property);
        }

        String string = (String) value;
        if (StringUtils.isNotBlank(string)) {
            return ExecutionFlow.CONTINUE;
        } else if (notBlank.exception()) {
            return ExecutionFlow.THROW;
        } else {
            return ExecutionFlow.RETURN;
        }
    }
}
