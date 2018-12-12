package org.dream.utils.quota.client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.dream.utils.quota.OrderQuotaInfoModel;
import org.dream.utils.quota.SimpleMessageCodec;
import org.dream.utils.quota.client.AbstrtactQuotoClient;
import org.dream.utils.quota.handler.QuotaDataHandleService;
import org.dream.utils.quota.msg.XtraderSubQuotaReq;
import org.dream.utils.quota.msg.XtraderUnSubQuotaReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * Created by 12 on 2016/10/27.
 */
public class InternationalClient extends AbstrtactQuotoClient {

    private static final Logger LOG = LoggerFactory.getLogger(InternationalClient.class);

    private EventLoopGroup queryGroup;
    private Bootstrap bootstrap;
    private InternatonalQuotaHandler handler;
    private Channel channel;
    private String ip;
    private Integer port;

    public InternationalClient(String  host ,int  port){
         this.ip =host;
         this.port=port;

    }

    @Override
    public void setQuotaDataHandleService(QuotaDataHandleService service) {
        this.handler = new InternationalQuotaInfoHandler(service);
    }


    public boolean startup() {
        // 重启,订阅计数重置为0
        if (isStart && channel != null) {
            LOG.info("client 已经启动了,不再重复启动");
            return false;
        }

        initOnRestart();

        try {
            if (handler == null) return false;

            LOG.info("行情订阅初始化进行中..QuotaClient={}", handler.getClass());
            if (queryGroup == null || queryGroup.isShutdown() || queryGroup.isShuttingDown() || queryGroup.isTerminated()) {
                queryGroup = new NioEventLoopGroup(2, new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "nettyNioLoopGroup" + System.currentTimeMillis());
                    }
                });
            }

            bootstrap = new Bootstrap();
            handler.bindClient(this);
            bootstrap.group(queryGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("codec", new SimpleMessageCodec());
                    ch.pipeline().addLast("handler", handler);
                }
            });

            //开始链接
            channel = bootstrap.connect(ip, port).sync().channel();

            LOG.info("行情订阅初始化结束...QuotaClient={}", handler.getClass());
        } catch (Exception e) {


            LOG.error("初始化行情访问C++，内容出错 ", e);
        }
        return true;
    }




    @Override
    public boolean destory()   {
        LOG.info("行情订阅开始销毁程序开始,订阅的对象={}", toString(orderQuotaInfoModels));

        clearAllOnDestory();

        if (channel != null && channel.isActive()) {
            channel.close();
            channel = null;
        }
        if (queryGroup != null) {
            queryGroup.shutdownGracefully();
            queryGroup = null;
        }

        LOG.info("行情订阅开始销毁程序结束,订阅的对象={}", toString(orderQuotaInfoModels));


        return true;
    }

    public synchronized boolean subscribe(OrderQuotaInfoModel... models) {

        try {
            super.subscribe(models);

            for (OrderQuotaInfoModel model : models) {
                if (model == null || StringUtils.isEmpty(model.getContractCode()) || StringUtils.isEmpty(model.getExchangeNo())) {
                    continue;
                }

                if (orderQuotaInfoModels.contains(model)) {
                    unSubscribe0(model);
                    LOG.info("行情订阅开始>>>>>> 已经存在的合约重新订阅{}", model);
                }

                XtraderSubQuotaReq q = new XtraderSubQuotaReq();
                q.ExchangeNo = model.getExchangeNo();

                if (executorMap.containsKey(model.getContractCode())) {
                    continue;
                }
                q.CommodityNo = model.getContractCode().replaceAll("[\\d]", "");
                q.ContractNo1 = model.getContractCode().replaceAll("[a-zA-Z]", "");

                if(channel != null)
                    channel.writeAndFlush(q);

                incrementSub(model);

                Thread.sleep(100);
            }


        } catch (Exception e) {
            LOG.error("订阅失败", e);
        }

        return true;
    }


    protected boolean unSubscribe0(OrderQuotaInfoModel... models) throws Exception {
        try {
            if (models == null || !isStart) {
                return false;
            }


            for (OrderQuotaInfoModel model : models) {
                if (model == null || StringUtils.isEmpty(model.getContractCode()) || StringUtils.isEmpty(model.getExchangeNo())) {
                    continue;
                }

                if (orderQuotaInfoModels.contains(model)) {
                    XtraderUnSubQuotaReq q = new XtraderUnSubQuotaReq();
                    q.ExchangeNo = model.getExchangeNo();
                    q.CommodityNo = model.getContractCode().replaceAll("[\\d]", "");
                    q.ContractNo1 = model.getContractCode().replaceAll("[a-zA-Z]", "");

                    channel.writeAndFlush(q);

                    decrementSub(model);

                } else {
                    LOG.info("行情订阅结束>>>>>> 不存在当前合约不能取消{}", model);
                }
                Thread.sleep(100);

            }


        } catch (Exception e) {
            LOG.warn("取消订阅失败" + models.toString(), e);
        }

        return true;
    }





    //restart的时候并未去关闭现有的工作线程池，所以要保证restart调用的任务方法都不是阻塞的
    public synchronized void restart()   {

        if (orderQuotaInfoModels == null || orderQuotaInfoModels.isEmpty()) {
            shutdown();
            LOG.info("行情服务重启任务>>>>>>>:没有可以订阅的行情不执行重启");
            return;
        }


        LOG.info("行情服务开始执行重启任务>>>>>>>:当前需要订阅行情{}", orderQuotaInfoModels);
        isStart = false;

        if (channel != null && channel.isActive()) {
            channel.close();
        }

        restartSendEmail(orderQuotaInfoModels);
        shutdownExecutor();

        List<OrderQuotaInfoModel> restartList = new ArrayList<>(orderQuotaInfoModels);
        orderQuotaInfoModels.clear();

        //重新订阅信息
        subscribe(restartList.toArray(new OrderQuotaInfoModel[restartList.size()]));
        LOG.info("行情服务重启任务执行完成<<<<<<<:当前订阅中行情{}", orderQuotaInfoModels);
    }


}
