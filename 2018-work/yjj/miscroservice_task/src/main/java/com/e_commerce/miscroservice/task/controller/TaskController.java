package com.e_commerce.miscroservice.task.controller;

import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hyf
 * @Date 2018/12/21 10:57
 */
@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    /**
     * 处理分发中心入口
     * @param dataBase
     * @return
     */
    @RequestMapping("/dispose/distribute")
    public Response.ResponseData disposeDistribute(@RequestBody DataBase dataBase){
        return taskService.disposeDistribute(dataBase);
    }
}
