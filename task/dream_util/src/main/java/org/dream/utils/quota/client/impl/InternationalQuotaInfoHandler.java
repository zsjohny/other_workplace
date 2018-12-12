package org.dream.utils.quota.client.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.dream.model.quota.Quota;
import org.dream.utils.log.XtraderLog;
import org.dream.utils.quota.JSON2QuotaObject;
import org.dream.utils.quota.handler.QuotaDataHandleService;
import org.dream.utils.quota.helper.QuotaCacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

import static org.dream.utils.quota.client.AbstrtactQuotoClient.RESTART_TIME;

/**
 *
 * client = xtrader;
 * 行情接口数据获取的处理类
 * Created by nessary on 16-8-16.
 */
public class InternationalQuotaInfoHandler extends InternatonalQuotaHandler {
    /**
     * 日志处理
     */
    private static final Logger logger = LoggerFactory.getLogger(InternationalQuotaInfoHandler.class);


    private QuotaDataHandleService quotaDataHandleService;

    public InternationalQuotaInfoHandler(QuotaDataHandleService quotaDataHandleService) {
        this.quotaDataHandleService = quotaDataHandleService;

    }

    @Override
    public void onQuota(String s) {

        if (StringUtils.isEmpty(s)) {
            logger.error("接口返回信息为空,直接跳过");
            return;
        }

        //记录时间--恢复用
        client.updateTime(RESTART_TIME);


        XtraderLog.log(s);

        final Quota quota = JSON2QuotaObject.getQuota2InterJson(JSON.parseObject(s));
        if (quota == null) {
            logger.error("网关推送的行情无法转化成quota对象，行情字符串为{}", s);
            return;
        }

        //更新合约定时器
        client.updateTime(quota.getInstrumentId());
        ExecutorService executorService = client.getWork(quota.getInstrumentId());

        if (executorService != null) {

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    try {

                        String instrumentId = QuotaCacheHelper.quotaCode2VarietyCode.get(quota.getInstrumentId());

                        if (StringUtils.isEmpty(instrumentId)) {
                            logger.debug("行情数据转化instrumentId{} 为空,进行过滤{}", quota.getInstrumentId(), QuotaCacheHelper.quotaCode2VarietyCode);
                            return;
                        }
                        quota.setInstrumentId(instrumentId);




//                            if (quota.getAskPrice().equals(0) || quota.getAskVolume().equals(0) || quota.getBidPrice().equals(0) || quota.getBidVolume().equals(0)) {
//                                logger.debug("买一或者卖一的价格或者量值,没有进行过滤{}",quota);
//                                return;
//                            }

                        quotaDataHandleService.quotaHandle(quota);


                        } catch (Exception e) {
                            logger.error("线程池定时启动访合约数据出错", e);
                            return;
                        }

                    }
                });
            }else{
                logger.info("找不到{}的处理线程池",quota.getInstrumentId());
            }


    }
}
