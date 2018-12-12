package org.dream.utils.quota.client;

import org.dream.utils.concurrent.Executors;
import org.dream.utils.quota.OrderQuotaInfoModel;
import org.dream.utils.quota.helper.QuotaDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 12 on 2016/10/28.
 */
public abstract class AbstrtactQuotoClient implements QuotaClient {

    private static final Logger LOG = LoggerFactory.getLogger(AbstrtactQuotoClient.class);

    protected AtomicInteger restartCount = new AtomicInteger(0);

    protected volatile boolean reStart;

    //当前已经订阅的行情数
    protected AtomicInteger subscribeCount = new AtomicInteger(0);

    protected volatile boolean isStart;
    //    protected volatile boolean isConnectSuccess ;
    protected Map<String, ExecutorService> executorMap = new ConcurrentHashMap<>();
    protected List<OrderQuotaInfoModel> orderQuotaInfoModels = new ArrayList<>();

    private Map<String, Long> timeMap = new HashMap<>();

    private Map<String, Timer> timerTaskMap = new HashMap<>();


    public static final String RESTART_TIME = "RESTART_TIME";

    public static final String RESUB_TIME = "RESUB_TIME";

    /**
     * 重启的时候调用.
     * 启动状态,订阅数为0 ,启动定时器
     */
    protected void initOnRestart() {
        isStart = true;
        subscribeCount.set(0);
        orderQuotaInfoModels.clear();
        startRestartTimer();
        startReSubTimer();
    }

    /**
     * 销毁的时候调用
     * 清理所有的缓存,定时器,启动状态重置为false
     */
    protected void clearAllOnDestory() {
        orderQuotaInfoModels.clear();
        isStart = false;
        cancelAllTimer();
        shutdownExecutor();
    }

    /**
     * 计数
     * 缓存
     *
     * @param model
     */
    protected void incrementSub(OrderQuotaInfoModel model) {
        LOG.info("行情订阅开始,合约为 {},当前已经订阅的合约{}", model.getContractCode(), toString(orderQuotaInfoModels));

        executorMap.put(model.getContractCode(), Executors.newSingleThreadExecutor(model.getContractCode()));
        orderQuotaInfoModels.add(model);
        subscribeCount.incrementAndGet();

    }


    /**
     * @param model
     */
    protected void decrementSub(OrderQuotaInfoModel model) {
        ExecutorService executorService = executorMap.remove(model.getContractCode());
        if (executorService != null) {
            executorService.shutdown();
        }

        orderQuotaInfoModels.remove(model);
        subscribeCount.decrementAndGet();


        //订阅计数-
        LOG.info("取消订阅订阅[{}]服务,线程销毁为{},当前还存在的合约{}",
                model.getContractCode(),
                executorService != null ? executorService.isShutdown() : "",
                toString(orderQuotaInfoModels));
    }


    protected abstract void restart() throws Exception;

    protected abstract boolean startup() throws Exception;



    @Override
    public synchronized boolean subscribe(OrderQuotaInfoModel... models) throws Exception {

        if (models == null) {
            return false;
        }
        startup();


        return true;
    }


    protected abstract boolean unSubscribe0(OrderQuotaInfoModel... models) throws Exception;

    public synchronized boolean unSubscribe(OrderQuotaInfoModel... models) throws Exception {

        if (models == null || shutdown()) {
            return false;
        }

        unSubscribe0(models);
        shutdown();

        return true;
    }


    protected synchronized boolean shutdown() {
        LOG.info("shutdown subscribeCount " + subscribeCount.get());

        if (isStart && subscribeCount.get() <= 0) {
            return destory();
        }
        return false;
    }

    public void updateTime(String key) {
        timeMap.put(key, System.currentTimeMillis());
    }

    public Long getRestartTime() {
        return timeMap.get(RESTART_TIME);
    }

    public Long getTime(String key) {
        return timeMap.get(key);
    }


