package com.test.config;

import java.util.StringJoiner;
import java.util.StringTokenizer;

public class CharLinkAndSplit {
    public static void main(String[] args) {
        StringTokenizer tokenizer = new StringTokenizer("a_b_c", "_", true);
        while (tokenizer.hasMoreElements()) {
            System.out.println(tokenizer.nextElement());
        }

        String[] arr = new String[]{"a", "b", "c"};

        StringJoiner stringJoiner = new StringJoiner("_");

        for (String str : arr) {
            stringJoiner.add(str);
        }
        System.out.println(stringJoiner.toString());

    }
}
