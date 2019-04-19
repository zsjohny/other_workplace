package com.jiuy.timer.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.HttpRequest;
import com.jiuy.mapper.QrtzJobDetailsMapper;
import com.jiuy.mapper.QrtzOptLogMapper;
import com.jiuy.timer.job.BaseJob;
import com.jiuy.timer.mapper.QrtzJobsAcceptMapper;
import com.jiuy.timer.model.*;
import com.jiuy.timer.services.ITimerService;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时器接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/28 15:44
 * @Copyright 玖远网络
 */
@Log4j2
@Service("timerService")
public class TimeServiceImpl implements ITimerService {

    private final Scheduler scheduler;

    private final QrtzJobDetailsMapper qrtzJobDetailsMapper;

    private final QrtzOptLogMapper qrtzOptLogMapper;

    private final QrtzJobsAcceptMapper qrtzJobsAcceptMapper;

    @Autowired
    public TimeServiceImpl(@Qualifier("Scheduler") Scheduler scheduler, QrtzJobDetailsMapper qrtzJobDetailsMapper, QrtzOptLogMapper qrtzOptLogMapper, QrtzJobsAcceptMapper qrtzJobsAcceptMapper) {
        this.scheduler = scheduler;
        this.qrtzJobDetailsMapper = qrtzJobDetailsMapper;
        this.qrtzOptLogMapper = qrtzOptLogMapper;
        this.qrtzJobsAcceptMapper = qrtzJobsAcceptMapper;
    }

