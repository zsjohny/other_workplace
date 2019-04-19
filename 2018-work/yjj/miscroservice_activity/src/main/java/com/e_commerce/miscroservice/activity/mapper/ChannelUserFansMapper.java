package com.e_commerce.miscroservice.activity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/26 16:50
 * @Copyright 玖远网络
 */
@Mapper
public interface ChannelUserFansMapper {


    /**
     * 粉丝支付
     *
     * @param shopMemberId shopMemberId
     * @param orderCount 支付订单数量
     * @return int
     * @author Charlie
     * @date 2018/12/26 16:55
     */
    int payOrderSuccess(@Param("shopMemberId") long shopMemberId,
                        @Param("orderCount") Integer orderCount
                        );
}
