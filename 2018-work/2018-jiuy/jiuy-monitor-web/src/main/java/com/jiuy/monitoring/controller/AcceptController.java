package com.jiuy.monitoring.controller;


import com.jiuy.base.util.ResponseResult;
import com.jiuy.timer.model.QrtzJobsAcceptQuery;
import com.jiuy.timer.model.QrtzOptLogQuery;
import com.jiuy.timer.services.ITimerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * 接收定时任务
 * @author Aison
 * @version V1.0
 * @date 2018/5/28 14:53
 * @Copyright 玖远网络
 */
@RestController
public class AcceptController {

    @Resource(name = "timerService")
    private ITimerService timerService;

    /**
     * 接收定时任务请求
     * @author Aison
     * @date 2018/5/28 14:56
     */
    @RequestMapping("/acceptJob")
    public ResponseResult acceptJob(QrtzJobsAcceptQuery jobDetailVo) {

        timerService.addJobRouter(jobDetailVo);
        return ResponseResult.SUCCESS;
    }

    /**
     * 批量添加job有事务支持
     * @param jobDetailVos 批量的字符串
     * @author Aison
     * @date 2018/6/8 13:37
     */
    @RequestMapping("/acceptJobs")
    public ResponseResult acceptJob(String jobDetailVos) {
        if(jobDetailVos!=null) {
            try {
                jobDetailVos = URLDecoder.decode(jobDetailVos,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        timerService.acceptBatch(jobDetailVos);
        return ResponseResult.SUCCESS;
    }


    /**
     * 回调某一个定时任务 中提供的回调地址及
     * @param jobName jobName
     * @param jobGroup jobGroup
     * @author Aison
     * @date 2018/5/31 14:55
     */
    @RequestMapping("/doCall")
    public ResponseResult doCall(String jobName,String jobGroup) {

        timerService.callBack(jobName,jobGroup);
        return ResponseResult.SUCCESS;
    }

    /**
     * 分页查询任务清单
     * @param qrtzJobDetailsQuery 查询参数封装
     * @author Aison
     * @date 2018/5/29 9:15
     */
    @RequestMapping("/jobDetails")
    public ResponseResult jobDetails(QrtzJobsAcceptQuery qrtzJobDetailsQuery) {

        return ResponseResult.instance().success(timerService.qrtzJobPages(qrtzJobDetailsQuery));
    }


    /**
     * 查询对象
     * @param qrtzOptLogQuery qrtzOptLogQuery
     * @author Aison
     * @date 2018/5/30 14:11
     */
    @RequestMapping("/jobOptLog")
    public ResponseResult jobOptLog(QrtzOptLogQuery qrtzOptLogQuery) {

        return ResponseResult.instance().success(timerService.qrtzOptLogs(qrtzOptLogQuery));
    }




}
