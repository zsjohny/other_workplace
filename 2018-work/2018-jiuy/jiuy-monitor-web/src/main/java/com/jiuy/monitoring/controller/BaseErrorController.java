package com.jiuy.monitoring.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/28 11:10
 * @Copyright 玖远网络
 */
@Controller
@RequestMapping(value = "error")
public class BaseErrorController implements ErrorController  {

    @Override
    public String getErrorPath() {
        return "error/error";
    }

    @RequestMapping
    public String error() {
        return getErrorPath();
    }
}
