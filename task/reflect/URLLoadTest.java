package com.finace.miscroservice.task_scheduling.test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class URLLoadTest {
    private String name;
    private String value;

    public String get() {
        String userName = "";
        this.name = userName;
        return "OK";
    }

    public static void main(String[] args) throws MalformedURLException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        File file = new File("E:\\fiance_miscroservice\\fiance_miscroservice\\miscroservice_task_scheduling\\src\\main\\java\\com\\finace\\miscroservice\\task_scheduling\\test\\rpc-1.1.1.jar");
        URL url = file.toURI().toURL();

        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(loader, url);
        Class c = Class.forName("com.wuai.company.rpc.mobile.Client");
        System.out.println(c);


    }
}
