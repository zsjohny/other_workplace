package com.finace.miscroservice.task_scheduling;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.distribute_task.timerTask.TimeTask;
import com.finace.miscroservice.task_scheduling.dao.TimerSchedulerDao;
import com.finace.miscroservice.task_scheduling.listener.TimerSchedulerMqListener;
import com.finace.miscroservice.task_scheduling.po.TimerSchedulerPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

@Component
public class Testss {

    @Autowired
    TimerSchedulerMqListener timerSchedulerMqListener;

    @Autowired
    private TimerSchedulerDao timerSchedulerDao;

    @PostConstruct
    public void init() throws IOException {

        File file = new File("C:\\Users\\nessary\\Downloads\\libs");
        List<String> lines;
        for (File fi : file.listFiles()) {


            lines = Files.readAllLines(fi.toPath(), Charset.forName("utf-8"));


            for (String str : lines) {
                jsonArray2TimeTaskArray(str);
            }


        }


//        for (int i = 0; i < 1000; i++) {
//            String uid = UUID.randomUUID().toString();
//            new Thread(() -> {
//
//                TimerScheduler timerScheduler = new TimerScheduler();
//                timerScheduler.setName(uid);
//                timerScheduler.setParams(uid);
//                timerScheduler.setCron("* * * * * ? 2019");
//                timerScheduler.setType(1);
//                timerSchedulerMqListener.transferTo(JSONObject.toJSONString(timerScheduler));
//            }).start();
//        }
        System.err.println("_______________________________________________");
        System.err.println("_______________________________________________");
        System.err.println("_______________________________________________");
        System.err.println("_______________________________________________");


    }

    Boolean flag = Boolean.FALSE;

    /**
     * jsonArray转TimeTask数组
     *
     * @return
     */
    private void jsonArray2TimeTaskArray(String json) {

        JSONArray objects = null;
        try {
            objects = JSONObject.parseArray(json);
        } catch (Exception e) {

            return;
        }


        int len = objects.size();

        TimeTask timeTask;

        for (int i = 0; i < len; i++) {
            timeTask = objects.getObject(i, TimeTask.class);
            if (timeTask.getTimeTaskName().equals("timer_end_red_packet2825283212825076731251")) {
                flag = Boolean.TRUE;
                System.out.println("__________");
                timerSchedulerDao.saveTimerScheduler(timeTask2TimerSchedulingPo(timeTask));
            }
            if (!flag) {
                return;
            } else {

                timerSchedulerDao.saveTimerScheduler(timeTask2TimerSchedulingPo(timeTask));
            }
        }

    }

    private TimerSchedulerPO timeTask2TimerSchedulingPo(TimeTask timeTask) {
        TimerSchedulerPO timerSchedulerPO = new TimerSchedulerPO();
        timerSchedulerPO.setTimerSchedulerName(timeTask.getTimeTaskName());
        timerSchedulerPO.setUuid(UUIdUtil.generateUuid());

        timerSchedulerPO.setTimerSchedulerParam(timeTask.getParams());

        timerSchedulerPO.setTimerSchedulerCron(timeTask.getExecuteTime());

        return timerSchedulerPO;
    }


}
