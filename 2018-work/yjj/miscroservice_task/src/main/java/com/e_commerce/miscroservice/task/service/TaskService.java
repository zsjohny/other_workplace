package com.e_commerce.miscroservice.task.service;

import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * @Author hyf
 * @Date 2018/12/21 10:59
 */
public interface TaskService<T> {
    /**
     * 处理
     * @param dataBase
     * @return
     */
    Response.ResponseData disposeDistribute(T dataBase);
}
