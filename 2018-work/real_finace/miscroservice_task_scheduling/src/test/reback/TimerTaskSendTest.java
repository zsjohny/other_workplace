package com.finace.miscroservice;

import com.alibaba.fastjson.JSON;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.distribute_task.helper.TimeTaskHelper;
import com.finace.miscroservice.distribute_task.timerTask.ScheduleOperaEnum;
import com.finace.miscroservice.distribute_task.timerTask.TimeTask;
import com.finace.miscroservice.task_scheduling.entity.TimerTransfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimerTaskSendTest {

    @Autowired
    private TimeTaskHelper timeTaskHelper;

    @Test
    public void get() {
        TimeTask timeTask = new TimeTask();
        TimerTransfer transfer = new TimerTransfer();
        transfer.setSendType("orders6_delay_task_delay_task");
        transfer.setUuid("1111");
        User user = new User();
        user.setEmail("805687116");
        user.setUser_id(1);
        user.setAge(23);
        user.setAddress("hangzhou");
        transfer.setMsg(JSON.toJSONString(user));
        timeTask.setParams(JSON.toJSONString(transfer));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH dd  MM ?");
        timeTask.setExecuteTime(formatter.format(LocalDateTime.now().plusSeconds(10)));
        timeTask.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        timeTask.setTimeTaskName(UUID.randomUUID().toString());
        timeTaskHelper.execute(timeTask);
    }


}
