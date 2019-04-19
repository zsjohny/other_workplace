package com.jiuyuan.util.enumeration;

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumeration<T> implements Enumeration<T> {

    private Iterator<T> source;

    /**
     * Creates a new IterationEnumeration.
     * 
     * @param source The source iterator. Must not be null.
     */
    public IteratorEnumeration(Iterator<T> source) {

        if (source == null) {
            throw new IllegalArgumentException("Source must not be null");
        }

        this.source = source;
    }

    public boolean hasMoreElements() {
        return source.hasNext();
    }

    public T nextElement() {
        return source.next();
    }
}