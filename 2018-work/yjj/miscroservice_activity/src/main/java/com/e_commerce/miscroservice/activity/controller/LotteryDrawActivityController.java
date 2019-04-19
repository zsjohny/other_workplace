package com.e_commerce.miscroservice.activity.controller;

import com.e_commerce.miscroservice.activity.service.LotteryDrawActivityService;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 抽奖活动
 * @author hyf
 * @version V1.0
 * @date 2018/12/18 15:24
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("lottery/draw")
public class LotteryDrawActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    /**
     * 参加活动
     * @param phone 手机号
     * @return
     */
    @RequestMapping("join/activity")
    public Response joinActivity(String phone){
        return lotteryDrawActivityService.joinActivity(phone);
    }

    /**
     * 产品列表
     * @return
     */
    @RequestMapping("product/list")
    public Response findAllProductList(Integer code){
        return lotteryDrawActivityService.findAllProductList(code);
    }

}
