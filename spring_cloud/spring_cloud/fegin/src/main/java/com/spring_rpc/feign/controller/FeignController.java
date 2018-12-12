package com.spring_rpc.feign.controller;

import com.spring_rpc.feign.rpc.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {


    @Autowired
    private FeignService feignService;

    @GetMapping("hi")
    public String hi(String name) {
        return feignService.hi(name);
    }

    @GetMapping("get")
    public String get(String name) {

        return feignService.get(name);

    }

}
