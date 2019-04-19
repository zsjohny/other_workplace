package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.activity.entity.channel.ChannelUserGather;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 19:10
 * @Copyright 玖远网络
 */
public interface ChannelUserGatherDao {

    int save(ChannelUserGather gather);

    /**
     * 跟新统计信息
     *
     * @param channelUserId 渠道商id
     * @param fansCount 粉丝数量
     * @param orderFansCount 下单粉丝数量
     * @param fansOrderCount 粉丝下单数量
     * @param isRollback 是否回滚
     * @author Charlie
     * @date 2018/12/26 16:57
     */
    void appendByChannelUserId(Long channelUserId, int fansCount, int orderFansCount, int fansOrderCount, boolean isRollback);
}
