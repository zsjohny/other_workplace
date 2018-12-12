package com.finace.miscroservice;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.TimerScheduler;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendTest {

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;

    @Test
    public void get() {

//        TimerScheduler timerScheduler = new TimerScheduler();
//        timerScheduler.setType(TimerSchedulerTypeEnum.RED_PACKET_ENDED.toNum());
//        timerScheduler.setCron("0/10 * * * * ?");
//        UserRedPackets user = new UserRedPackets();
//        user.setFlag(1);
//        user.setHbdetail("sdsds");
//        user.setHbmoney(029.00);
//        timerScheduler.setParams(new Object[]{user, user});
////        timerScheduler.setParams(new Object[]{"你好", "2"});
//
//        mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));

    }


}
