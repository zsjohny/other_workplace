package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.ChannelOrderRecordDao;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelOrderRecord;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 19:11
 * @Copyright 玖远网络
 */
@Component
public class ChannelOrderRecordDaoImpl implements ChannelOrderRecordDao {


    @Override
    public int save(ChannelOrderRecord newOrderRec) {
        return MybatisOperaterUtil.getInstance().save(newOrderRec);
    }

    @Override
    public ChannelOrderRecord findByOrderId(Long orderId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ChannelOrderRecord(),
                new MybatisSqlWhereBuild(ChannelOrderRecord.class)
                        .eq(ChannelOrderRecord::getOrderId, orderId)
        );
    }
}
