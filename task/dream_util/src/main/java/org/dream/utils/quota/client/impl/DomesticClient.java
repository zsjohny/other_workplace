package org.dream.utils.quota.client.impl;

import com.alibaba.fastjson.JSONObject;
import net.jctp.*;
import org.apache.commons.lang3.StringUtils;
import org.dream.model.quota.Quota;
import org.dream.utils.log.CtpLog;
import org.dream.utils.math.Arith;
import org.dream.utils.quota.OrderQuotaInfoModel;
import org.dream.utils.quota.client.AbstrtactQuotoClient;
import org.dream.utils.quota.handler.QuotaDataHandleService;
import org.dream.utils.quota.helper.QuotaCacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.dream.utils.quota.SimpleDateFormatUtil.*;

/**
 * client=ctp
 * Created by yhj on 16/10/26.
 */
public class DomesticClient extends AbstrtactQuotoClient {
    private static final Logger LOG = LoggerFactory.getLogger(DomesticClientListener.class);

    private String domesticFrontUrl;
    private String domesticBrokerId;
    private String domesticUserId;
    private String domesticPassword;

    private QuotaDataHandleService quotaDataHandleService;

    private MdApi mdApi;


    public DomesticClient(String frontUrl, String brokerId, String userId, String pwd) {
        try {
            this.domesticBrokerId = brokerId;
            this.domesticFrontUrl = frontUrl;
            this.domesticUserId = userId;
            this.domesticPassword = pwd;

        } catch (Throwable e) {
            LOG.error("实例化失败", e);
        }
    }

    @Override
    public void setQuotaDataHandleService(QuotaDataHandleService quotaDataHandleService) {
        this.quotaDataHandleService = quotaDataHandleService;
    }

    public boolean startup() throws Exception {
        // 如果当前已经启动了,那么就不在连接了
        if (isStart && mdApi != null) {
            LOG.info("client 已经启动了,不再重复启动");
            return false;
        }

        initCTP();
        initOnRestart();

        //这里其实不是异步
        mdApi.SyncConnect(domesticFrontUrl, domesticBrokerId, domesticUserId, domesticPassword);
        LOG.info("启动连接行情服务器成功! ");
        Thread.sleep(300);

        return true;
    }

    @Override
    public boolean destory() {
        LOG.info("行情订阅开始销毁程序开始,订阅的对象={}", toString(orderQuotaInfoModels));
        clearAllOnDestory();

        if (mdApi != null){
            mdApi.Close();
            mdApi = null;
        }

        LOG.info("行情订阅开始销毁程序结束,订阅的对象={}", toString(orderQuotaInfoModels));
        return true;
    }

    @Override
    public synchronized boolean subscribe(OrderQuotaInfoModel... models) throws Exception {

        super.subscribe(models);

        for (OrderQuotaInfoModel model : models) {

            if (orderQuotaInfoModels.contains(model)) {
                unSubscribe0(models);
                LOG.info("行情订阅开始>>>>>> 已经存在的合约取消订阅{}", model);
            }
            mdApi.SubscribeMarketData(new String[]{model.getContractCode()});

            incrementSub(model);
        }


        return true;
    }


    protected boolean unSubscribe0(OrderQuotaInfoModel[] models) throws Exception {
        if (models == null || !isStart) {
            return false;
        }

        for (OrderQuotaInfoModel model : models) {

            if (orderQuotaInfoModels.contains(model)) {
                mdApi.UnSubscribeMarketData(new String[]{model.getContractCode()});
                decrementSub(model);
            } else {
                LOG.info("行情订阅结束>>>>>> 不存在当前合约不能取消{}", model);
            }

        }
        return true;
    }


    public synchronized void restart() throws Exception {


        if (orderQuotaInfoModels == null || orderQuotaInfoModels.isEmpty()) {
            LOG.info("行情服务重启任务>>>>>>>:没有可以订阅的行情执行销毁");
            shutdown();
            return;
        }

        isStart = false;
        if (mdApi != null) {
            mdApi.Close();
            mdApi = null;
        }

        LOG.info("行情服务开始执行重启任务>>>>>>>:当前需要订阅行情{}", toString(orderQuotaInfoModels));

        restartSendEmail(orderQuotaInfoModels);
        shutdownExecutor();

        Set<OrderQuotaInfoModel> reStartorderQuotaInfoModels = new HashSet<>(orderQuotaInfoModels);
        orderQuotaInfoModels.clear();
        subscribe(reStartorderQuotaInfoModels.toArray(new OrderQuotaInfoModel[reStartorderQuotaInfoModels.size()]));

        LOG.info("行情服务重启任务执行完成<<<<<<<:当前订阅中行情{}", toString(orderQuotaInfoModels));
    }

    private void initCTP() {
        if (mdApi != null) {
            mdApi.Close();
        }

        mdApi = new MdApi("", false, false);
        mdApi.setListener(new DomesticClientListener(quotaDataHandleService));
    }


///>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   DomesticClientListener>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    class DomesticClientListener implements MdApiListener {


        private QuotaDataHandleService quotaDataHandleService;

        DomesticClientListener(QuotaDataHandleService quotaMessageHandler) {
            this.quotaDataHandleService = quotaMessageHandler;
        }

        @Override
        public void OnFrontConnected() {

        }

        @Override
        public void OnFrontDisconnected(int i) {

        }

        @Override
        public void OnHeartBeatWarning(int i) {

        }

