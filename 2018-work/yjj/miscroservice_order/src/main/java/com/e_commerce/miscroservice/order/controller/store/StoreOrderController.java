package com.e_commerce.miscroservice.order.controller.store;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqTemplate;
import com.e_commerce.miscroservice.commons.entity.service.TimerScheduler;
import com.e_commerce.miscroservice.commons.enums.colligate.MqChannelEnum;
import com.e_commerce.miscroservice.commons.enums.colligate.TimerSchedulerTypeEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.DebugUtils;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.order.service.yjj.StoreOrderService;
import com.e_commerce.miscroservice.order.vo.StoreOrderDTO;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/17 10:52
 */
@RestController
@RequestMapping( "order/storeOrder" )
public class StoreOrderController {

    private Log logger = Log.getInstance(StoreOrderController.class);
    private static final int DAY_OF15 = 15*24*60*60*1000;

    @Autowired
    private StoreOrderService storeOrderService;
    @Autowired
    @Lazy
    private MqTemplate mqTemplate;


    /**
     * 注册平台代发15天后的,待结算到已结算
     *
     * @param storeOrderNo storeOrderNo
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/2/19 11:41
     */
    @RequestMapping( "registerSendGoodAfter15DaysJob" )
    public Response registerSendGoodAfter15DaysJob(
            @RequestParam("storeOrderNo") Long storeOrderNo
    ) {
        long curr = System.currentTimeMillis();
        DebugUtils.todo("发货后15天");
        //15天
//        curr += DAY_OF15;
        curr += 1000*5*60;
        String cron = new SimpleDateFormat("ss mm HH dd MM ? yyyy").format(new Date(curr));
        logger.info("注册发货15天的定时任务 cron={},orderId={}", cron, storeOrderNo);
        return ResponseHelper.canShouldNotLogin()
                .invokeNoReturnVal(userId->{
                    TimerScheduler timerScheduler = new TimerScheduler();
                    timerScheduler.setType(TimerSchedulerTypeEnum.ORDER_FAILURE_INSPECT.toNum());
//                    timerScheduler.setType(TimerSchedulerTypeEnum.SEND_GOOD_AFTER_15DAYS.toNum());
                    timerScheduler.setName(TimerSchedulerTypeEnum.ORDER_FAILURE_INSPECT.toChar()+storeOrderNo);
//                    timerScheduler.setName(TimerSchedulerTypeEnum.SEND_GOOD_AFTER_15DAYS.toChar()+storeOrderId);
                    timerScheduler.setCron(cron);
                    JSONObject param = new JSONObject();
                    param.put("storeOrderNo", storeOrderNo);
                    timerScheduler.setParams(param.toJSONString());
                    //分销待入账到入账
                    mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
                })
                .returnResponse();
    }



}
