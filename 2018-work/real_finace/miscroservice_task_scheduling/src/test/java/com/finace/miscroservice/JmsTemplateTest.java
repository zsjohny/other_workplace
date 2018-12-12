//package com.finace.miscroservice;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.finace.miscroservice.commons.annotation.DependsOnMq;
//import com.finace.miscroservice.commons.config.MqTemplate;
//import com.finace.miscroservice.commons.entity.TimerScheduler;
//import com.finace.miscroservice.commons.enums.MqChannelEnum;
//import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
//import com.finace.miscroservice.commons.utils.UUIdUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@DependsOnMq
//public class JmsTemplateTest {
//
//
//    @Autowired
//    private MqTemplate mqTemplate;
//
//    @Test
//    public void test() {
//        AtomicInteger atomicInteger = new AtomicInteger();
//        for (int i = 0; i < 3; i++) {
//            new Thread(() -> {
////                while (true) {
//
//
//                    TimerScheduler timerScheduler = new TimerScheduler();
//                    JSONObject jsonObject = new JSONObject();
//
//
//                    /** 红包过期处理*/
//                    timerScheduler.setType(TimerSchedulerTypeEnum.RED_PACKET_ENDED.toNum());
//                    timerScheduler.setName("timer_end_red_packet964602280964629607125" + UUIdUtil.generateUuid());
//                    timerScheduler.setCron("*/30 * 17 18 5 ? 2018");
//                    //timerScheduler.setCron(sdf3.format(DateUtils.dateAndDayByDate(String.valueOf(Integer.valueOf(DateUtils.getNowTimeStr())+100), "0")));
//                    jsonObject.put("type", 1); //1--红包过期  2--红包过期提醒
//                    jsonObject.put("hbid", 1);
//                    jsonObject.put("userId", 2);
//                    timerScheduler.setParams(JSON.toJSONString(jsonObject));
//
//                    mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
////                }
//            }).start();
//        }
////        while (true) {
////
////        }
//    }
//
//
//}
