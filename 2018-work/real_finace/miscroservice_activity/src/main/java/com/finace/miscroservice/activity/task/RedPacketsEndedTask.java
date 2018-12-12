package com.finace.miscroservice.activity.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.service.UserRedPacketsService;
import com.finace.miscroservice.commons.annotation.DependsOnMqAndDb;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.config.TaskBuildPathConfig;
import com.finace.miscroservice.commons.entity.TimerScheduler;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TaskBuildPathEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.MD5Util;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@DependsOnMqAndDb
public class RedPacketsEndedTask {
    private static Log logger = Log.getInstance(RedPacketsEndedTask.class);

    @Autowired
    private MqTemplate mqTemplate;
    @Autowired
    private UserRedPacketsService userRedPacketsService;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private TaskBuildPathConfig taskBuildPathConfig;

    @PostConstruct
    public void init() {
        Thread thread = new Thread(() -> redPacketsEnded());
        thread.setDaemon(Boolean.TRUE);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setName("redPacketsEndedTask");
        thread.start();
    }

    @Synchronized
    public void redPacketsEnded() {

        //检测是否是同一台机器
        if (!taskBuildPathConfig.checkCanBuild(TaskBuildPathEnum.TIMER_RED_PACKETS_ENDED_TASK)) {
            return;
        }

        logger.info("【红包到期任务补发】%s：开始补发红包到期任务...");
        try {
            // 未发送过定时任务消息的红包列表(flag状态为0的)
            List<UserRedPackets> userRedPacketsList = userRedPacketsService.getEndedUserRedPackets();
            if (null != userRedPacketsList && userRedPacketsList.size() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf1 = new SimpleDateFormat("s m H d M ? y");
                SimpleDateFormat sdf2 = new SimpleDateFormat("d M ? y");
                SimpleDateFormat sdf3 = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
                TimerScheduler timerScheduler = new TimerScheduler();
                JSONObject jsonObject = new JSONObject();

                for (UserRedPackets userRedPackets : userRedPacketsList) {

                    /** 红包过期处理*/
                    timerScheduler.setType(TimerSchedulerTypeEnum.RED_PACKET_ENDED.toNum());
                    timerScheduler.setName("timer_end_red_packet"+UUIdUtil.generateUuid());
                    timerScheduler.setCron(sdf1.format(sdf.parse(userRedPackets.getHbendtime())));
                    //timerScheduler.setCron(sdf3.format(DateUtils.dateAndDayByDate(String.valueOf(Integer.valueOf(DateUtils.getNowTimeStr())+100), "0")));
                    jsonObject.put("type", 1); //1--红包过期  2--红包过期提醒
                    jsonObject.put("hbid", String.valueOf(userRedPackets.getHbid()));
                    jsonObject.put("userId", String.valueOf(userRedPackets.getUserid()));
                    timerScheduler.setParams(JSON.toJSONString(jsonObject));
                    //红包过期处理
                    mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
                    /** 红包过期处理 */

                    Date endData = this.getDateBefore(sdf.parse(userRedPackets.getHbendtime()), 3);
                    String cron = "0 0 17 "+sdf2.format(endData);
                    Long endday = DateUtils.getDistanceDays(DateUtils.getNowDateStr(),DateUtils.dateStr2(endData));
                    String userCronKey = MD5Util.getLowercaseMD5(String.valueOf(userRedPackets.getUserid())+cron);
                    if( userStrHashRedisTemplate.get(userCronKey) == null  && endday > 0){
                        /** 红包过期前3天给用户发短信提醒 */
                        jsonObject.clear();
                        jsonObject.put("type", 2);  //1--红包过期  2--红包过期提醒
                        jsonObject.put("userId", String.valueOf(userRedPackets.getUserid()));
                        jsonObject.put("hbid", String.valueOf(userRedPackets.getHbid()));
                        timerScheduler.setName("timer_end_red_packet"+UUIdUtil.generateUuid());
                        timerScheduler.setCron(cron);
                        logger.info("时间调试"+endday+"-----------"+userRedPackets.getHbendtime()+"---"+this.getDateBefore(sdf.parse(userRedPackets.getHbendtime()), 3)+"---"+cron+"------"+JSON.toJSONString(jsonObject));
                        timerScheduler.setParams(JSON.toJSONString(jsonObject));
                        mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
                        if( endday > 0 ){
                            userStrHashRedisTemplate.set(userCronKey, "sendSuccess", endday, TimeUnit.DAYS);
                        }else{
                            userStrHashRedisTemplate.set(userCronKey, "sendSuccess", 1, TimeUnit.DAYS);
                        }

                        /** 红包过期前3天给用户发短信提醒 */
                    }

                    //修改红包消息发送状态
                    userRedPacketsService.updateUserRedPacketsFlag(userRedPackets);
                }

                logger.info("红包到期任务补发：补发红包到期任务结束，共补发[{}]个红包到期任务", userRedPacketsList.size());
            } else {
                logger.info("红包到期任务补发：补发红包到期任务结束，共补发0个红包到期任务");
            }
        } catch (Exception e) {
            logger.error("红包到期任务补发：补发红包到期任务异常:{}", e);
            e.printStackTrace();
        }


    }

    /**
     * 获取时间前几天
     * @param d
     * @param day
     * @return
     */
    private Date getDateBefore(Date d, int day) throws Exception{
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

}
