package com.e_commerce.miscroservice.distribution.rpc.activity;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/14 14:50
 * @Copyright 玖远网络
 */
@FeignClient(value = "ACTIVITY", path = "activity/rpc/ShopActivityRpcController")
public interface ShopShareRpcService{

    /**
     * 我的有效粉丝数量
     *
     * @param shopMemberId 小程序用户主键
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/14 14:44
     */
    @RequestMapping( "myEffectiveFans" )
    Long myEffectiveFans(@RequestParam("shopMemberId") Long shopMemberId);



}
