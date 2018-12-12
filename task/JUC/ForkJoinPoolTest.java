package org.dream.quota.tcp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by nessary on 16-8-29.
 */
public class Test extends RecursiveTask<Integer> {

    private Integer sum = 0;


    private Integer start;


    private Integer end;


    public Test(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long statTime = System.currentTimeMillis();
        int end = (int) (Math.random() * 9999 + 1);

        int sum = 0;

        for (int i = 0; i < end; i++) {

            sum += i;
        }
        System.out.println("sum:" + sum);

        System.out.println("普通总时长：" + (System.currentTimeMillis() - statTime) + "ms");

        statTime = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool();


        Test test = new Test(0, end);


        Integer invoke = pool.invoke(test);

        System.out.println(invoke);
        test.isCompletedAbnormally();

        System.out.println("\n线程池总时长：" + (System.currentTimeMillis() - statTime) + "ms");


    }

    @Override
    protected Integer compute() {

        if (end - start > 50) {


            Integer middle = (end + start) / 2;
            Test testLeft = new Test(start, middle);
            Test testRight = new Test(middle, end);
          /*  testLeft.fork();
            testRight.fork();


            sum = testLeft.join() + testRight.join();
*/
            invokeAll(testLeft, testRight);

            try {
                sum = testLeft.get() + testRight.get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {

            for (int i = start; i < end; i++) {
                sum += i;
            }

        }

        return sum;

    }
}
