package com.test.config;

//如果子线程在取得值的同时，主线程将InheritableThreadLocal中的值进行更改，那么子线程取到的还是旧值。
public class Test3 {

    public static void main(String[] args) {
        InheritableThreadLocal iter = new InheritableThreadLocal(


        ) {
            @Override
            protected Object childValue(Object parentValue) {
                System.out.println(parentValue + "____");
                return super.childValue(parentValue);
            }
        };

        iter.set("kjsndjnsj");

        System.out.println(iter.get());

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            System.out.println(iter.get());

        }).start();

        iter.set("skndj");
        System.out.println(iter.get());

    }


}

