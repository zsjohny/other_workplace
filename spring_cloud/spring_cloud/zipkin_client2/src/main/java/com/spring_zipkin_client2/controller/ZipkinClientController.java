package com.spring_zipkin_client2.controller;


import com.spring_zipkin_client2.rpc.ZipkinClientRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZipkinClientController {

    @Autowired
    ZipkinClientRpc zipkinClientRpc;


    @GetMapping("hi")
    public String hi(String name) {

        return zipkinClientRpc.hi(name);
    }


}
