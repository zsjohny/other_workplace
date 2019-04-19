package com.e_commerce.miscroservice.activity.controller.rpc;

import com.e_commerce.miscroservice.activity.service.ShareService;
import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/14 14:42
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "activity/rpc/ShopActivityRpcController" )
public class ShopShareRpcController{


    @Autowired
    private ShareService shareService;

    /**
     * 我的有效粉丝数量
     *
     * @param shopMemberId 小程序用户主键
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/14 14:44
     */
    @RequestMapping( "myEffectiveFans" )
    public Long myEffectiveFans(@RequestParam("shopMemberId") Long shopMemberId) {
        return shareService.myEffectiveFans (shopMemberId);
    }

}
