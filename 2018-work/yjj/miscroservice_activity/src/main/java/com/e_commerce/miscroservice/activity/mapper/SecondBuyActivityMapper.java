package com.e_commerce.miscroservice.activity.mapper;

import com.e_commerce.miscroservice.activity.entity.SecondBuyActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 10:35
 * @Copyright 玖远网络
 */
@Mapper
public interface SecondBuyActivityMapper{

    /**
     * 最近的一个活动
     *
     * @param shopProductId 商品id
     * @param currentTime 查询日期
     * @param id 活动id, 如果是null则不查
     * @return com.e_commerce.miscroservice.activity.entity.TeamBuyActivity
     * @author Charlie
     * @date 2018/11/27 10:50
     */
    SecondBuyActivity recentlyShopProductActivity(
            @Param ("shopProductId") Long shopProductId,
            @Param ("currentTime") Long currentTime,
            @Param("id") Long id
    );


}
