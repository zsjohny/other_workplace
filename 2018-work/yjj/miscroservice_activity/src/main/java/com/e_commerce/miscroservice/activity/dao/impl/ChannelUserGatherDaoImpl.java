package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.ChannelUserGatherDao;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelUserGather;
import com.e_commerce.miscroservice.activity.mapper.ChannelUserGatherMapper;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 19:11
 * @Copyright 玖远网络
 */
@Component
public class ChannelUserGatherDaoImpl implements ChannelUserGatherDao {




    @Autowired
    private ChannelUserGatherMapper channelUserGatherMapper;



    @Override
    public int save(ChannelUserGather gather) {
        return MybatisOperaterUtil.getInstance().save(gather);
    }

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
    @Override
    public void appendByChannelUserId(Long channelUserId, int fansCount, int orderFansCount, int fansOrderCount, boolean isRollback) {
        if (isRollback) {
            fansCount = 0 - fansCount;
            orderFansCount = 0 - orderFansCount;
            fansOrderCount = 0 - fansOrderCount;
        }

        ChannelUserGather gather = findByUserId(channelUserId);
        ErrorHelper.declareNull(gather, "没有统计信息");
        int upd = channelUserGatherMapper.appendFansCount(gather.getId(), fansCount, orderFansCount, fansOrderCount);
        ErrorHelper.declare(upd == 1, "新增粉丝数失败");
    }


    public ChannelUserGather findByUserId(Long channelUserId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ChannelUserGather(),
                new MybatisSqlWhereBuild(ChannelUserGather.class)
                        .eq(ChannelUserGather::getChannelUserId, channelUserId)
                        .eq(ChannelUserGather::getDelStatus, StateEnum.NORMAL)
        );
    }



}
