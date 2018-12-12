package com.spring_server.ribbon.active.listener;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class EurekaListener {


    @EventListener
    @Async
    public void listenerRegister(EurekaInstanceRegisteredEvent registeredEvent) {
        System.err.println("into register");

        InstanceInfo instanceInfo = registeredEvent.getInstanceInfo();

        //System.out.println(instanceInfo.getInstanceId());;//localhost:client:8070
        System.out.println(instanceInfo.getAppName()); //CLIENT


    }

    @EventListener
    @Async
    public void listenerCancel(EurekaInstanceCanceledEvent canceledEvent) {
        System.err.println("into cancel");

        System.out.println(canceledEvent.getAppName()); //CLIENT


    }



}
