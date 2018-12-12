package com.shenofusc.task;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * 延迟任务处理
 */
public class DelayTask implements MessageListener {
    public void onMessage(Message message) {
        try {
            String receivedMsg = new String(message.getBody(), "UTF-8");
            System.out.println("Received : " + receivedMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
