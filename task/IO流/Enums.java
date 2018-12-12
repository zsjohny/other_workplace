package org.finace.schedule.utils;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * Created by Ness on 2017/1/8.
 */
public class Enums {
    static enum TestEnum {
        BLUE, RED, YELLOW, BLACK;


    }

    public static void main(String[] args) {
        EnumMap<TestEnum, String> map = new EnumMap<TestEnum, String>(TestEnum.class);
        map.put(TestEnum.BLACK, "11");
        String s = map.get(TestEnum.BLACK);
        System.out.println(s);
        EnumSet<TestEnum> set = EnumSet.allOf(TestEnum.class);
        Iterator<TestEnum> iterator = set.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
