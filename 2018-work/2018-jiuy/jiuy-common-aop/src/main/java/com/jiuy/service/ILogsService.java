package com.jiuy.service;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.util.ResponseResult;

import java.util.concurrent.Future;

/**
 * 日志service
 * @author Aison
 * @version V1.0
 * @date 2018/6/6 9:22
 * @Copyright 玖远网络
 */
public interface ILogsService {

    /**
     * 异步添加日志
     * @param log 日志实体
     * @author Aison
     * @date 2018/6/6 9:23
     * @return 返回异步的future
     */
    Future<ResponseResult> syncAcceptLogs(MyLog log);

}
