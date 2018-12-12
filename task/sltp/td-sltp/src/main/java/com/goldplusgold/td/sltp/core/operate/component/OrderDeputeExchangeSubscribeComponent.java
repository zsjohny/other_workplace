package com.goldplusgold.td.sltp.core.operate.component;


import com.goldplusgold.mq.pubsub.PubSubChannels;
import com.goldplusgold.mq.pubsub.simple_impl.MsgChannelBusSimpleImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 委托交易所的订单的订阅者
 * Created by Ness on 2017/5/10.
 */

public abstract class OrderDeputeExchangeSubscribeComponent implements OrderSubscribe {

    @Autowired
    private MsgChannelBusSimpleImpl msgChannelBusSimple;

    @PostConstruct
    public void init() {
        msgChannelBusSimple.registerSubscriber((msg) -> onMsg(msg), PubSubChannels.CH_SLTP_TRIGGER);

    }


}
