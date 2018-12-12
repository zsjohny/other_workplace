package com.spring_consul.controller;


import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsulController {


    public String hi(String name) {
        return "OK " + name;
    }
}
