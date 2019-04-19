package com.jiuyuan.util.tree.help.stringify;

public class ToStringStringifier<T> implements Stringifier<T> {

    public String toString(T obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }
}