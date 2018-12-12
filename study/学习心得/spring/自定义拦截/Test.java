package com.test.config;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */
@RestController
public class Test {


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String test03(@Validated User user) {
        System.out.println("用于修改用户信息" + user);
        return null;
    }


}
