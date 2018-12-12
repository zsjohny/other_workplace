package com.finace.miscroservice.commons.config;


import com.finace.miscroservice.commons.enums.MqNameEnum;
import com.finace.miscroservice.commons.log.Log;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MqTemplate {


    private Log logger = Log.getInstance(MqTemplate.class);


    public MqTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private RabbitTemplate rabbitTemplate;
    private final double DEFAULT_RATE_LIMITER_COUNT = 10d;
    private RateLimiter rateLimiter = RateLimiter.create(DEFAULT_RATE_LIMITER_COUNT);


    /**
     * 发送消息
     *
     * @param channel 通道消息
     * @param msg     发送消息
     */
    public void sendMsg(String channel, String msg) {

        rateLimiter.acquire();

        if (StringUtils.isAnyEmpty(channel, msg)) {
            logger.warn("向通道={} 发送消息={} 参数为空", channel, msg);
            return;
        }
        logger.info("开始发送向通道={} 发送消息={}", channel, msg);
        rabbitTemplate.convertAndSend((channel + MqNameEnum.EXCHANGER_NAME_SUFFIX.toName()).intern()
                , (channel + MqNameEnum.ROUTING_KEY_NAME_SUFFIX.toName()).intern(), msg);
    }


}
