package com.e_commerce.miscroservice.order.controller.yjj.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.order.YjjOrder;
import com.e_commerce.miscroservice.order.service.yjj.YjjOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/13 18:35
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping("/order/rpc/yjjOrder")
public class YjjOrderRpcController {

    @Autowired
    private YjjOrderService yjjOrderService;

    /**
     * 根据订单号查询订单
     *
     * @param orderNo orderNo
     * @return true 操作成功
     * @author Charlie
     * @date 2018/10/11 17:57
     */
    @RequestMapping( "findYjjOrderByOrderNo" )
    public YjjOrder findYjjOrderByOrderNo(@RequestBody String orderNo) {
        YjjOrder yjjOrder = yjjOrderService.findYjjOrderByOrderNo(orderNo);
        return yjjOrder;
    }
}
