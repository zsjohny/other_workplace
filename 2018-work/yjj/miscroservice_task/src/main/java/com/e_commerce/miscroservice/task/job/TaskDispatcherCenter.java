package com.e_commerce.miscroservice.task.job;

import com.e_commerce.miscroservice.commons.entity.task.DataBase;

/**
 * 任务分发类
 * @author hyf
 * @version V1.0
 * @date 2018/12/10 16:00
 * @Copyright 玖远网络
 */
public interface TaskDispatcherCenter<T> {
     void distributeServices(T msg);
}
