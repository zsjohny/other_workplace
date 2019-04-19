package com.e_commerce.miscroservice.product.rpc;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 11:02
 * @Copyright 玖远网络
 */
@FeignClient(value = "ACTIVITY", path = "activity/rpc/ShopActivityRpcController")
@Component
public interface ShopActivityRpcService{

    /**
     * 最近的一个(正在进行或待开始)的活动
     * <p>返回json字符串.返回对象类型可能是团购,也可能是秒杀,自己判定</p>
     *
     * @param activityId 活动id,如果有值,activityType必填
     * @param activityType 活动类型, 如果不传, 则查询该商品参与的团购或者秒杀活动中, 距离仙子最近的一个活动
     * @param shopProductId 小程序商品id,必填
     * @return java.lang.String
     * @author Charlie
     * @date 2018/11/27 10:08
     */
    @RequestMapping( "recentlyShopProductActivity" )
    Map<String, Object> recentlyShopProductActivity(
            @RequestParam(value = "activityId", required = false) Long activityId,
            @RequestParam(value = "activityType", required = false) Integer activityType,
            @RequestParam("shopProductId") Long shopProductId);


}
