package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.activity.entity.channel.ChannelOrderRecord;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 19:11
 * @Copyright 玖远网络
 */
public interface ChannelOrderRecordDao {
    int save(ChannelOrderRecord newOrderRec);

    ChannelOrderRecord findByOrderId(Long orderId);
}
