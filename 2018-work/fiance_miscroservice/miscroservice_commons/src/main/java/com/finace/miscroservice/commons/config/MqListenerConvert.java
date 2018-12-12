package com.finace.miscroservice.commons.config;

import com.finace.miscroservice.commons.log.Log;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.UnsupportedEncodingException;

/**
 * mq的处理转化类
 */
public abstract class MqListenerConvert implements MessageListener {
    private Log logger = Log.getInstance(MqListenerConvert.class);

    private final String START_SING = "\"";
    private final String DEFAULT_JSON_SPLIT = "\t";
    private final double DEFAULT_RATE_LIMITER_COUNT = 50;
    private RateLimiter rateLimiter = RateLimiter.create(DEFAULT_RATE_LIMITER_COUNT);

    /**
     * mq传递 消息
     *
     * @param transferData 转移的数据
     */
    protected abstract void transferTo(String transferData);

    @Override
    public void onMessage(Message message) {
        rateLimiter.acquire();
        String transferData = "";
        try {
            transferData = new String(message.getBody(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("mq的处理转化类 转化消息={} 失败", message.getBody(), e);
        }


        if (transferData.startsWith(START_SING)) {
            transferData = transferData.substring(1, transferData.length() - 1).replaceAll("\\\\\\\\", DEFAULT_JSON_SPLIT).replaceAll("\\\\", "").replaceAll(DEFAULT_JSON_SPLIT, "\\\\");
        }

        if (StringUtils.isEmpty(transferData)) {
            logger.error("MQ所传信息为空，过滤");
            return;
        }

        transferTo(transferData);

    }
}
