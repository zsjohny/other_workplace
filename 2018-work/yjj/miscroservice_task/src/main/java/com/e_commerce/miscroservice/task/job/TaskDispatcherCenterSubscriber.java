package com.e_commerce.miscroservice.task.job;


import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums.*;

/**
 * 消息的订阅者
 * @author hyf
 * @version V1.0
 * @date 2018/12/10 16:30
 * @Copyright 玖远网络
 */
@Component
public class TaskDispatcherCenterSubscriber
{
    private Log logger = Log.getInstance(TaskDispatcherCenterSubscriber.class);
    @Autowired
    private TaskDispatcherBus taskDispatcherBus;
    @Subscribe
    @AllowConcurrentEvents
    public void subscribe(DataBase dataBase){
        logger.info("接收数据={}",dataBase);
        if (dataBase==null){
            logger.warn("数据为空 不予处理");
            return;
        }
        Integer mType = dataBase.getModuleType();
        if (mType == null) {
            logger.warn("未知的分发类型");
            return;
        }
        //选择模块
        //分发
        if (MODEL_TYPE_LIVE.isMe(mType)){
            logger.info("模块选择 直播 LIVE={}", dataBase.getModuleType());
            taskDispatcherBus.liveBus(dataBase);
        }else if (MODEL_TYPE_SHOP.isMe(mType)){
                logger.info("模块选择 店中店 SHOP={}", dataBase.getModuleType());
            taskDispatcherBus.shopBus(dataBase);
        }

    }
}
