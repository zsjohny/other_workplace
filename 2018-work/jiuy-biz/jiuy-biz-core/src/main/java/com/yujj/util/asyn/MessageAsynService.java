/**
 * 
 */
package com.yujj.util.asyn;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LWS
 *
 */
@Service
public class MessageAsynService {

    @Autowired
    private AmqpTemplate amqpTemplate;
    
    public Object asynExecuteMethod(String signature){
        amqpTemplate.convertAndSend(signature);
        return signature;
    }
}