    protected void restartSendEmail(Collection allSubscribeList) {

        restartCount.incrementAndGet();

        if (restartCount.get() > 5) {
            LOG.error("send restart email");
            QuotaDataHelper.sendReStartEmial(new ArrayList(allSubscribeList), restartCount.get());
            restartCount.set(0);
        }

    }


    protected void shutdownExecutor() {

        for (Map.Entry<String, ExecutorService> entry : executorMap.entrySet()) {

            ExecutorService exec = entry.getValue();
            exec.shutdown();
            LOG.info("线程销毁为{} 合约为{} ", exec.isShutdown(), entry.getKey());

        }
        //去除之前订阅的信息
        executorMap.clear();
    }

    protected void startRestartTimer() {
        cancelRestartTimer();

        if (!timerTaskMap.containsKey(RESTART_TIME)) {
            LOG.info("startRestartTimer");
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new RestartTask(), DELAY, DELAY);
            updateTime(RESTART_TIME);
            timerTaskMap.put(RESTART_TIME, timer);
        }
    }

    protected void cancelRestartTimer() {
        cancelTimer(RESTART_TIME);
    }


    protected void cancelAllTimer() {
        for (Timer timer : timerTaskMap.values()) {
            timer.cancel();
        }

        LOG.info("cancelTimer all timer {}", timerTaskMap);
        timerTaskMap.clear();
    }


    protected void cancelTimer(String key) {

        if (timerTaskMap.containsKey(key)) {
            LOG.info("cancelTimer  key={}", key);
            Timer timer = timerTaskMap.remove(key);
            if (timer != null) timer.cancel();
        }

    }

    protected void startReSubTimer() {
        cancelTimer(RESUB_TIME);
        LOG.info("startReSubTimer ");
        if (!timerTaskMap.containsKey(RESUB_TIME)) {
            Timer timer = new Timer();
            timer.schedule(new ResubscribeTask(), DELAY, SUB_DELAY);
            timerTaskMap.put(RESUB_TIME, timer);
        }
    }


    protected String toString(List<OrderQuotaInfoModel> list) {
        StringBuilder sb = new StringBuilder();
        for (OrderQuotaInfoModel model : list) {
            sb.append(",").append(model.getContractCode());
        }
        return sb.length() > 1 ? sb.substring(1) : "";
    }


    /**
     * 根据当前合约获取当前线程池
     *
     * @param contractCode 当前合约代码
     * @return
     */
    public ExecutorService getWork(String contractCode) {

        return executorMap.get(contractCode);
    }

    /**
     *
     * 订阅重启定时任务
     * 需要保证JMS开闭消息正确否则会一直重启
     */
    protected class ResubscribeTask extends TimerTask {
        @Override
        public void run() {

            if(orderQuotaInfoModels==null||orderQuotaInfoModels.isEmpty())return;


            for (OrderQuotaInfoModel model : new ArrayList<>(orderQuotaInfoModels)) {

                try {

                    long nowInterval = System.currentTimeMillis() - getTime(model.getContractCode());
                    if (nowInterval > SUB_INTERVAL && isStart && subscribeCount.get() > 0 && !reStart) {
                        LOG.info("订阅服务[{}]没有响应,取消订阅后重新订阅", model);
                        updateTime(model.getContractCode());
                        unSubscribe(model);
                        Thread.sleep(555);
                        subscribe(model);
                    }
                } catch (Exception e) {
                }

            }
        }


    }


    /**
     * 连接重启定时任务
     * 需要保证JMS开闭消息正确否则会一直重启
     */
    protected class RestartTask extends TimerTask {

        @Override
        public void run() {
            long nowInterval = System.currentTimeMillis() - getRestartTime();

            if (nowInterval > INTERVAL && isStart) {
                try {
                    reStart = true;
                    updateTime(RESTART_TIME);
                    LOG.info("行情正在进行重启任务的执行,时间间隔{},最大毫秒数{}", nowInterval, INTERVAL);
                    restart();
                } catch (Exception e) {
                } finally {
                    reStart = false;
                }


            }
        }
    }

}
