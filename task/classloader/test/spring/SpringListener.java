package com.finace.miscroservice.task_scheduling.test.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

@WebListener
//必须加@ServletComponentScan 才能识别 WebListener
public class SpringListener implements ServletContextListener, BeanFactoryAware {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("===========================MyServletContextListener初始化");
//        BeanFactory bean = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(BeanFactory.class);

        SpringLoader springLoader = new SpringLoader();
        try {
            ClassLoader loader = springLoader.init();
            DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
            factory.setBeanClassLoader(loader);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("===========================MyServletContextListener销毁");
    }

    BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
