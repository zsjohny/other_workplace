package com.finace.miscroservice.distribute_task.helper;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.distribute_task.timerTask.TimeTask;
import com.finace.miscroservice.distribute_task.timerTask.TimeTaskBus;
import com.finace.miscroservice.distribute_task.timerTask.TimeTaskJob;
import com.finace.miscroservice.distribute_task.util.IpUtil;
import com.finace.miscroservice.distribute_task.util.LogUtil;
import com.finace.miscroservice.distribute_task.zookeeper.ZKManager;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * 定时任务的帮助类
 */
public class TimeTaskHelper {

    private TimeTaskBus timeTaskBus;
    private ZKManager zkManager;

    /**
     * @param serverList  zk服务地址
     * @param timeTaskJob 监听的定时任务处理类
     */
    public TimeTaskHelper(String serverList, TimeTaskJob timeTaskJob, LogUtil logUtil) {
        timeTaskBus = new TimeTaskBus(timeTaskJob, logUtil);
        zkManager = new ZKManager(serverList, timeTaskBus, logUtil);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> timeTaskBus.destroy()));
    }

    /**
     * @param serverList  zk服务地址
     * @param timeTaskJob 监听的定时任务处理类
     * @param ipUtil      ip的帮助类 获取外网Ip
     */
    public TimeTaskHelper(String serverList, TimeTaskJob timeTaskJob, LogUtil logUtil, IpUtil ipUtil) {
        timeTaskBus = new TimeTaskBus(timeTaskJob, logUtil);
        zkManager = new ZKManager(serverList, timeTaskBus, logUtil, ipUtil);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> timeTaskBus.destroy()));
    }

    /**
     * 恢复数据
     *
     * @param timeTasks 需要恢复的定时任务列表
     */
    public void restart(List<TimeTask> timeTasks) {
        Thread thread = new Thread(() -> zkManager.resetAllTask(timeTasks));
        thread.setDaemon(Boolean.TRUE);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setName("timerSchedulerTaskInit");
        thread.start();

    }

    /**
     * 执行定时任务
     *
     * @param timeTask 定时任务类型
     */
    public void execute(TimeTask... timeTask) {
        timeTaskBus.execute(timeTask);
        zkManager.transfer(JSONObject.toJSONString(timeTask));
    }




}
