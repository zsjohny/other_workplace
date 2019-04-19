package com.jiuyuan.util.intercept;

import java.lang.annotation.Annotation;

public class AnnotatedAnnotation<M extends Annotation> {

    private Annotation annotatedAnnotation;

    private M metaAnnotation;

    public AnnotatedAnnotation(Annotation annotatedAnnotation, M metaAnnotation) {
        this.annotatedAnnotation = annotatedAnnotation;
        this.metaAnnotation = metaAnnotation;
    }

    public Annotation getAnnotatedAnnotation() {
        return annotatedAnnotation;
    }

    public void setAnnotatedAnnotation(Annotation annotatedAnnotation) {
        this.annotatedAnnotation = annotatedAnnotation;
    }

    public M getMetaAnnotation() {
        return metaAnnotation;
    }

    public void setMetaAnnotation(M metaAnnotation) {
        this.metaAnnotation = metaAnnotation;
    }
}
