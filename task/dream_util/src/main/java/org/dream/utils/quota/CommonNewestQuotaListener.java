package org.dream.utils.quota;

import java.text.ParseException;

import org.dream.model.quota.Quota;
import org.dream.model.quota.SimpleQuota;
import org.dream.utils.jms.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * 公共的最新行情接口
 * 
 * <strong>行情中买一价、卖一价和最新价不保证不为空，因为行情数据转化时可能为正无穷大，使用时请注意</strong>
 * 
 * 需要的地方只需要注入一下这个接口就好如：
 * 
 * 
  	<bean id="hq2mqRecver" class="org.apache.activemq.command.ActiveMQTopic">
		<property name="physicalName" value="hq2mq" />
	</bean>
  	<bean id="commonNewestQuotaListener" class="org.dream.utils.quota.CommonNewestQuotaListener" />
	<bean id="jmsContainer" class="org.dream.utils.jms.JmsContainer">
		<property name="connectionFactory" ref="connectionFactory" />
		<property name="msgListeners">
			<map>
				<entry key-ref="hq2mqRecver" value-ref="commonNewestQuotaListener" />
			</map>
		</property>
	</bean>
	
 *
 *调用时
 * @author wangd
 *
 */
@Deprecated
public class CommonNewestQuotaListener implements Listener {

    Logger logger = LoggerFactory.getLogger(CommonNewestQuotaListener.class);
    @Override
    public void onMessage(String message) {
	 try {
	     Quota quota = JSON2QuotaObject.json2Quota(message);
	    
	    if (quota.getAskPrice().isInfinite()) {
		quota.setAskPrice(null);
	    }
	    
	    if (quota.getBidPrice().isInfinite()) {
		quota.setBidPrice(null);
	    }
	    
	    if (quota.getLastPrice().isInfinite()) {
		quota.setLastPrice(null);
	    }
	    
	    SimpleQuota simpleQuota = JSON2QuotaObject.map2SimpleQuota(quota);
	    Quota tempQuota = new Quota();
	    BeanUtils.copyProperties(quota, tempQuota);
	    SimpleQuota.newestQuota.put(quota.getInstrumentId(), quota);
	    SimpleQuota.newestQuota.put(quota.getInstrumentId() + "_simple", simpleQuota);
	    
	    
	} catch (ParseException e) {
	    logger.error("解析行情数据错误，{}", e.getMessage());
	    
	}

    }

}
