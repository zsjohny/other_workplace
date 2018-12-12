package com.finace.miscroservice.task_scheduling.task;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.annotation.DependsOnMqAndDb;
import com.finace.miscroservice.commons.config.*;
import com.finace.miscroservice.commons.current.ExecutorService;
import com.finace.miscroservice.commons.current.ExecutorTask;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TaskBuildPathEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.distribute_task.helper.TimeTaskHelper;
import com.finace.miscroservice.distribute_task.timerTask.ScheduleOperaEnum;
import com.finace.miscroservice.distribute_task.timerTask.TimeTask;
import com.finace.miscroservice.distribute_task.util.CronUtil;
import com.finace.miscroservice.task_scheduling.dao.TimerSchedulerDao;
import com.finace.miscroservice.task_scheduling.entity.TimerSchedulerDelayTask;
import com.finace.miscroservice.task_scheduling.entity.TimerTransfer;
import com.finace.miscroservice.task_scheduling.po.TimerSchedulerPO;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.finace.miscroservice.commons.enums.MqNameEnum.*;

/**
 * 定时任务的处理类
 */
@Component
@DependsOnMqAndDb
public class TimerSchedulerTask implements BeanFactoryAware, DisposableBean {

    public static final String DELAY_TASK_NAME_PREFIX = "_delay_task";


    private static Log logger = Log.getInstance(TimerSchedulerTask.class);


    @Autowired
    private MqTemplate mqTemplate;


    @Autowired
    private TimerSchedulerDao timerSchedulerDao;


    @Autowired
    private TimeTaskHelper timeTaskHelper;

    @Autowired
    private TaskBuildPathConfig taskBuildPathConfig;

    /**
     * 恢复数据
     */
    @PostConstruct
    public void init() {
        doTask();

    }


    /**
     * 执行工作
     */
    public void doTask() {

        //赋值mq
        mqTemplates = mqTemplate;


        logger.info("开始实例化所有延迟任务");
        initAllDelayTask();
        logger.info("成功实例化所有延迟任务");


        //检测是否是同一台机器
        if (!taskBuildPathConfig.checkCanBuild(TaskBuildPathEnum.TIMER_SCHEDULING_TASK_PATH)) {
            return;
        }


        //查询数据库没有过期的数据
        List<TimerSchedulerPO> timerSchedulerPos =
                timerSchedulerDao.findTimerSchedulerAllByNotExpire();


        if (timerSchedulerPos == null || timerSchedulerPos.isEmpty()) {
            logger.info("检测数据库没有待恢复的定时任务");
            return;
        }

        logger.info("开始恢复定时任务");

        List<TimeTask> timeTasks = new ArrayList<>(timerSchedulerPos.size());

        for (TimerSchedulerPO timerSchedulerPO : timerSchedulerPos) {


            //检测是否是延迟任务
            if (StringUtils.isEmpty(timerSchedulerPO.getTimerSchedulerCron())) {
                //延迟队列
                doDelayTask(timerSchedulerPO);
            } else {
                //定时任务
                //检测是否过期
                if (!checkValidByCron(timerSchedulerPO.getTimerSchedulerCron())) {
                    deleteTimerScheduler(timerSchedulerPO.getUuid());
                    continue;
                }
                //添加待消费的对象
                timeTasks.add(timerSchedulingPo2TimeTask(timerSchedulerPO, Boolean.TRUE));

            }


            logger.info("have recover job = {} success...", timerSchedulerPO.getTimerSchedulerName());
        }

        logger.info("成功恢复定时任务");
        timeTaskHelper.restart(timeTasks);


    }


    /**
     * 开始执行工作任务
     */
    public void startJob(TimerSchedulerPO timerSchedulerPO) {
        if (timerSchedulerPO == null || timerSchedulerPO.wasEmpty()) {
            logger.warn("执行工作任务={} 的参数为空", timerSchedulerPO);
            return;
        }

        timeTaskHelper.execute(timerSchedulingPo2TimeTask(timerSchedulerPO, Boolean.FALSE));


        logger.info("实例化定时任务Id={} 成功", timerSchedulerPO.getUuid());
    }


    /**
     * 检测cron是否过期 true是有效 false不是
     */
    private boolean checkValidByCron(String cron) {


        boolean validFlag = CronUtil.checkValidCron(cron);
        if (validFlag) {
            logger.info("cron={} 表达式验证有效", cron);
        } else {
            logger.info("cron={} 表达式已经失效", cron);
        }


        return validFlag;


    }


    /**
     * 删除定时任务
     *
     * @param uuid 定时任务的Id
     */
    private void deleteTimerScheduler(String uuid) {
        logger.info("开始删除定时任务ID={}", uuid);

        timerSchedulerDao.removeTimerSchedulerByUUId(uuid);

    }

    /**
     * 初始化所有的延迟任务监听
     */
    public void initAllDelayTask() {
        TimerSchedulerTypeEnum[] values = TimerSchedulerTypeEnum.values();

        for (TimerSchedulerTypeEnum typeEnum : values) {
            if (typeEnum.toNum() < 0) {
                //-1用来获取正的执行时间
                initDelayJob(typeEnum.toChar() + DELAY_TASK_NAME_PREFIX, -1L * typeEnum.toNum());
                logger.info("初始化 延迟任务={} 成功", typeEnum.toChar());
            }
        }


    }


