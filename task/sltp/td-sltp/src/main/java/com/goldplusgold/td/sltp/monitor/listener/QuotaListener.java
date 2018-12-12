package com.goldplusgold.td.sltp.monitor.listener;

import com.goldplusgold.mq.msgs.DynamicQuotationBOWrapper;
import com.goldplusgold.mq.pubsub.MQSubscriber;
import com.goldplusgold.td.sltp.monitor.filter.QuotaHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 监听行情信息
 * Created by Administrator on 2017/5/12.
 */
public class QuotaListener implements MQSubscriber{
    private Logger logger = LoggerFactory.getLogger(QuotaListener.class);

    @Autowired
    private QuotaHandler quotaHandler;

    @Override
    public void onMsg(Object msg) {
        logger.info("接收到一条行情: " + msg);
        DynamicQuotationBOWrapper quotation = (DynamicQuotationBOWrapper) msg;
        if (quotation.getInstType()==null || quotation.getBo().getLastPrice()==null || quotation.getBo().getHighestPrice()==null || quotation.getBo().getLowestPrice()==null){
            logger.warn("行情数据不合法! " + quotation);
            return;
        }
        quotaHandler.handle(quotation);
    }
}
