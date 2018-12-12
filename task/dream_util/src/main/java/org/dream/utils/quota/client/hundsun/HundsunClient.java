package org.dream.utils.quota.client.hundsun;

import com.hundsun.mcapi.MCServers;
import com.hundsun.mcapi.exception.MCSubscribeException;
import com.hundsun.mcapi.interfaces.ISubscriber;
import com.hundsun.mcapi.subscribe.MCSubscribeParameter;
import com.hundsun.t2sdk.impl.client.T2Services;
import org.dream.utils.concurrent.Executors;
import org.dream.utils.quota.OrderQuotaInfoModel;
import org.dream.utils.quota.client.SimpleQuotaClient;
import org.dream.utils.quota.handler.QuotaDataHandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Created by yhj on 16/11/9.
 */
public class HundsunClient extends SimpleQuotaClient {

    private static final Logger LOG = LoggerFactory.getLogger(HundsunClient.class);

    private T2Services server = null ;

    private ISubscriber subscriber = null;

    private int subscriberRe ;

    private Map<String, ExecutorService> executorMap = new ConcurrentHashMap<>();

    public HundsunClient() {

        HundsunDataHelper.setHundsunClient(this);
    }

    @Override
    public void setQuotaDataHandleService(QuotaDataHandleService service) {
        HundsunDataHelper.setQuotaDataHandleService(service);
    }

    public ExecutorService getWorkThread(String contractCode){
        return executorMap.get(contractCode);
    }

    @Override
    protected boolean isStarting() {
        return super.isStarting() && server != null && subscriber != null ;
    }

    @Override
    protected boolean startup()  {

        try {

            LOG.info("启动任务执行开始>>>>");

            super.startup();

            server = T2Services.getInstance();
            server.init();

            Thread.sleep(200);

            server.start();
            MCServers.MCInit();

            LOG.info("开始订阅所有的行情信息>>>>");
            subscriber = MCServers.GetSubscriber();

            MCSubscribeParameter subParam = new MCSubscribeParameter();
            subParam.SetTopicName("ofutu.quotation_push");
            //原样返回
            byte[] data = {1,3,5,8,9,10,2,3,0};
            subParam.SetAppData(data);
            subParam.SetFromNow(true);
            subParam.SetReplace(true);
            subParam.SetFilter("exchange_type", "");
            subParam.SetFilter("future_code", "");
            subParam.SetFilter("product_id", "");
            subscriberRe =  subscriber.SubscribeTopic(subParam, 3000);

            LOG.info("启动任务执行完成>>>>");
            return true;
        } catch (Exception e) {
            LOG.error("启动失败>>",e);
        }

        return false;
    }

    @Override
    public boolean internalSubscribe(OrderQuotaInfoModel model) throws Exception {
        LOG.info("行情订阅>>>>>> 订阅行情{}", model.getContractCode());
        HundsunDataHelper.putContractCode(model.getContractCode());
        executorMap.put(model.getContractCode(), Executors.newSingleThreadExecutor(model.getContractCode()));

        return true;
    }

    @Override
    public boolean internalUnSubscribe(OrderQuotaInfoModel param) throws Exception {
        HundsunDataHelper.removeContractCode(param.getContractCode());
        ExecutorService executorService = executorMap.remove(param.getContractCode());
        if (executorService != null) executorService.shutdown();
        LOG.info("取消订阅>>>>>> 取消行情{}", param.getContractCode());
        return true;
    }

    @Override
    protected boolean isAllowShutdown() {
        return super.isAllowShutdown() && HundsunDataHelper.isEmpty()  ;
    }

    public boolean destory() {

        try {
            LOG.info("销毁任务执行开始>>>>{}",toString(orderQuotaInfoModels));

            timerTask(false);
            shutdownExecutor();

            if(subscriber!=null){
                subscriber.CancelSubscribeTopic(subscriberRe);
                subscriber = null;
                MCServers.Destroy();
            }
           if(server!=null){
               server.stop();
               server = null ;
           }


            LOG.info("销毁任务执行结束>>>>");

            return true;
        } catch (MCSubscribeException e) {
            LOG.error(e.getMessage(),e);
            return false;
        }
    }



    protected void shutdownExecutor() {

        for (Map.Entry<String, ExecutorService> entry : executorMap.entrySet()) {

            ExecutorService exec=entry.getValue();
            exec.shutdown();
            LOG.info("线程销毁为{} 合约为{} ", exec.isShutdown(), entry.getKey());
        }
        //去除之前订阅的信息
        executorMap.clear();
    }




}