    /**
     * 添加调度任务
     * @param jobDetailVo 定时任务对象封装
     * @author Aison
     * @date 2018/5/28 15:46
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addJob(QrtzJobsAcceptQuery jobDetailVo) {

        //如果有开始时间和结束时间 还有间隔时间 则调用简单添加的逻辑
        if(Biz.isNotEmpty(jobDetailVo.getBeginDate(),jobDetailVo.getEndDate(),jobDetailVo.getEachTime())){
            addSimpleTrigger(jobDetailVo);
            return ;
        }
       try {
           scheduler.start();
           String jobName = jobDetailVo.getJobName();
           String jobGroupName = jobDetailVo.getJobGroup();
           String cronExpression = jobDetailVo.getCronExpression();
           //构建job信息
           JobDetail jobDetail = JobBuilder.newJob(BaseJob.class).withIdentity(jobName, jobGroupName).build();
           jobDetail.getJobDataMap().put("data",Biz.obToJson(jobDetailVo));
           //表达式调度构建器(即任务执行的时间)
           CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
           //按新的cronExpression表达式构建一个新的trigger
           CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(scheduleBuilder).build();
           scheduler.scheduleJob(jobDetail, trigger);
           // 添加任务
           jobDetailVo.setCreateTime(new Date());
           qrtzJobsAcceptMapper.insertSelective(jobDetailVo);

       }catch (Exception e) {
           e.printStackTrace();
           throw BizException.def().msg(Biz.getFullException(e));
       }
    }

    /**
     * 添加简单定时任务
     *
     * @param jobDetailVo jobDetailVo
     * @author Aison
     * @date 2018/6/15 14:29
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSimpleTrigger(QrtzJobsAcceptQuery jobDetailVo) {

        try{
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date  beginTime = formatDate.parse(jobDetailVo.getBeginDate());
            Date  endTime = formatDate.parse(jobDetailVo.getEndDate());
            String jobName = jobDetailVo.getJobName();
            String jobGroupName = jobDetailVo.getJobGroup();
            JobDetail jobDetail = JobBuilder.newJob(BaseJob.class).withIdentity(jobName, jobGroupName).build();
            jobDetail.getJobDataMap().put("data",Biz.obToJson(jobDetailVo));
            //触发器名称  触发器分组  执行次数  间隔时间(毫秒级)
            SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(jobName,jobGroupName)
                    .startAt(beginTime)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(jobDetailVo.getEachTime()).repeatForever())
                    .endAt(endTime).build();
            scheduler.scheduleJob(jobDetail,simpleTrigger);
            scheduler.start();
            jobDetailVo.setCreateTime(new Date());
            qrtzJobsAcceptMapper.insertSelective(jobDetailVo);
        }catch (Exception e) {
            e.printStackTrace();
            throw BizException.def().msg(Biz.getFullException(e));
        }
    }

    /**
     * 批量添加调度任务
     * @param jobDetailVoStr 定时任务对象封装
     * @author Aison
     * @date 2018/5/28 15:46
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptBatch(String jobDetailVoStr) {
        try{
            List<QrtzJobsAcceptQuery>  jobDetailVos =  Biz.jsonStrToListObject(jobDetailVoStr,List.class,QrtzJobsAcceptQuery.class);
            for (QrtzJobsAcceptQuery jobDetailVo : jobDetailVos) {
               addJobRouter(jobDetailVo);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw BizException.def().msg(Biz.getFullException(e));
        }
    }

    /**
     * 添加job的路由
     * @param jobDetailVo job的vo
     * @author Aison
     * @date 2018/6/8 13:40
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addJobRouter(QrtzJobsAcceptQuery jobDetailVo) {
        Integer type = jobDetailVo.getJobType();
        type = type == null ? 0 : type;
        QrtzOptLog qrtzOptLog = jobDetailVo.logInstance();
        jobDetailVo.setJobClassName(BaseJob.class.getName());
        Integer eachTime = jobDetailVo.getEachTime();
        if(eachTime ==null) {
            jobDetailVo.setEachTime(0);
        }
        switch (type) {
            case 1 :
            case 0:
                jobDetailVo.setCronExpression(Biz.date2cron(jobDetailVo.getCronExpression(),jobDetailVo.getDate()));
                addJob(jobDetailVo);
                break;
            case 2:
                jobDetailVo.setCronExpression(Biz.date2cron(jobDetailVo.getCronExpression(),jobDetailVo.getDate()));
                updateJob(jobDetailVo);
                break;
            case 3:
                pausejob(jobDetailVo);
                break;
            case 4:
                resumeJob(jobDetailVo);
                break;
            case 5:
                deleteJob(jobDetailVo);
                break;
            default:
                break;
        }
        qrtzOptLog.setStatus(1);
        qrtzOptLog.setResult("操作成功");
        addOptLog(qrtzOptLog);
    }



    /**
     * 更新某个定时任务
     * @param jobDetailVo 定时任务封装
     * @author Aison
     * @date 2018/5/28 18:18
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(QrtzJobsAcceptQuery jobDetailVo) {

      try{
          String jobName = jobDetailVo.getJobName();
          String jobGroupName = jobDetailVo.getJobGroup();
          String cronExpression = jobDetailVo.getCronExpression();

          TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
          CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
          JobKey jobKey = trigger.getJobKey();
          JobDetail jobDetail = scheduler.getJobDetail(jobKey);
          String data = (String) jobDetail.getJobDataMap().get("data");
          String newData = Biz.obToJson(jobDetailVo);

          // 如果参数有变动
          if(!newData.equals(data)) {
              // 先暂停此job
              pausejob(jobDetailVo);
              // 然后删除job
              deleteJob(jobDetailVo);
              // 重新在添加job
              addJob(jobDetailVo);
              return ;
          } else {
              jobDetail.getJobDataMap().put("data",Biz.obToJson(jobDetailVo));
              // 表达式调度构建器
              CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
              // 按新的cronExpression表达式重新构建trigger
              trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
              // 按新的trigger重新设置job执行
              scheduler.rescheduleJob(triggerKey, trigger);
          }

          QrtzJobsAcceptQuery query = new QrtzJobsAcceptQuery();
          query.setJobName(jobDetailVo.getJobName());
          query.setJobGroup(jobDetailVo.getJobGroup());
          QrtzJobsAccept accept =  qrtzJobsAcceptMapper.selectOne(query);
          if(accept!=null) {
              accept.setUpdateTime(new Date());
              accept.setJobComment(jobDetailVo.getJobComment());
              accept.setFeedbackData(jobDetailVo.getFeedbackData());
              accept.setFeedbackUrl(jobDetailVo.getFeedbackUrl());
              accept.setCronExpression(jobDetailVo.getCronExpression());
              accept.setDate(jobDetailVo.getDate());
              qrtzJobsAcceptMapper.updateByPrimaryKey(accept);
          }
      }catch (Exception e) {
          e.printStackTrace();
          throw BizException.def().msg(Biz.getFullException(e));
      }
    }


    /**
     * 删除定时任务
     * @param jobDetailVo jobDetailVo
     * @author Aison
     * @date 2018/5/28 18:23
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(QrtzJobsAcceptQuery jobDetailVo) {
      try{
          String jobName = jobDetailVo.getJobName();
          String jobGroupName = jobDetailVo.getJobGroup();
          scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
          scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
          scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));

          QrtzJobsAcceptQuery query = new QrtzJobsAcceptQuery();
          query.setJobName(jobDetailVo.getJobName());
          query.setJobGroup(jobDetailVo.getJobGroup());
          List<QrtzJobsAccept> accepts =  qrtzJobsAcceptMapper.selectList(query);
          if(accepts!=null && accepts.size()>0) {
              accepts.forEach(action->{
                  action.setDelState(1);
                  action.setUpdateTime(new Date());
                  qrtzJobsAcceptMapper.updateByPrimaryKey(action);
              });
          }
      }catch (Exception e) {
          e.printStackTrace();
          throw BizException.def().msg(Biz.getFullException(e));
      }
    }

    /**
     * 重新启动job任务
     * @param jobDetailVo jobDetailVo
     * @author Aison
     * @date 2018/5/28 18:23
     */
    @Override
    public void resumeJob(QrtzJobsAcceptQuery jobDetailVo) {
        try{
            scheduler.resumeJob(JobKey.jobKey(jobDetailVo.getJobName(), jobDetailVo.getJobGroup()));
        }catch (Exception e) {
            e.printStackTrace();
            throw BizException.def().msg(Biz.getFullException(e));
        }
    }

