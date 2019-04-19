package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.ChannelUserDao;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelUser;
import com.e_commerce.miscroservice.activity.mapper.ChannelUserMapper;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 19:06
 * @Copyright 玖远网络
 */
@Component
public class ChannelUserDaoImpl implements ChannelUserDao {




    /**
     * 查找用户
     *
     * @param channelUserId channelUserId
     * @return com.e_commerce.miscroservice.activity.entity.channel.ChannelUser
     * @author Charlie
     * @date 2018/12/24 19:12
     */
    @Override
    public ChannelUser findNormalById(Long channelUserId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ChannelUser(),
                new MybatisSqlWhereBuild(ChannelUser.class)
                        .eq(ChannelUser::getId, channelUserId)
                        .eq(ChannelUser::getDelStatus, StateEnum.NORMAL)
        );
    }



}
