package com.goldplusgold.td.sltp.core.operate.component;

import com.goldplusgold.mq.p2p.P2PChannelBus;
import com.goldplusgold.mq.p2p.P2PChannels;
import com.goldplusgold.mq.p2p.P2PPublisher;
import com.goldplusgold.mq.pubsub.MQPublisher;
import com.goldplusgold.mq.pubsub.MsgChannelBus;
import com.goldplusgold.mq.pubsub.PubSubChannels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 订单跟交易系统进行通知的发布者
 * Created by Ness on 2017/5/17.
 */
@Component
public class OrderTradeNotifyPublishComponent implements OrderPublish {

    private P2PPublisher publisher;

    @Autowired
    public OrderTradeNotifyPublishComponent(@Qualifier("p2PChannelBusRabbitImpl") P2PChannelBus channelBus) {
        this.publisher = channelBus.registerPublisher(P2PChannels.CH_TRADE_OFFSET);
    }

    @Override
    public void publish(Object msg) {
        //TODO:待定
        publisher.publish(msg);
    }
}
