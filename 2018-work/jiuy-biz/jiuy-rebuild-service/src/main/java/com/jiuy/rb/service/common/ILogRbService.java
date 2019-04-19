package com.jiuy.rb.service.common;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.model.log.AccessLog;

import java.util.concurrent.Future;

/**
 * 日志service
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/13 16:22
 * @Copyright 玖远网络
 */
public interface ILogRbService {

    /**
     * 异步添加日志
     * @param log 日志实体
     * @author Aison
     * @date 2018/6/6 9:23
     */
    void syncOperatorLogs(MyLog<?> log);


    /**
     * 添加访问日志
     *
     * @param accessLog 访问日志
     * @author Aison
     * @date 2018/8/3 15:51
     */
   void syncAccessLog(AccessLog accessLog);


}
