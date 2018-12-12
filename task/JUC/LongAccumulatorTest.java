package com.finace.miscroservice.commons.utils;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

public class LongAccumulatorTest {
    public static void main(String[] args) {



        LongAccumulator accumulator = new LongAccumulator((x, y) -> x * y, 1);


        accumulator.accumulate(1);
        accumulator.accumulate(2);
        accumulator.accumulate(3);
        accumulator.accumulate(12);
        System.out.println(accumulator.get());
        accumulator.reset();
        System.out.println(accumulator.get());
    }
}
