package com.finace.miscroservice.task_scheduling.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RestController
public class Tests implements BeanFactoryAware {


    @GetMapping("get")
    public void init() {
        try {
//            Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass("com.wuai.company.util.JwtToken");
//
//            Object o = aClass.newInstance();
//            Method get = aClass.getMethod("toToken", Integer.class, String.class);
//
//            Object invoke = get.invoke(o, 1, "234");

//            Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass("contract.test.CustomerController");
            Class<?> aClass = this.getClass().getClassLoader().loadClass("contract.test.CustomerController");

            Object o = aClass.newInstance();
            Method get = aClass.getMethod("get");

            Object invoke = get.invoke(o);
            System.out.println("______________init______________");
            System.out.println(invoke);


            aClass = Thread.currentThread().getContextClassLoader().loadClass("com.finace.miscroservice.test.Rest");

            o = aClass.newInstance();
            get = aClass.getMethod("hi", String.class);

            invoke = get.invoke(o, "test");
            System.out.println("______________test______________");
            System.out.println(invoke);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    private String name;
    private String value;

    public String get() {
        String userName = "";
        this.name = userName;
        return "OK";
    }


    DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
