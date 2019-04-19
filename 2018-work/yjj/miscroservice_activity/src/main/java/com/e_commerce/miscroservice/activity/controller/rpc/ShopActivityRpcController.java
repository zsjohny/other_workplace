package com.e_commerce.miscroservice.activity.controller.rpc;

import com.e_commerce.miscroservice.activity.service.ShopActivityService;
import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.activity.ShopActivityQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 10:03
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "activity/rpc/ShopActivityRpcController" )
public class ShopActivityRpcController{


    @Autowired
    private ShopActivityService shopActivityService;

    /**
     * 最近的一个(正在进行或待开始)的活动
     * <p>
     *     type = -1 没有活动
     *     type = 1 团购
     *     type = 2 秒杀
     *
     *     data: 活动
     * </p>
     *
     * @param activityId 活动id,如果有值,activityType必填
     * @param activityType 活动类型, 如果不传, 则查询该商品参与的团购或者秒杀活动中, 距离仙子最近的一个活动
     * @param shopProductId 小程序商品id,必填
     * @return java.lang.String
     * @author Charlie
     * @date 2018/11/27 10:08
     */
    @RequestMapping("recentlyShopProductActivity")
    public Map<String, Object> recentlyShopProductActivity(
            @RequestParam(value = "activityId", required = false) Long activityId,
            @RequestParam(value = "activityType", required = false) Integer activityType,
            @RequestParam("shopProductId") Long shopProductId
    ){
        ShopActivityQuery query = new ShopActivityQuery ();
        query.setId (activityId);
        query.setActivityType (activityType);
        query.setShopProductId (shopProductId);
        return shopActivityService.recentlyShopProductActivity (query);
    }


}
