package com.jiuyuan.util.intercept;

import java.lang.annotation.Annotation;

public class IndexedAnnotation<T extends Annotation> {

    private int index;

    private T annotation;

    public IndexedAnnotation(int index, T annotation) {
        this.index = index;
        this.annotation = annotation;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getAnnotation() {
        return annotation;
    }

    public void setAnnotation(T annotation) {
        this.annotation = annotation;
    }
}
