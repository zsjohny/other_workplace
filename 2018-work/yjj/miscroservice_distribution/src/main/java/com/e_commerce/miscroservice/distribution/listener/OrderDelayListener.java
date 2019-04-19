package com.e_commerce.miscroservice.distribution.listener;//package com.e_commerce.miscroservice.user.listener;

import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.distribution.service.ShopMemberAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 分销管理奖待入账到已入账
 *
 * @author charlie
 * @version V1.0
 * @date 2018/9/13 21:05
 * @Copyright 玖远网络
 */
@Component
public class OrderDelayListener extends MqListenerConvert {
    private static Log logger = Log.getInstance(OrderDelayListener.class);

    @Value ("${scheduler.job.uuid.managerEarningsIn}")
    private String managerEarningsIn;


    @Autowired
    private ShopMemberAccountService shopMemberAccountService;

    @Override
    protected void transferTo(String transferData) {
        logger.info ("OrderDelayListener 监听 transferData={}", transferData);
        if (transferData == null) {
            // ignore
        }
        else if (transferData.equals (managerEarningsIn)){
            try {
                shopMemberAccountService.teamInWait2Alive ();
            } catch (Exception e) {
                e.printStackTrace ();
                logger.error ("分销管理金待入账入账  失败!!!");
            }
        }
        else {
            // do someThing
        }
    }


}
