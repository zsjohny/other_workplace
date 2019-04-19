package com.e_commerce.miscroservice.order.service.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.BaseService;

/**
 * 描述 代理返现
 * @date 2018/9/26 11:23
 * @return
 */
public interface ProxyRewardService extends BaseService<ProxyReward> {

    /**
     * 描述 生成订单收益
     * @param proxyOrder
     * @author hyq
     * @date 2018/10/11 12:03
     * @return void
     */
    void getOrderReward(ProxyOrder proxyOrder);

    /**
     * 描述 根据用户id 订单号 获取本笔订单的收益
     * @param userId

    * @param orderNo
     * @author hyq
     * @date 2018/10/11 12:02
     * @return com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward
     */
    ProxyReward getRewardMoney(long userId,String orderNo);

}
