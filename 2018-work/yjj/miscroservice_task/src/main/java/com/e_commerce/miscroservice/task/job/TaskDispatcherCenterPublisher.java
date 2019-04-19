package com.e_commerce.miscroservice.task.job;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

/**
 * 任务调度的发布者
 * @author hyf
 * @version V1.0
 * @date 2018/12/10 16:27
 * @Copyright 玖远网络
 */
@Component
public class TaskDispatcherCenterPublisher
{
    private EventBus asyncEventBus;

    public TaskDispatcherCenterPublisher(TaskDispatcherCenterSubscriber taskDispatcherCenterSubscriber){
        asyncEventBus=  asyncEventBus = new EventBus();
        register(taskDispatcherCenterSubscriber);
    }

    /**
     * 注册
     * @param taskDispatcherCenterSubscriber
     */
    public void register(TaskDispatcherCenterSubscriber taskDispatcherCenterSubscriber){
        asyncEventBus.register(taskDispatcherCenterSubscriber);
    }

    /**
     * 注销事件
     * @param taskDispatcherCenterSubscriber
     */
    public void unregister(TaskDispatcherCenterSubscriber taskDispatcherCenterSubscriber){
        asyncEventBus.unregister(taskDispatcherCenterSubscriber);
    }

    /**
     * 触发事件
     * @param obj
     */
    public void publish(Object obj){
        asyncEventBus.post(obj);
    }
}
