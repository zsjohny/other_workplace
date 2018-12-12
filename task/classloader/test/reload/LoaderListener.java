package com.finace.miscroservice.task_scheduling.test.reload;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LoaderListener {
    public void listener(String className) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                try {
                    MyClassLoader loader = new MyClassLoader();
                    Thread.currentThread().setContextClassLoader(loader);
                    loader.register(LoaderTest.class);
                    Class<?> aClass = loader.loadClass(className);
                    Object o = aClass.newInstance();
//                    Method run = o.getClass().getDeclaredMethod("run", String.class);
//                    run.invoke(o, "tom");

                    System.out.println(o.getClass().getClassLoader());
                    LoaderTest loaderTest = (LoaderTest) o;
//                    loaderTest.run("tom");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 1000, 1000 * 10);
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        new LoaderListener().listener("com.finace.miscroservice.task_scheduling.test.reload.LoaderTest");
//        LoaderTest loaderTest = loader.register(LoaderTest.class);

//        while (true) {
//            Thread.sleep(1000 * 5);
//            loaderTest.run("tom");
//        }

    }
}
