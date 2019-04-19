package com.jiuy.timer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/10 16:38
 * @Copyright 玖远网络
 */

@RestController
public class IndexController {


    @RequestMapping("/move")
    public String move() {

        return "SUCCESS";
    }
}
