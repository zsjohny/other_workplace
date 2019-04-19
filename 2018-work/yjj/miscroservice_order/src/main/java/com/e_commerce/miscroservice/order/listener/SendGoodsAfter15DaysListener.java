package com.e_commerce.miscroservice.order.listener;//package com.e_commerce.miscroservice.user.listener;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.order.service.yjj.StoreOrderService;
import com.e_commerce.miscroservice.order.vo.StoreOrderDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 分销管理奖待入账到已入账
 *
 * @author charlie
 * @version V1.0
 * @date 2018/9/13 21:05
 * @Copyright 玖远网络
 */
//@Component
public class SendGoodsAfter15DaysListener extends MqListenerConvert {
    private static Log logger = Log.getInstance(SendGoodsAfter15DaysListener.class);


    @Autowired
    private StoreOrderService storeOrderService;

    @Override
    protected void transferTo(String transferData) {
        logger.info ("发货后15天监听 监听 transferData={}", transferData);
        if (StringUtils.isBlank(transferData)) {
            logger.warn("监听发货后15天,没有订单信息");
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(transferData);
        StoreOrderDTO dto = new StoreOrderDTO();
        dto.setOrderNo(jsonObject.getLong("storeOrderNo"));
        storeOrderService.platformInsteadOfSendGoodsAfter15Days(dto);

    }


}
