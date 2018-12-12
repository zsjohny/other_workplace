package org.dream.quota.tcp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by nessary on 16-8-30.
 */
public class ThreadPoolExecutorTest {


    public String getHello() {

        return "hello3333";
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:app*.xml");

        String str = (String) context.getBean("test2");

        System.err.println(Thread.currentThread().getId() + "into...1");
        System.out.println(Thread.currentThread().getId() + str);

        System.err.println(Thread.currentThread().getId() + "into...2");
    }

}
