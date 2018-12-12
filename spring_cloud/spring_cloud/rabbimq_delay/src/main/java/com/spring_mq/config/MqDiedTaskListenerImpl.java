package com.spring_mq.config;

import org.springframework.stereotype.Component;

@Component
public class MqDiedTaskListenerImpl implements MqDiedTaskListener {

    @Override
    public void task(Object msg) {

        System.err.println("==========Died==============");
        if (msg instanceof byte[]) {
            byte[] _by = (byte[]) msg;
            System.out.println(new String(_by));
        }
    }
}
