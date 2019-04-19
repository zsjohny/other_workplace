package com.jiuy.service;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.mapper.logs.OperatorLogsMapper;
import com.jiuy.model.logs.OperatorLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * 日志service
 * @author Aison
 * @version V1.0
 * @date 2018/6/6 9:24
 * @Copyright 玖远网络
 */
@Service("logsService")
public class LogsServiceImpl implements ILogsService {

    private final OperatorLogsMapper operatorLogsMapper;

    @Autowired
    public LogsServiceImpl(OperatorLogsMapper operatorLogsMapper) {
        this.operatorLogsMapper = operatorLogsMapper;
    }

    /**
     * 异步添加日志
     * @param log 日志实体
     * @author Aison
     * @date 2018/6/6 9:23
     */
    @Async
    @Override
    public Future<ResponseResult> syncAcceptLogs(MyLog log) {

        Future<ResponseResult> future;
        try {
            OperatorLogs logs = new OperatorLogs();
            UserSession userSession = log.getUserSession();
            String userId = userSession == null ? "" : userSession.getUserId().toString();
            String userName = userSession == null ? null : userSession.getUserName();
            logs.setCreateMethod(log.getMethod());
            logs.setCreateTime(new Date());
            logs.setModuelCode(log.getModel().getCode());
            logs.setModuleName(log.getModel().getName());
            logs.setOpertionDetail(Biz.obToJson(log.getLog()));
            logs.setOpertionUserId(userId);
            logs.setOpertionUserName(userName);
            logs.setDataId(log.getDataId());

            operatorLogsMapper.insertSelective(logs);
            future = new AsyncResult<>(ResponseResult.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            future = new AsyncResult<>(ResponseResult.FAILED);
        }
        return future;
    }
}
