package com.spring_hystrix_turbine_client1.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HystrixTurbineClient1Controller {


    //3解决跨域 另外两个在 crossConfig里面
    @CrossOrigin(origins = "*")
    @GetMapping("hi")
    @HystrixCommand(fallbackMethod = "error")
    public String hi(String name) {


        return "you name is " + name;
    }


    public String error(String name) {
        return "sorry forbidden " + name;
    }


}