    /**
     * 初始化延迟队列任务
     *
     * @param delayTaskPrefix 延迟任务名称前缀
     * @param delayTime       延迟时间(单位是毫秒)
     */
    public void initDelayJob(String delayTaskPrefix, Long delayTime) {

        if (delayTime == null || delayTime < 0 || StringUtils.isEmpty(delayTaskPrefix)) {
            logger.warn("所传的延迟任务参数不符合规范 delayTaskPrefix={} , delayTime={}", delayTaskPrefix, delayTime);
            return;
        }

        try {

            MqManager manager = new MqManager();
            MqConfig mqConfig = new MqConfig();

            //注册ttl的fanout
            mqConfig.setFactory((DefaultListableBeanFactory) beanFactory);


            mqConfig.setQueueName(delayTaskPrefix + QUEUE_NAME_SUFFIX.toName());
            mqConfig.setExchangeName(delayTaskPrefix + EXCHANGER_NAME_SUFFIX.toName());
            mqConfig.setRoutingKeyName(delayTaskPrefix + ROUTING_KEY_NAME_SUFFIX.toName());
            String diedPrefix = delayTaskPrefix + "_died";

            mqConfig.setTTL_QUEUE_NAME(diedPrefix + QUEUE_NAME_SUFFIX.toName());
            mqConfig.setTTL_EXCHANGE_NAME(diedPrefix + EXCHANGER_NAME_SUFFIX.toName());
            mqConfig.setTTL_ROUTING_KEY_NAME(diedPrefix + ROUTING_KEY_NAME_SUFFIX.toName());
            mqConfig.setTTL_DELAY_TIME(delayTime);
            manager.setMqConfig(mqConfig);
            manager.registerFanoutTTl();

            //注册监听任务类
            ClassPool pool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(this.getClass());
            pool.insertClassPath(classPath);
            CtClass ctClass = pool.get(TimerSchedulerDelayJob.class.getCanonicalName());
            ctClass.setName(TimerSchedulerDelayJob.class.getSimpleName() + "_" + delayTaskPrefix);

            MqListenerConvert timerSchedulerDelayJob = (MqListenerConvert) manager.registerAndGetMqListenerConvert(ctClass.toClass());

            //注册dead的fanout
            mqConfig = new MqConfig();
            mqConfig.setFactory((DefaultListableBeanFactory) beanFactory);
            mqConfig.setQueueName(diedPrefix + QUEUE_NAME_SUFFIX.toName());
            mqConfig.setExchangeName(diedPrefix + EXCHANGER_NAME_SUFFIX.toName());
            mqConfig.setRoutingKeyName(diedPrefix + ROUTING_KEY_NAME_SUFFIX.toName());
            mqConfig.setMqListenerConvert(timerSchedulerDelayJob);
            manager.setMqConfig(mqConfig);
            manager.registerFanoutListener();

            logger.info("执行ttl={}延迟队列任务成功 延迟时间为={}", delayTaskPrefix, delayTime);


        } catch (Exception e) {
            logger.error("执行ttl={}延迟队列任务成功 延迟时间为={} 任务出错", delayTaskPrefix, delayTime, e);

        }
    }


    private static ExecutorService sendTimerSchedulerExecutor = new ExecutorService(Runtime.getRuntime().availableProcessors() << 1, "timerSchedulerTask");


    /**
     * 发送定时任务的参数
     *
     * @param channelSuffix 发送通道的后缀
     * @param data          发送的数据
     */
    public static void sendTimerScheduler(String channelSuffix, String data) {
        sendTimerSchedulerExecutor.addTask(new ExecutorTask() {
            @Override
            public void doJob() {
                logger.info("开始发送定时任务参数={}", data);
                mqTemplates.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_SEND_.toName() + channelSuffix, data);
            }
        });


    }


    /**
     * 执行延迟队列任务
     *
     * @param timerSchedulerPO 延迟队列类
     */
    private void doDelayTask(TimerSchedulerPO timerSchedulerPO) {

        //创建延迟队列发送对象
        TimerSchedulerDelayTask schedulerDelayTask = new TimerSchedulerDelayTask();
        schedulerDelayTask.setChannelName(TimerSchedulerTypeEnum.num2Char(timerSchedulerPO.getTimerSchedulerType()));
        schedulerDelayTask.setUuid(timerSchedulerPO.getUuid());
        schedulerDelayTask.setSendContent(timerSchedulerPO.getTimerSchedulerParam());


        //发送延迟队列 延迟队列的通道加DELAY_TASK_NAME_PREFIX即可
        mqTemplates.sendMsg(schedulerDelayTask.getChannelName() + DELAY_TASK_NAME_PREFIX, JSONObject.toJSONString(schedulerDelayTask));
        logger.info("成功恢复延迟队列任务={}", timerSchedulerPO.getUuid());

    }


    private BeanFactory beanFactory;

    private static MqTemplate mqTemplates;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    /**
     * 定时任务实体类转换调度中心执行实体类
     *
     * @param timerSchedulerPO 定时任务实体类
     * @param isRecovery       是否是恢复过来的数据 true是 false不是
     * @return
     */
    private TimeTask timerSchedulingPo2TimeTask(TimerSchedulerPO timerSchedulerPO, Boolean isRecovery) {
        TimeTask timeTask = new TimeTask();
        timeTask.setTimeTaskName(timerSchedulerPO.getTimerSchedulerName());
        if (isRecovery) {
            timeTask.setParams(timerSchedulerPO.getTimerSchedulerParam());
        } else {

            TimerTransfer transfer = new TimerTransfer();
            transfer.setUuid(timerSchedulerPO.getTimerSchedulerName());
            transfer.setMsg(timerSchedulerPO.getTimerSchedulerParam());
            transfer.setSendType(TimerSchedulerTypeEnum.num2Char(timerSchedulerPO.getTimerSchedulerType()));
            timeTask.setParams(JSONObject.toJSONString(transfer));
        }
        timeTask.setExecuteTime(timerSchedulerPO.getTimerSchedulerCron());
        timeTask.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        return timeTask;
    }


    @Override
    public void destroy() throws Exception {
        sendTimerSchedulerExecutor.shutdown();
    }

}
