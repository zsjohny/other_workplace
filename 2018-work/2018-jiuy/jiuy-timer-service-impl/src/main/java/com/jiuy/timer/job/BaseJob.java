package com.jiuy.timer.job;


import com.alibaba.fastjson.JSONObject;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.HttpRequest;
import com.jiuy.timer.model.QrtzJobsAcceptQuery;
import com.jiuy.timer.model.QrtzOptLog;
import com.jiuy.timer.services.ITimerService;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * quartz的逻辑对象
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/28 15:48
 * @Copyright 玖远网络
 */
@Log4j2
public class BaseJob implements Job, java.io.Serializable {


    @Value("${host}")
    private String host ;

    @Resource(name = "timerService")
    private ITimerService timerService;

    /**
     * 执行任务
     * @param jobExecutionContext jobExecutionContext
     * @author Aison
     * @date 2018/5/29 14:30
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("开始执行定时器====>",timerService);
        QrtzJobsAcceptQuery jobDetailVo ;
        QrtzOptLog optLog = new QrtzOptLog();
        optLog.setOptTimeStart(new Date());
        String jobName="";
        try{
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            JobKey jobKey = jobDetail.getKey();

            if("plain_f355bce97f444448973ef559b6b75721".equals(jobKey.getName())) {

                System.out.println("====>");
            }
            optLog.setJobName(jobKey.getName());
            optLog.setJobGroup(jobKey.getGroup());
            JobDataMap jobDataMap =  jobDetail.getJobDataMap();
            String data = (String) jobDataMap.get("data");
            jobDetailVo = Biz.jsonStr2Obj(data,QrtzJobsAcceptQuery.class);
            optLog = jobDetailVo.fill(optLog);
            jobName = jobDetailVo.getJobComment();

            Map<String,Object> param = JSONObject.parseObject(jobDetailVo.getFeedbackData());
            param.put("host",host);
            if(!isLoopTime(param)) {
                return ;
            }
            log.info("定时任务序列化信息:{}",data);
            String result = HttpRequest.sendPostJson(jobDetailVo.getFeedbackUrl(),param);
            optLog.setResult(result);
            // 表示执行完成
            optLog.setStatus(1);
            log.info("定时任务{}执行完成===>",jobName);
        }catch (Exception e) {
            optLog.setStatus(0);
            optLog.setResult(Biz.getFullException(e));
            e.printStackTrace();
            log.info("执行定任务{}失败===>",jobName);
        }
        optLog.setOptType(6);
        optLog.setOptTimeEnd(new Date());
        timerService.addOptLog(optLog);
    }


    private static String trueStr = "true";
    /**
     * 轮询执行是否在执行中
     *
     * @param param param
     * @author Aison
     * @date 2018/6/23 17:59
     */
    private boolean isLoopTime(Map<String,Object> param) {
        String loop = (String) param.get("loop");
        if(trueStr.equals(loop)) {
            String endTime = (String) param.get("endTime");
            String beginTime = (String) param.get("beginTime");
            Date now = new Date();
            String datePix = Biz.formatDate(now,"yyyy-MM-dd");
            if(endTime!=null && beginTime!=null) {
                endTime  = datePix+" "+endTime;
                beginTime = datePix + " " +beginTime;
                Date dateEnd = Biz.dateStr2Date(endTime,"yyyy-MM-dd HH:mm:ss");
                Date dateBegin = Biz.dateStr2Date(beginTime,"yyyy-MM-dd HH:mm:ss");
                // 只有在 这个时间区间内的才会执行
                Long nowTime = now.getTime();
                return nowTime >= dateBegin.getTime() && nowTime <= dateEnd.getTime();
            }
        }
        return true;
    }

}
