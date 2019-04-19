package com.e_commerce.miscroservice.task.job;

import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 调度中心
 * @author hyf
 * @version V1.0
 * @date 2018/12/10 16:08
 * @Copyright 玖远网络
 */
@Component
public class TaskDispatcherCenterImpl implements TaskDispatcherCenter<DataBase>{
    Log logger = Log.getInstance(TaskDispatcherCenterImpl.class);
    @Autowired
    private TaskDispatcherCenterPublisher publisher;

    @Override
    public void distributeServices(DataBase msg) {
        logger.info("开始接受参数={}进行分配", msg);

        publisher = ApplicationContextUtil.getBean(TaskDispatcherCenterPublisher.class);

        publisher.publish(msg);

        logger.info("结束接受参数={}进行分配", msg);
    }
}
