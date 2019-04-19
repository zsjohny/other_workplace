package com.jiuy.timer.services;

import com.jiuy.base.model.MyPage;
import com.jiuy.timer.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时器接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/28 15:44
 * @Copyright 玖远网络
 */
public interface ITimerService {

    /**
     * 添加定时任务
     * @param jobDetailVo 定时任务
     * @author Aison
     * @date 2018/5/28 15:51
     */
    void addJob(QrtzJobsAcceptQuery jobDetailVo);

    /**
     * 添加简单定时任务
     *
     * @param jobDetailVo jobDetailVo
     * @author Aison
     * @date 2018/6/15 14:29
     */
     void addSimpleTrigger(QrtzJobsAcceptQuery jobDetailVo);

    /**
     * 添加job的路由
     * @param jobDetailVo job的vo
     * @author Aison
     * @date 2018/6/8 13:40
     */
    void addJobRouter(QrtzJobsAcceptQuery jobDetailVo);

    /**
     * 批量添加jobs
     * @param jobDetailVos 字符串的jobs
     * @author Aison
     * @date 2018/6/8 13:25
     */
    void acceptBatch(String jobDetailVos);

    /**
     * 更新某个定时任务
     * @param jobDetailVo 定时任务
     * @author Aison
     * @date 2018/5/28 18:18
     */
    void updateJob(QrtzJobsAcceptQuery jobDetailVo);


    /**
     * 删除定时任务
     * @param jobDetailVo 定时任务
     * @author Aison
     * @date 2018/5/28 18:23
     */
    void deleteJob(QrtzJobsAcceptQuery jobDetailVo);

    /**
     * 重新启动job任务
     * @param jobDetailVo 定时任务
     * @author Aison
     * @date 2018/5/28 18:23
     */
    void resumeJob(QrtzJobsAcceptQuery jobDetailVo);

    /**
     * 暂停job任务
     * @param jobDetailVo 定时任务
     * @author Aison
     * @date 2018/5/28 18:23
     */
    void pausejob(QrtzJobsAcceptQuery jobDetailVo);


    /**
     * 分页查询任务列表
     * @param qrtzJobDetailsQuery 请求参数封装
     * @author Aison
     * @date 2018/5/29 9:11
     */
    MyPage<QrtzJobsAcceptQuery> qrtzJobPages(QrtzJobsAcceptQuery qrtzJobDetailsQuery);

    /**
     * 添加定时日志
     * @param qrtzOptLog 日志对象
     * @author Aison
     * @date 2018/5/29 13:16
     */
    void addOptLog(QrtzOptLog qrtzOptLog);

    /**
     * 查询某个任务的操作日志 包括执行日志
     * @param query 查询实体
     * @author Aison
     * @date 2018/5/30 14:13
     */
    MyPage<QrtzOptLogQuery> qrtzOptLogs(QrtzOptLogQuery query);

    /**
     * 回调某以个job
     * @param jobName job名称
     * @param jobGroupName job分组
     * @author Aison
     * @date 2018/5/31 14:56
     */
    void callBack(String jobName,String jobGroupName);




}
