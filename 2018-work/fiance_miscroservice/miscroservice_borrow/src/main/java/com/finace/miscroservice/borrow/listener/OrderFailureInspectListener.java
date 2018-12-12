package com.finace.miscroservice.borrow.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.service.BorrowService;
import com.finace.miscroservice.borrow.service.FinanceBidService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouCloseOrderService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouOrderCheckService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.TimerScheduler;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 判断订单是否成功处理
 */
@Component
public class OrderFailureInspectListener extends MqListenerConvert {
    private static Log logger = Log.getInstance(OrderFailureInspectListener.class);

    @Autowired
    private FinanceBidService financeBidService;

    @Value("${borrow.pay.go.fuiou.html}")
    private String goToFuiouHtml;


    @Override
    protected void transferTo(String transferData) {
        logger.info("订单是否成功任务处理开始transferData={}", transferData);
//		FuiouH5PayService.Param param = JSONObject.parseObject(transferData, FuiouH5PayService.Param.class);
        if (transferData == null) {
            logger.warn("订单是否成功任务处理,解析参数为空={}", transferData);
            return;
        }
        String orderId = transferData.split("##")[0];
        String version = transferData.split("##")[1];

        try {
            //关闭订单
            financeBidService.closeOrder(orderId, goToFuiouHtml, version);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("订单{}失效处理。异常信息：{}", transferData, e);
        }

    }

}






















