package com.spring_zipkin_client1.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZipkinClientController {


    @GetMapping("hi")
    public String hi(String name) {

        return "I'm Ok ，thanks " + name + " ,you fine";
    }


}
