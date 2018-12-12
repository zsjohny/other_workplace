//package com.finace.miscroservice.quartz;
//
//import com.finace.miscroservice.task_scheduling.distribute_task.timerTask.ScheduleOperaEnum;
//import com.finace.miscroservice.task_scheduling.distribute_task.timerTask.TimeTask;
//import com.finace.miscroservice.task_scheduling.distribute_task.timerTask.TimeTaskBus;
//import com.finace.miscroservice.task_scheduling.distribute_task.timerTask.TimeTaskJob;
//
//import java.util.UUID;
//
//public class QuartzTest extends TimeTaskJob {
//    @Override
//    public void job(String params) {
//
//        System.out.println("into.,,," + params);
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        TimeTaskBus bus = new TimeTaskBus(new QuartzTest());
//
//        String uuid = UUID.randomUUID().toString();
////        TimeTask timeTask = new TimeTask();
////        timeTask.setParams("12345");
////
////        timeTask.setExecuteTime("0/5 * * *  * ?");
////        timeTask.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
////        timeTask.setTimeTaskName(uuid);
////        bus.execute(timeTask);
////        Thread.sleep(7000);
////        timeTask = new TimeTask();
////        timeTask.setTimeTaskName(uuid);
////        timeTask.setParams("23456");
////        timeTask.setExecuteTime("0/5 * * *  * ?");
////        timeTask.setScheduleOperaEnum(ScheduleOperaEnum.UPDATE_TASK);
////        bus.execute(timeTask);
////        Thread.sleep(7000);
//        new Thread(() -> {
//            for (int i = 0; i < 10000000; i++) {
//                TimeTask t = new TimeTask();
//                t.setTimeTaskName(uuid);
//                t.setTimeTaskName(UUID.randomUUID().toString());
//                t.setParams("45678");
//                t.setExecuteTime("0/10 * * *  * ?");
//                t.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
//                bus.execute(t);
//            }
//        }).start();
//
//        new Thread(() -> {
////            LockSupport.parkNanos(1000*1000);
//            for (int i = 0; i < 10000000; i++) {
//                TimeTask t = new TimeTask();
//                t.setTimeTaskName(uuid);
//                t.setTimeTaskName(UUID.randomUUID().toString());
//                t.setParams("123456");
//                t.setExecuteTime("0/15 * * *  * ?");
//                t.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
//                bus.execute(t);
//            }
//        }).start();
//
//        new Thread(() -> {
////            LockSupport.parkNanos(1000 * 1000*1000);
//            for (int i = 0; i < 10000000; i++) {
//                TimeTask t = new TimeTask();
//                t.setTimeTaskName(uuid);
//                t.setTimeTaskName(UUID.randomUUID().toString());
//                t.setParams("78910");
//                t.setExecuteTime("0/20 * * *  * ?");
//                t.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
//                bus.execute(t);
//            }
//
//        }).start();
//
//    }
//
//}
