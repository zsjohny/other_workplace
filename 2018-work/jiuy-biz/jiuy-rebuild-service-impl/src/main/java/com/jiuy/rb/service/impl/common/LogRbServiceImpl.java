package com.jiuy.rb.service.impl.common;

import com.jiuy.base.model.MyLog;
import com.jiuy.rb.mapper.common.OperatorLogsRbMapper;
import com.jiuy.rb.mapper.log.AccessLastLogMapper;
import com.jiuy.rb.mapper.log.AccessLogMapper;
import com.jiuy.rb.model.log.AccessLog;
import com.jiuy.rb.service.common.ILogRbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 添加日志
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/13 16:23
 * @Copyright 玖远网络
 */
@Service("logRbService")
public class LogRbServiceImpl implements ILogRbService {


    private static ThreadPoolTaskExecutor threadPoolTaskExecutor;

    static {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(200);
        executor.setKeepAliveSeconds(120);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        threadPoolTaskExecutor = executor;
    }

    @Autowired
    private OperatorLogsRbMapper operatorLogsRbMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private AccessLastLogMapper accessLastLogMapper;

    /**
     * 异步操作添加日志
     *
     * @param log 日志实体
     * @author Aison
     * @date 2018/6/6 9:23
     */
    @Async
    @Override
    public void syncOperatorLogs(MyLog<?> log) {

        //TODO:日志终止
//        threadPoolTaskExecutor.execute(()->{
//            try {
//                Map<String,MyLogInner> logMap = log.getLog();
//                if(logMap == null) {
//                    return ;
//                }
//
//                logMap.forEach((key,val)->{
//                    OperatorLogsRb logs = new OperatorLogsRb();
//                    UserSession userSession = log.getUserSession();
//                    String userId = userSession == null ? "" : userSession.getId().toString();
//                    String userName = userSession == null ? null : userSession.getName();
//                    logs.setCreateMethod(log.getMethod());
//                    logs.setCreateTime(new Date());
//                    logs.setModuelCode(log.getModel().getCode());
//                    logs.setModuleName(log.getModel().getName());
//                    logs.setOpertionDetail(Biz.obToJson(val.getLogStrs()));
//                    logs.setOpertionUserId(userId);
//                    logs.setOpertionUserName(userName);
//                    logs.setDataId(key);
//                    logs.setTableComment(val.getTableComment());
//                    logs.setTableName(val.getTableName());
//                    operatorLogsRbMapper.insertSelective(logs);
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }

    /**
     * 添加访问日志
     *
     * @param accessLog 访问日志
     * @author Aison
     * @date 2018/8/3 15:51
     */
    @Override
    public void syncAccessLog(AccessLog accessLog) {
        //TODO:日志终止
//        threadPoolTaskExecutor.execute(() -> {
//            accessLogMapper.insertSelective(accessLog);
//
//            // 有用户信息的时候才计算
//            if (accessLog.getUserId() != null) {
//                AccessLastLogQuery query = new AccessLastLogQuery();
//                query.setUserId(accessLog.getUserId());
//                query.setType(accessLog.getType());
//                AccessLastLog lastLog = accessLastLogMapper.selectOne(query);
//
//                if (lastLog == null) {
//                    lastLog = new AccessLastLog();
//                    lastLog.setCreateTime(new Date());
//                    lastLog.setUserId(accessLog.getUserId());
//                    lastLog.setType(accessLog.getType());
//                    lastLog.setUri(accessLog.getUri());
//                    lastLog.setIp(accessLog.getIp());
//                    accessLastLogMapper.insertSelective(lastLog);
//                } else {
//                    AccessLastLog last = new AccessLastLog();
//                    last.setId(lastLog.getId());
//                    last.setCreateTime(new Date());
//                    accessLastLogMapper.updateByPrimaryKeySelective(last);
//                }
//            }
//        });
    }


}
