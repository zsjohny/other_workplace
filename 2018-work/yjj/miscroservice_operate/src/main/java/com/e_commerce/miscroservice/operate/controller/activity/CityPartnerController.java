package com.e_commerce.miscroservice.operate.controller.activity;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.service.activity.CityPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/22 17:09
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("city/partner")
public class CityPartnerController {

    @Autowired
    private CityPartnerService cityPartnerService;

    /**
     * 查询城市合伙人
     * @param name 姓名
     * @param phone 手机号
     * @param pageSize 本页数量
     * @param pageNumber 页码
     * @return
     */
    @RequestMapping("all")
    public Response findAllCityPartner(String name, String phone, @RequestParam(name = "pageSize",required = false,defaultValue = "10") Integer pageSize, @RequestParam(name = "pageNumber") Integer pageNumber){
        return cityPartnerService.findAllCityPartner(name,phone,pageNumber,pageSize);
    }
}
