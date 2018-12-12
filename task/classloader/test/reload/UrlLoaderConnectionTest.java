package com.finace.miscroservice.task_scheduling.test.reload;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class UrlLoaderConnectionTest {

    private static void test1() throws ClassNotFoundException, MalformedURLException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        File file = new File("E:\\fiance_miscroservice\\fiance_miscroservice\\miscroservice_task_scheduling\\src\\main\\resources\\lib\\miscroservcice_distribute_task-1.1.1.jar");
        URL url = file.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        Class<?> aClass = loader.loadClass("com.finace.miscroservice.distribute_task.util.CronUtil");

        Constructor<?> constructor = aClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object o = constructor.newInstance();
        Method checkValidCron = aClass.getMethod("checkValidCron", String.class);

        checkValidCron.invoke(o, "1.00");

    }

    private static void test2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MalformedURLException, ClassNotFoundException, InstantiationException {
        File file = new File("E:\\fiance_miscroservice\\fiance_miscroservice\\miscroservice_task_scheduling\\src\\main\\resources\\lib\\miscroservcice_distribute_task-1.1.1.jar");

        URLClassLoader loader =
                (URLClassLoader) ClassLoader.getSystemClassLoader();
        //error写法 load 是强转过来的 不可以进行私有方法的反射
        //Method addURL = loader.getClass().getDeclaredMethod("addURL", URL.class);
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(loader, file.toURI().toURL());
        Class<?> aClass = loader.loadClass("com.finace.miscroservice.distribute_task.util.CronUtil");
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Object o = declaredConstructor.newInstance();
        Method checkValidCron = aClass.getMethod("checkValidCron", String.class);

        checkValidCron.invoke(o, "* * * * * ? * ");

    }


    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        test1();
        test2();
    }
}
