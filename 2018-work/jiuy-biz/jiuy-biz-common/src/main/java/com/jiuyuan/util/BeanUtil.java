package com.jiuyuan.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class BeanUtil {

    public static <S, T> void copyProperties(T target, S source) {
        try {
            BeanUtils.copyProperties(target, source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
