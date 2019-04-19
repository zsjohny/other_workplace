package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.activity.entity.channel.ChannelUser;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 19:06
 * @Copyright 玖远网络
 */
public interface ChannelUserDao {



    /**
     * 查找用户
     *
     * @param channelUserId channelUserId
     * @return com.e_commerce.miscroservice.activity.entity.channel.ChannelUser
     * @author Charlie
     * @date 2018/12/24 19:12
     */
    ChannelUser findNormalById(Long channelUserId);


}
