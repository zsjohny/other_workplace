package com.finace.miscroservice.task_scheduling.test.spring;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class SpringLoader {

    public ClassLoader init() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MalformedURLException {

        File file = new File("E:\\fiance_miscroservice\\fiance_miscroservice\\test\\build\\libs\\test-1.1.1.jar");


        URLClassLoader loader =
                (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(loader, file.toURI().toURL());

        return loader;

    }

}
