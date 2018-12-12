package com.finace.miscroservice;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

//@BenchmarkMode(Mode.Throughput)//基准测试类型
//@OutputTimeUnit(TimeUnit.SECONDS)//基准测试结果的时间类型
//@Warmup(iterations = 3)//预热的迭代次数
//@Threads(2)//测试线程数量
//@State(Scope.Thread)//该状态为每个线程独享
////度量:iterations进行测试的轮次，time每轮进行的时长，timeUnit时长单位,batchSize批次数量
//@Measurement(iterations = 2, time = -1, timeUnit = TimeUnit.SECONDS, batchSize = -1)
public class CountTest {

    @Benchmark
    public void serialLazyJDK() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                builder.append(i);

            }
            System.out.println(builder.toString());

    }


}

