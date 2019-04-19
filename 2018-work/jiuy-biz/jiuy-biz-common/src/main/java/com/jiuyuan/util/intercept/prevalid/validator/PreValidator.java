package com.jiuyuan.util.intercept.prevalid.validator;

import java.lang.annotation.Annotation;

import com.jiuyuan.util.intercept.prevalid.ExecutionFlow;


public interface PreValidator {

    /**
     * @param preValidAA a PreValid-annotated annotation.
     */
    ExecutionFlow validate(Annotation preValidAA, Object value) throws Exception;
}
