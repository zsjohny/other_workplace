package com.spring_zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class ZipkinMain {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinMain.class, args);
    }
}