        @Override
        public void OnRspUserLogin(CThostFtdcRspUserLoginField cThostFtdcRspUserLoginField, CThostFtdcRspInfoField cThostFtdcRspInfoField, int i, boolean b) {
            updateTime(RESTART_TIME);
//            isConnectSuccess = true;
            LOG.info("ctp login succ");


        }

        @Override
        public void OnRspUserLogout(CThostFtdcUserLogoutField cThostFtdcUserLogoutField, CThostFtdcRspInfoField cThostFtdcRspInfoField, int i, boolean b) {
//            isConnectSuccess = false;
        }

        @Override
        public void OnRspError(CThostFtdcRspInfoField cThostFtdcRspInfoField, int i, boolean b) {
            LOG.info("OnRspError " + JSONObject.toJSON(cThostFtdcRspInfoField));
        }

        @Override
        public void OnRspSubMarketData(CThostFtdcSpecificInstrumentField cThostFtdcSpecificInstrumentField, CThostFtdcRspInfoField cThostFtdcRspInfoField, int i, boolean b) {
            updateTime(RESTART_TIME);

            LOG.info("OnRspSubMarketData");
        }

        @Override
        public void OnRspUnSubMarketData(CThostFtdcSpecificInstrumentField cThostFtdcSpecificInstrumentField, CThostFtdcRspInfoField cThostFtdcRspInfoField, int i, boolean b) {
            updateTime(RESTART_TIME);

            LOG.info("OnRspUnSubMarketData");
        }

        @Override
        public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField data) {
            updateTime(RESTART_TIME);

            CtpLog.log( data);


            if (data != null) {
                final Quota quota = new Quota();
                quota.setInstrumentId(data.InstrumentID);
                quota.setExchangeId(data.ExchangeID);
                quota.setAskPrice(data.AskPrice1);
                quota.setAskVolume(data.AskVolume1);
                quota.setBidPrice(data.BidPrice1);
                quota.setBidVolume(data.BidVolume1);
                quota.setLastPrice(data.LastPrice);
                quota.setPreSetPrice(data.PreSettlementPrice);
                quota.setPreClsPrice(data.PreClosePrice);
                quota.setPreOpenInterest(data.PreOpenInterest);
                quota.setOpenPrice(data.OpenPrice);
                quota.setHighestPrice(data.HighestPrice);
                quota.setLowestPrice(data.LowestPrice);
                quota.setVolume((double) data.Volume);
                quota.setTurnover(data.Turnover);
                quota.setOpenInterest(data.OpenInterest);
                quota.setPositionVolume((long) data.OpenInterest);
                quota.setSettlePrice(Double.isInfinite(data.SettlementPrice) ? 0 : data.SettlementPrice);
                quota.setUpLimitPrice(Double.isInfinite(data.UpperLimitPrice) ? 0 : data.UpperLimitPrice);
                quota.setDownLimitPrice(Double.isInfinite(data.LowerLimitPrice) ? 0 : data.LowerLimitPrice);

                quota.setUpDropPrice(Arith.subtract(6, data.LastPrice, data.PreSettlementPrice));
                quota.setUpDropSpeed(Arith.divides(10, Arith.subtract(6, data.LastPrice, data.PreSettlementPrice), data.PreSettlementPrice));

                try {
                    //使用转化前的合约代码更新定时器
                    updateTime(data.InstrumentID);

                    quota.setUpTime(DATE_FORMAT.parse(data.ActionDay + " " + data.UpdateTime + "." + data.UpdateMillisec).getTime());
                    quota.setUpTimeFormat(DATE_FORMAT3.format(quota.getUpTime()));
                    quota.setTradeDay(DATE_FORMAT2.format(DATE_FORMAT1.parse(data.ActionDay)));

                } catch (ParseException e) {
                    LOG.error(" parse date error ", e);
                }

                //FIXME 这个数据没找到
                quota.setPrePositionVolume(0L);

                ExecutorService executorService = executorMap.get(data.InstrumentID);
                if (executorService == null) return;

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {

                        String instrumentId = QuotaCacheHelper.quotaCode2VarietyCode.get(quota.getInstrumentId());

                        if (StringUtils.isEmpty(instrumentId)) {
                            LOG.debug("行情数据转化instrumentId{} 为空,进行过滤{}", quota.getInstrumentId(), QuotaCacheHelper.quotaCode2VarietyCode);
                            return;
                        }
                        quota.setInstrumentId(instrumentId);

//                        if (quota.getAskPrice().equals(0) || quota.getAskVolume().equals(0) || quota.getBidPrice().equals(0) || quota.getBidVolume().equals(0)) {
//                            LOG.debug("买一或者卖一的价格或者量值,没有进行过滤{}", quota);
//                            return;
//                        }

                        quotaDataHandleService.quotaHandle(quota);
                    }
                });
            }


        }

        @Override
        public void OnRspSubForQuoteRsp(CThostFtdcSpecificInstrumentField
                                                cThostFtdcSpecificInstrumentField, CThostFtdcRspInfoField cThostFtdcRspInfoField, int i, boolean b) {

        }

        @Override
        public void OnRspUnSubForQuoteRsp(CThostFtdcSpecificInstrumentField
                                                  cThostFtdcSpecificInstrumentField, CThostFtdcRspInfoField cThostFtdcRspInfoField, int i, boolean b) {

        }

        @Override
        public void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField cThostFtdcForQuoteRspField) {


        }
    }


}
