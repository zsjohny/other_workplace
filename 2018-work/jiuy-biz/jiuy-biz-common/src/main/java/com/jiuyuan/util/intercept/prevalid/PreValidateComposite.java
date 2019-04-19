package com.jiuyuan.util.intercept.prevalid;

import java.lang.annotation.Annotation;

import com.jiuyuan.util.intercept.prevalid.ExecutionFlow;
import com.jiuyuan.util.intercept.prevalid.validator.PreValidator;


public class PreValidateComposite {

    /**
     * PreValidate-annotated annotation
     */
    private Annotation annotation;

    private PreValidator preValidator;

    public PreValidateComposite(Annotation annotation, PreValidator preValidator) {
        this.annotation = annotation;
        this.preValidator = preValidator;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public PreValidator getPreValidator() {
        return preValidator;
    }

    public void setPreValidator(PreValidator preValidator) {
        this.preValidator = preValidator;
    }

    public ExecutionFlow validate(Object value) throws Exception {
        return this.preValidator.validate(annotation, value);
    }
}
