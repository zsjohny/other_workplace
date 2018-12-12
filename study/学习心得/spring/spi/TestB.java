package com.newman.pay;

import java.util.ArrayList;
import java.util.List;

public class TestB implements Search {
    @Override
    public List serch(String keyword) {
        System.out.println("+++");
        List<String> list = new ArrayList<>();
        list.add("33333");
        list.add("444444444");

        return list;
    }

}
