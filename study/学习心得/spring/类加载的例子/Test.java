package com.finace.miscroservice.task_scheduling.test;


public class Test {
    public static void main(String[] args) {
        SingleTon singleTon = SingleTon.getInstance();
        System.out.println("count1=" + singleTon.count1);
        System.out.println("count2=" + singleTon.count2);

        //再次加载  上面已经初始化过一次
        singleTon = new SingleTon();
        System.out.println("count1=" + singleTon.count1);
        System.out.println("count2=" + singleTon.count2);

    }
}

class SingleTon {
    private static SingleTon singleTon = new SingleTon();
    public static int count1;
    public static int count2 = 0;

    public SingleTon() {
        System.out.println("____" + count1);
        count1++;
        count2++;
    }

    public static SingleTon getInstance() {
        return singleTon;
    }
}