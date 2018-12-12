package com.goldplusgold.td.sltp.core.operate.component;

import com.goldplusgold.mq.p2p.P2PChannelBus;
import com.goldplusgold.mq.p2p.P2PChannels;
import com.goldplusgold.mq.pubsub.MsgChannelBus;
import com.goldplusgold.mq.pubsub.PubSubChannels;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 交易订单回调的订阅类
 * Created by Ness on 2017/5/15.
 */
public abstract class OrderTradeNotifySubscribeComponent implements OrderSubscribe {
    @Resource
    private P2PChannelBus P2PChannelBusRabbitImpl;

    @PostConstruct
    public void init() {
        P2PChannelBusRabbitImpl.registerSubscriber((x) -> onMsg(x), P2PChannels.CH_TRADE_NOTIFY);
    }


}
