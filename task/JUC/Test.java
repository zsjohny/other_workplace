package com.goldplusgold.td.market.protocol;

import java.util.HashMap;

/**
 * Created by Ness on 2017/4/24.
 */
public class Test {

    public static void judgeNumber() {
        int x = 5, y = 6;
        x ^= y;
        y ^= x;
        x ^= y;
        System.out.println("x:" + x);
        System.out.println("y:" + y);

    }

    public static void judgeParty() {
        int x = 5;

        System.out.println("奇数 为1:" + (x & 1));
        System.out.println("偶数 为0:" + (x & 1));

    }


    public static void getAverage() {

        int x = 4, y = 6;
        System.out.println((x & y) + ((x ^ y) >> 1));


    }

    public static void getOppsite() {

        int x = 4;
        System.out.println(~x + 1);


    }

    public static void getAbs() {

        int x = -4;
        int y = x >> 31;
        System.out.println((x ^ y) - y);

    }


    public static void addNumber() {
        int x = -4, y = 5;

        System.out.println(y | x);


    }


    public static void main(String[] args) {

        addNumber();
    }


}
