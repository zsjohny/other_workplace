package com.jiuyuan.util;


import java.util.*;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 集合空对象
 * @package githubHome
 * @description
 * @date 2018/6/20 12:47
 */
public class Empty{

    public static <T> Set<T> set() {
        return SET;
    }

    public static <T> List<T> list() {
        return LIST;
    }

    public static <K, V> Map<K, V> map() {
        return MAP;
    }

    private static final Set SET = new EmptySet();
    private static final List LIST = new EmptyList();
    private static final Map MAP = new EmptyMap<>();

    static class EmptySet<E> extends HashSet<E>{
        @Override
        public boolean add(E e) {
            throw new IllegalAccessError("不支持插入元素");
        }
    }

    static class EmptyList<E> extends ArrayList<E>{
        @Override
        public boolean add(E e) {
            throw new IllegalAccessError("不支持插入元素");
        }
    }

    static class EmptyMap<K, V> extends HashMap<K, V>{
        @Override
        public V put(K key, V value) {
            throw new IllegalAccessError("不支持插入元素");
        }
    }

}
