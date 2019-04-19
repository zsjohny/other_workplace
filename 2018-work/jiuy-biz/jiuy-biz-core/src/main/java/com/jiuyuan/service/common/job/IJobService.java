package com.jiuyuan.service.common.job;

import com.jiuyuan.util.DateUtil;
import com.util.JobDetailVo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title job API
 * @package jiuy-biz
 * @description
 * @date 2018/5/30 12:57
 * @copyright 玖远网络
 */
public interface IJobService{


    /**
     * 向job server申请多种类型的job服务
     *
     * @param description 申请服务的描述, 用于记录日志
     * @param sources
     * @return com.jiuyuan.service.common.job.AbstractJobService.JobResponse
     * @auther Charlie(唐静)
     * @date 2018/6/10 10:04
     */
    Object applyMultiJobTask(String description ,Collection<JobDetailVo>... sources) throws IOException;


    /**
     * 新增一个job任务
     * @param vo
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    Object addJob(String description, JobDetailVo vo) throws IOException;


    /**
     * 新增一个job任务
     * @param vo
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    Object deleteJob(String description, JobDetailVo vo) throws IOException ;


    /**
     * 新增一个job任务
     * @param vo
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    Object updateJob(String description, JobDetailVo vo) throws IOException ;

    /**
     * 暂停一个job任务
     * @param vo
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    Object pauseJob(String description, JobDetailVo vo) throws IOException ;


    /**
     * 将一个具体的日期转换成一个 CronExpression 表达式
     * @param date
     * @return java.lang.String date设置成coreExpression表达式
     * @auther Charlie(唐静)
     * @date 2018/5/30 14:24
     */
    static String date2Cron(Date date){
        if (date == null) {
            throw new NullPointerException("com.jiuyuan.service.common.job.IJobService#date2Cron : 日期不可为空");
        }
        return new SimpleDateFormat("ss mm HH dd MM ? yyyy").format(date);
    }


    /**
     * 将一个具体的日期转换成一个 CronExpression 表达式
     * @param date
     * @return java.lang.String date设置成coreExpression表达式
     * @auther Charlie(唐静)
     * @date 2018/5/30 14:24
     */
    static String date2Cron(long date){
        String dateStr = DateUtil.parseLongTime2Str(date);
        Date dt;
        try {
            dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期转化异常");
        }
        return date2Cron(dt);
    }

}
