package com.newman.pay;

import java.util.ArrayList;
import java.util.List;

public class TestA implements Search {
    @Override
    public List serch(String keyword) {
        System.out.println("+++");
        List<String> list = new ArrayList<>();
        list.add("1111");
        list.add("222");

        return list;
    }

}
