package com.e_commerce.miscroservice.operate.controller.order;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.RefundOrderFindReqeust;
import com.e_commerce.miscroservice.operate.service.order.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/30 16:27
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/refund")
public class RefundOrderController {

    @Autowired
    private RefundOrderService refundOrderService;

    /**
     * 售后订单管理
     * @param refundOrderNo
     * @param countStr
     * @param countEnd
     * @param brandName
     * @param name
     * @param applyTimeStr
     * @param applyTimeEnd
     * @param shopName
     * @param orderNo
     * @param refundMoneyStr
     * @param refundMoneyEnd
     * @return
     */
    @RequestMapping("/order/all")
    @Consume(RefundOrderFindReqeust.class)
    public Response findAllRefundOrder(String refundOrderNo,Integer countStr,Integer countEnd,String brandName,String name,String applyTimeStr,String applyTimeEnd,String shopName,String orderNo,Double refundMoneyStr,Double refundMoneyEnd){

        return refundOrderService.findAllRefundOrder((RefundOrderFindReqeust)ConsumeHelper.getObj());
    }

    /**
     * 结束工单
     * @param id
     * @param money
     * @param msg
     * @return
     */
    @RequestMapping("finish/service")
    public Response finishService(Long id,Double money,String msg){
        return refundOrderService.finishService(id,money,msg);

    }
}
