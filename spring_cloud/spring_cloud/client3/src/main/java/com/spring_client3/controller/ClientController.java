package com.spring_client3.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @Value("${server.port}")
    private String port;

    @PostMapping("hi")
    public String getHi(String name) {
        System.out.println("hi 方法 端口：" + port + "被调用");
        return "IM OK  thanks +" + name;

    }
    @GetMapping("get")
    public String get(String name) {
        System.out.println("get 方法 端口：" + port + "被调用");
        return "IM OK  thanks +" + name;

    }
}
