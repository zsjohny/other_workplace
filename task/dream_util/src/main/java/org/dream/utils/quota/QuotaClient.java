package org.dream.utils.quota;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.dream.utils.concurrent.Executors;
import org.dream.utils.prop.SpringProperties;
import org.dream.utils.quota.msg.XtraderSubQuotaReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Quota访问行情的Utils类 Created by nessary on 16-8-15.
 */
public class QuotaClient {

    /**
     * 日志文件
     */
    private Logger logger = LoggerFactory.getLogger(QuotaClient.class);

    private EventLoopGroup queryGroup = null;
    private Bootstrap bootstrap = null;


    private QuotaHandler handler;


    private Channel channel = null;

    private Map<String, ExecutorService> executorMap = new ConcurrentHashMap<>();

    // 用户暂存当前连接中定位的合约.
    private List<OrderQuotaInfoModel> orderQuotaInfoModels = new ArrayList<>();


    public Long timeStamp = System.currentTimeMillis();


    private Timer timer = null;

    private String ip;
    private Integer port;

    private Long delay = 10000l;
    private Integer interval = 40000;

    {


        try {

            timer = new Timer();
            timer.scheduleAtFixedRate(new restartTask(), delay, delay);
            SpringProperties properties = SpringProperties.getBean(SpringProperties.class);
            ip = properties.getProperty("sys.utils.netty.host");
            port = Integer.valueOf(properties.getProperty("sys.utils.netty.port"));


        } catch (Exception e) {

        }


    }

    public QuotaClient() {

    }

    public QuotaClient(String ip, Integer port) {

        this.ip = ip;
        this.port = port;

    }

