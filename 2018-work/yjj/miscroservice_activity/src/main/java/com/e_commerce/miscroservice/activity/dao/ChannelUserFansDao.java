package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.activity.entity.channel.ChannelUserFans;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 9:52
 * @Copyright 玖远网络
 */
public interface ChannelUserFansDao {

    /**
     * 查询渠道商某个粉丝
     *
     * @param shopMemberId shopMemberId
     * @return com.e_commerce.miscroservice.activity.entity.channel.ChannelUserFans
     * @author Charlie
     * @date 2018/12/24 19:24
     */
    ChannelUserFans findNormalByShopMemberId(Long shopMemberId);

    int save(ChannelUserFans newFans);

    ChannelUserFans findByFansId(Long shopMemberId);


    /**
     * 粉丝支付
     *
     * @param shopMemberId 粉丝用户id
     * @param orderCount 支付订单数量
     * @param isRollback 回滚?
     * @return int
     * @author Charlie
     * @date 2018/12/26 16:55
     */
    int payOrderSuccess(long shopMemberId, int orderCount, boolean isRollback);
}
