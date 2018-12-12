package com.finace.miscroservice.task_scheduling.test;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        Flusher<String> stringFlusher = new Flusher<>("test", 5, 2000, 30, 1, new PrintOutProcessor());

        int index = 1;
        while (true) {
            stringFlusher.add(String.valueOf(index++));
//            Thread.sleep(1000);
            if (index > 20) {
                break;
            }
        }
    }
}