    /**
     * 暂停job任务
     * @param jobDetailVo jobDetailVo
     * @author Aison
     * @date 2018/5/28 18:23
     */
    @Override
    public void pausejob(QrtzJobsAcceptQuery jobDetailVo) {
        try{
            scheduler.pauseJob(JobKey.jobKey(jobDetailVo.getJobName(), jobDetailVo.getJobGroup()));
        }catch (Exception e) {
            e.printStackTrace();
            throw BizException.def().msg(Biz.getFullException(e));
        }
    }

    /**
     * 分页查询任务列表
     * @param qrtzJobDetailsQuery 请求参数封装
     * @author Aison
     * @date 2018/5/29 9:11
     */
    @Override
    public MyPage<QrtzJobsAcceptQuery> qrtzJobPages(QrtzJobsAcceptQuery qrtzJobDetailsQuery) {

        return MyPage.copy2Child(qrtzJobsAcceptMapper.selectList(qrtzJobDetailsQuery),QrtzJobsAcceptQuery.class,(srouce,target)->{
           try{
               Trigger.TriggerState state = scheduler.getTriggerState(TriggerKey.triggerKey(srouce.getJobName(), srouce.getJobGroup()));
               target.setJobStatus(state.name());
           }catch (Exception e) {
               e.printStackTrace();
           }
        });
    }


    /**
     * 添加定时日志
     * @author Aison
     * @date 2018/5/29 13:16
     */
    @Override
    public void addOptLog(QrtzOptLog qrtzOptLog) {
        qrtzOptLogMapper.insertSelective(qrtzOptLog);
    }


    /**
     * 查询某个任务的操作日志 包括执行日志
     * @param query query
     * @author Aison
     * @date 2018/5/30 14:13
     */
    @Override
    public MyPage<QrtzOptLogQuery> qrtzOptLogs(QrtzOptLogQuery query) {

        return MyPage.copy2Child(qrtzOptLogMapper.selectList(query),QrtzOptLogQuery.class,(source, targetObj) -> {
            targetObj.setOptTypeName(QrtzType.getByCodeInteger(source.getOptType()));
        });
    }


    /**
     * 回调某一个job
     * @param jobName job名称
     * @param jobGroupName job分组
     * @author Aison
     * @date 2018/5/31 14:56
     */
    @Override
    public void callBack(String jobName, String jobGroupName) {

        QrtzOptLog optLog = new QrtzOptLog();
        optLog.setOptTimeStart(new Date());
        optLog.setJobName(jobName);
        optLog.setJobGroup(jobGroupName);

        try{
            QrtzJobsAcceptQuery query = new QrtzJobsAcceptQuery();
            query.setJobName(jobName);
            query.setJobGroup(jobGroupName);
            QrtzJobsAccept accept = qrtzJobsAcceptMapper.selectOne(query);
            if(accept==null) {
                throw BizException.def().msg("获取不到任务");
            }
            optLog = QrtzJobsAcceptQuery.fill(accept,optLog);
            log.info("定时任务序列化信息:{}",Biz.obToJson(accept));
            Map<String,Object> param = JSONObject.parseObject(accept.getFeedbackData());
            String result = HttpRequest.sendPostJson(accept.getFeedbackUrl(),param);
            optLog.setResult(result);
            optLog.setStatus(1);
            log.info("定时任务{}执行完成===>",jobName);
            optLog.setOptType(7);
            optLog.setOptTimeEnd(new Date());
            addOptLog(optLog);
        }catch (Exception e) {
            optLog.setStatus(0);
            optLog.setResult(Biz.getFullException(e));
            e.printStackTrace();
            log.info("执行定任务{}失败===>",jobName);
            addOptLog(optLog);
            throw  BizException.def().msg(Biz.getFullException(e));
        }
    }

}
