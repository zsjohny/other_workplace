package com.jiuy.pcweb.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 此类是测试首页功能的类
 * @author Aison
 * @date   2018/5/23 21:54
 * @version V1.0
 * @Copyright 玖远网络
 */
@Controller
public class IndexController {


    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
