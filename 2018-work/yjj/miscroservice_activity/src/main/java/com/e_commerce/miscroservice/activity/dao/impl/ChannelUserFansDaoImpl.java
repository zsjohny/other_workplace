package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.ChannelUserFansDao;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelUserFans;
import com.e_commerce.miscroservice.activity.mapper.ChannelUserFansMapper;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 9:52
 * @Copyright 玖远网络
 */
@Component
public class ChannelUserFansDaoImpl implements ChannelUserFansDao {


    @Autowired
    private ChannelUserFansMapper channelUserFansMapper;

    /**
     * 查询渠道商某个粉丝
     *
     * @param shopMemberId shopMemberId
     * @return com.e_commerce.miscroservice.activity.entity.channel.ChannelUserFans
     * @author Charlie
     * @date 2018/12/24 19:24
     */
    @Override
    public ChannelUserFans findNormalByShopMemberId(Long shopMemberId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ChannelUserFans(),
                new MybatisSqlWhereBuild(ChannelUserFans.class)
                        .eq(ChannelUserFans::getShopMemberId, shopMemberId)
                        .eq(ChannelUserFans::getDelStatus, StateEnum.NORMAL)
        );
    }

    @Override
    public int save(ChannelUserFans newFans) {
        return MybatisOperaterUtil.getInstance().save(newFans);
    }

    /**
     * 查询粉丝
     *
     * @param shopMemberId shopMemberId
     * @return com.e_commerce.miscroservice.activity.entity.channel.ChannelUserFans
     * @author Charlie
     * @date 2018/12/26 16:34
     */
    @Override
    public ChannelUserFans findByFansId(Long shopMemberId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ChannelUserFans(),
                new MybatisSqlWhereBuild(ChannelUserFans.class)
                        .eq(ChannelUserFans::getDelStatus, StateEnum.NORMAL)
                        .eq(ChannelUserFans::getShopMemberId, shopMemberId)
        );
    }



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
    @Override
    public int payOrderSuccess(long shopMemberId, int orderCount, boolean isRollback) {
        if (isRollback) {
            orderCount = 0 - orderCount;
        }
        return channelUserFansMapper.payOrderSuccess(shopMemberId, orderCount) ;
    }
}
