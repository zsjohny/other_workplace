package com.e_commerce.miscroservice.activity.controller;

import com.e_commerce.miscroservice.activity.service.CityPartnerService;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/22 15:52
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/city/partner")
public class CityPartnerController {


    @Autowired
    private CityPartnerService cityPartnerService;
    /**
     * 添加城市合伙人
     * @param name 姓名
     * @param phone 手机
     * @param province 省
     * @param city 市
     * @param district 区
     * @return
     */
    @RequestMapping("insert")
    public Response insertCityPartner(String name,String phone,String province,String city,String district){
        return cityPartnerService.insertCityPartner(name,phone,province,city,district);
    }
}
