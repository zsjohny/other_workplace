package com.tunnel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Ness on 2016/12/27.
 */
public class Test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        Set<String> set = new TreeSet<>(list);
        list = new ArrayList<>(set);
        System.out.println(list);


    }
}
