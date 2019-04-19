package com.e_commerce.miscroservice.activity.service;

import com.e_commerce.miscroservice.commons.entity.activity.ShopActivityQuery;

import java.util.Map;

/**
 * 小程序活动(团购和秒杀)
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 10:02
 * @Copyright 玖远网络
 */
public interface ShopActivityService{

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
     * @param query query
     * @return java.lang.Object
     * @author Charlie
     * @date 2018/11/27 10:35
     */
    Map<String, Object> recentlyShopProductActivity(ShopActivityQuery query);
}
