package com.jiuyuan.util.adapter;

public interface ValueSupplier<K, V> {

    V get(K key);
}
