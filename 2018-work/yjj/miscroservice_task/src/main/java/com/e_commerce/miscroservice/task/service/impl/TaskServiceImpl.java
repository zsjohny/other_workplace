package com.e_commerce.miscroservice.task.service.impl;

import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.task.job.TaskDispatcherCenter;
import com.e_commerce.miscroservice.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author hyf
 * @Date 2018/12/21 11:00
 */
@Service
public class TaskServiceImpl  implements TaskService<DataBase> {
    Log logger = Log.getInstance(TaskServiceImpl.class);

    @Autowired
    private TaskDispatcherCenter taskDispatcherCenter;

    /**
     * 统一处理
     * @param dataBase
     * @return
     */
    @Override
    public Response.ResponseData disposeDistribute(DataBase dataBase) {
        logger.info("数据统一处理={}", dataBase);
        CompletableFuture<Object> objectCompletableFuture = new CompletableFuture<Object>();
        dataBase.setObjectCompletableFuture(objectCompletableFuture);
        taskDispatcherCenter.distributeServices(dataBase);
        try {
            return (Response.ResponseData) objectCompletableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Response.errorMsg("操作失败");
    }
}
