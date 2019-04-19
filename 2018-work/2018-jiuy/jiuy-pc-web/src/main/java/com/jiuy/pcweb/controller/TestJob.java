package com.jiuy.pcweb.controller;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/28 13:35
 * @Copyright 玖远网络
 */

import com.jiuy.base.util.Biz;
import com.jiuy.product.service.IProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Date;

@Controller
@Log4j2
public class TestJob {



    private final IProductService productService;

    @Autowired
    public TestJob(IProductService productService) {
        this.productService = productService;
    }

    /**
     * 测试易宝回调
     * @param request
     * @param host
     * @author Aison
     * @date 2018/7/4 10:23
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("testPayment")
    public String tesPayment(HttpServletRequest request,String host) {


        productService.moveTemplate();

        return "xx";
    }

}