    /**
     * 初始化访问数据_注册和登录
     *
     * @Param QuotaHandler 自定义处理的方法
     */
    public void startUp(final QuotaHandler quotaHandler) {
        try {

            if (quotaHandler == null) {
                return;
            }

            logger.info("行情订阅初始化进行中..QuotaHandler={}", quotaHandler.getClass());
            if (queryGroup == null || queryGroup.isShutdown() || queryGroup.isShuttingDown() || queryGroup.isTerminated()) {

                queryGroup = new NioEventLoopGroup(2, new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "nettyNioLoopGroup" + System.currentTimeMillis());
                    }
                });
            }

            handler = quotaHandler;
            bootstrap = new Bootstrap();
            handler.bindClient(this);
            bootstrap.group(queryGroup).channel(NioSocketChannel.class).handler(new QuotaInitializer(handler));
            //开始链接
            channel = bootstrap.connect(ip, port).sync().channel();

            logger.info("行情订阅初始化结束...QuotaHandler={}", quotaHandler.getClass());
        } catch (Exception e) {


            logger.error("初始化行情访问C++，内容出错 ", e);
        }

    }

    /**
     * * 根据信息 订阅行情信息
     *
     * @param models 订阅行情的实体类
     */
    public void orderQuotaInfoByParams(OrderQuotaInfoModel... models) {

        try {
            if (models == null) {
                return;
            }


            for (OrderQuotaInfoModel info : models) {
                if (info == null || StringUtils.isEmpty(info.getContractCode()) || StringUtils.isEmpty(info.getExchangeNo())) {
                    continue;
                }

                XtraderSubQuotaReq q = new XtraderSubQuotaReq();
                q.ExchangeNo = info.getExchangeNo();

                if (executorMap.containsKey(info.getContractCode())) {
                    continue;
                }
                q.CommodityNo = info.getContractCode().replaceAll("[\\d]", "");
                q.ContractNo1 = info.getContractCode().replaceAll("[a-zA-Z]", "");
                channel.writeAndFlush(q);
                executorMap.put(info.getContractCode(), Executors.newSingleThreadExecutor(info.getContractCode()));
                if (!orderQuotaInfoModels.contains(info)) {
                    orderQuotaInfoModels.add(info);
                }
                logger.info("行情订阅开始,市场为 {} ,合约为 {}", info.getExchangeNo(), info.getContractCode());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }


        } catch (Exception e) {
            logger.warn("订阅失败", e);
        }


    }

    /**
     * 销毁进程
     */

    public void shutdown() {

        try {

            logger.info("行情订阅开始销毁程序开始,订阅的对象={}", orderQuotaInfoModels);

            if (channel != null && channel.isActive()) {
                channel.close();
            }

            if (queryGroup != null) {

                queryGroup.shutdownGracefully();
            }

            for (ExecutorService exec : executorMap.values()) {
                exec.shutdown();

                logger.error("线程销毁为{}", exec.isShutdown());
            }

            if (timer != null) {
                timer.cancel();
            }

            logger.info("行情订阅开始销毁程序结束,订阅的对象={}", orderQuotaInfoModels);

        } catch (Exception e) {
            logger.error("销毁进程失败 出错 ", e);
        }

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


    private AtomicInteger count = new AtomicInteger(0);

    public void restart() {//restart的时候并未去关闭现有的工作线程池，所以要保证restart调用的任务方法都不是阻塞的

        logger.warn("网络链接异常导致回复系统中开始...订阅的对象={}", orderQuotaInfoModels);

        //发送邮箱提醒自己
        count.incrementAndGet();

        if (count.get() >= 5) {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HtmlEmail htmlEmail = new HtmlEmail();
                        htmlEmail.setHostName("smtp.163.com");
                        htmlEmail.setCharset("utf-8");
                        htmlEmail.setFrom(
                                "15924179757@163.com", "系统自动提醒");
                        htmlEmail.addTo("nessary@foxmail.com", "", "行情恢复失败!!");
                        htmlEmail.setSubject("行情获取失败提示");
                        htmlEmail.setAuthenticator(new DefaultAuthenticator("15924179757@163.com", "s985595"));

                        htmlEmail.setMsg("订阅的合约" + orderQuotaInfoModels + "恢复了" + count.get() + "次了，请及时修复");
                        htmlEmail.send();
                    } catch (Exception e) {

                    }
                }
            }).start();


        }

        if (channel != null && channel.isActive()) {
            channel.close();
        }
        startUp(handler);


        for (ExecutorService exec : executorMap.values()) {
            exec.shutdown();

            logger.info("线程销毁为{}", exec.isShutdown());
        }

        //去除之前订阅的信息
        executorMap.clear();


        //重新订阅信息
        orderQuotaInfoByParams(orderQuotaInfoModels.toArray(new OrderQuotaInfoModel[]{}));
        logger.warn("网络链接异常导致回复系统中结束...订阅的对象={}", orderQuotaInfoModels);
    }


    /**
     * 重启定时器任务
     */
    class restartTask extends TimerTask {

        @Override
        public void run() {
            long nowInterval = System.currentTimeMillis() - timeStamp;
            if (nowInterval > interval) {
                logger.info("行情{}正在进行重启任务的执行,时间间隔{},最大毫秒数{}", orderQuotaInfoModels, nowInterval, interval);
                restart();

            }
        }
    }

    public static void main(String[] args) {


        QuotaClient client = new QuotaClient("hq.xtrader.com.cn", 4579);
        client.startUp(new Get("test", "test1", "xt123456", "fa75b6791f8ae808794df91de8a0222a"));
        OrderQuotaInfoModel info = new OrderQuotaInfoModel();
        info.setExchangeNo("COMEX");
        info.setContractCode("GC1612");

        OrderQuotaInfoModel info2 = new OrderQuotaInfoModel();
        info2.setExchangeNo("NYMEX");
        info2.setContractCode("CL1610");

        OrderQuotaInfoModel info3 = new OrderQuotaInfoModel();
        info3.setExchangeNo("NYMEX");
        info3.setContractCode("CL1611");
        OrderQuotaInfoModel info4 = new OrderQuotaInfoModel();
        info4.setExchangeNo("EUREX");
        info4.setContractCode("FDAX1612");
        OrderQuotaInfoModel info5 = new OrderQuotaInfoModel();
        info4.setExchangeNo("HKEX");
        info4.setContractCode("HSI1612");
//        info4.setContractCode("FDAX1609");
//        client.orderQuotaInfoByParams(info, info2, info3,info2);
        client.orderQuotaInfoByParams(info3);

    }

}
