package org.dream.utils.quota.client;

import org.dream.utils.quota.OrderQuotaInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yhj on 16/11/9.
 */
public abstract class SimpleQuotaClient implements QuotaClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleQuotaClient.class);

    //前已经订阅的行情数
    protected AtomicInteger subscribeCount = new AtomicInteger(0);
    // 当前已经订阅的行情 缓存
    protected Set<OrderQuotaInfoModel> orderQuotaInfoModels = new HashSet<>();

    protected boolean starting ;// 当前开始状态

    protected long lastUpdateTime = System.currentTimeMillis();
    private Timer timer = null;

    /**
     * 启动连接
     *
     * @return
     * @throws Exception
     */
    protected boolean startup() {

        starting = true;
        timerTask(true);
        subscribeCount.set(0);
        orderQuotaInfoModels.clear();

        return false;
    }


    /**
     * 内部处理, 只是做业务的处理
     *
     * @param param
     * @return
     * @throws Exception
     */
    public abstract boolean internalSubscribe(OrderQuotaInfoModel param) throws Exception;


    /**
     * 内部处理, 只是做业务的处理
     *
     * @param params
     * @return
     * @throws Exception
     */
    public abstract boolean internalUnSubscribe(OrderQuotaInfoModel params) throws Exception;


    /**
     * @return
     * @throws Exception
     */
    public abstract boolean destory();


    /**
     * 判断是否连接中
     *
     * @return
     */
    protected boolean isStarting() {

        return starting;
    }


    /**
     * @return
     */
    protected boolean isAllowShutdown() {

        return subscribeCount.get() == 0 && orderQuotaInfoModels.isEmpty();
    }


    /**
     * @param start true ,开启定时任务,false ,停止
     */
    protected void timerTask(boolean start) {

        if (timer != null) {
            LOG.info("取消连接定时器任务>>>>");
            timer.cancel();
            timer=null;
        }

        if (start) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RestartTask(), DELAY, DELAY);
            LOG.info("开启连接定时器任务>>>>");

        }

    }

    /**
     *
     */
    public void flushLastUpdateTime() {
        lastUpdateTime = System.currentTimeMillis();
    }


    /**
     * 判断 参数,并且进行计数
     *
     * @param params
     * @return
     * @throws Exception
     */
    public synchronized boolean subscribe(OrderQuotaInfoModel... params) throws Exception {

        if (params == null || params.length == 0) {
            return false;
        }

        checkAndStartup();
        LOG.info("订阅行情>> 开始订阅{}", toString(Arrays.asList(params)));
        for (OrderQuotaInfoModel model : params) {

            subscribeCount.incrementAndGet();
            orderQuotaInfoModels.add(model);

            internalSubscribe(model);
        }

        return true;
    }


    /**
     * 判断 参数,并且进行计数
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public synchronized boolean unSubscribe(OrderQuotaInfoModel... params) throws Exception {

        if (params == null || params.length == 0) {
            return false;
        }
        LOG.info("取消订阅>> 开始取消{}", toString(Arrays.asList(params)));
        for (OrderQuotaInfoModel model : params) {

            subscribeCount.decrementAndGet();
            orderQuotaInfoModels.remove(model);

            internalUnSubscribe(model);
        }
        checkAndDestory();

        return true;
    }


    protected void checkAndStartup() {
        LOG.info("检查是否启动中,当前状态{},true:已经启动.", isStarting());
        if (isStarting()) {
            return;
        }
        startup();
    }

    protected void checkAndDestory() {
        LOG.info("检查是否启动中,当前状态{}-{},true:已经启动中并且允许销毁.", isAllowShutdown(), isStarting());
        if (isAllowShutdown() && isStarting()) {
            destory();
            starting = false;
        }

    }


    protected synchronized boolean restart() throws Exception {
        if (isAllowShutdown()) {
            LOG.info("行情服务重启任务>>>>>>>:没有可以订阅的行情执行销毁");
            destory();
            return false;
        }

        LOG.info("行情服务开始执行重启任务>>>>>>>:当前需要订阅行情{}", toString(orderQuotaInfoModels));

        destory();
        starting = false;
        Thread.sleep(200);

        OrderQuotaInfoModel[] startList = orderQuotaInfoModels.toArray(new OrderQuotaInfoModel[orderQuotaInfoModels.size()]);
        orderQuotaInfoModels.clear();
        subscribe(startList);

        LOG.info("行情服务重启任务执行完成<<<<<<<:当前订阅中行情{}", toString(orderQuotaInfoModels));

        return true;
    }


    public String toString(Collection<OrderQuotaInfoModel> list) {
        StringBuilder sb = new StringBuilder();
        for (OrderQuotaInfoModel model : list) {
            sb.append(",").append(model.getContractCode());
        }
        return sb.length() > 1 ? sb.substring(1) : "";
    }


    /**
     * 连接重启定时任务
     */
    class RestartTask extends TimerTask {

        @Override
        public void run() {
            long nowInterval = System.currentTimeMillis() - lastUpdateTime;

            if (nowInterval > INTERVAL && starting) {
                try {
                    LOG.info("行情正在进行重启任务开始执行,时间间隔{},最大毫秒数{}", nowInterval, INTERVAL);
                    flushLastUpdateTime();
                    restart();
                } catch (Exception e) {
                    LOG.error("重启失败>>", e);
                }


            }
        }
    }


}
