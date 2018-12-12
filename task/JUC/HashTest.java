package com.finace.miscroservice.commons.utils;

public class HashTest {
    public static void main(String[] args) {
        int hash = 7;
        int n = 4;
        //当n 为 2的倍数时候 那么 这两个是等价的 若果则不是等价
        System.out.println(hash & (n - 1));
        System.out.println(hash % n);

    }
}
