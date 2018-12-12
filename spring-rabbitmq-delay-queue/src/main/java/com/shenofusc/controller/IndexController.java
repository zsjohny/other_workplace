package com.shenofusc.controller;

import com.shenofusc.service.ProducerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class IndexController {
    @Resource
    private ProducerService producerService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/send")
    public String send(String msg) {
        producerService.send(msg);
        return "index";
    }
}
