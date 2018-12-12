package com.spring_rpc.ribbon.controller;

import com.spring_rpc.ribbon.rpc.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RibbonController {


    @Autowired
    private RibbonService ribbonService;
    @GetMapping("hi")
    public String hi(String name) {

        return ribbonService.hi(name);
    }


}
