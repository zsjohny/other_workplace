package com.test;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {


    public static void main(String[] args) throws IOException {

        SpringApplication.run(Main.class, args);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("我被关闭了")));
    }
}
