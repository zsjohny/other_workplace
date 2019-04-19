package com.e_commerce.miscroservice.store.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.service.FictitiousService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fictitious/order")
public class FictitiousController {

    private Log logger=Log.getInstance(FictitiousController.class);
    @Autowired
    private FictitiousService fictitiousService;


    /**
     * 选择app支付方式
     */
    @RequestMapping("/payChoose/method")
    public Response chososePay(@RequestParam("storeId")Long storeId,@RequestParam(value = "orderNo",required = false) Long orderNo,
                               @RequestParam(value = "blend",required = false,defaultValue = "0") Long blend){
        /**
         * blend来判断商家在购买商品的时候时候是混批 如果是混批就不能使用待结算支付(废弃)
         * 0:混批   1:只有一种sku商品
         */
        logger.info("storeId="+storeId+";orderNo="+orderNo);
        return fictitiousService.selectPay(storeId,orderNo,blend);
    }
    /**
     * 查询账户待结算余额(已经合并到选择支付里面)
     */
    @RequestMapping("/selectMoney")
    public Response selectMoney(@RequestParam("storeId")Long storeId){
        logger.info("storeId="+storeId);
        return fictitiousService.selectMoney(storeId);
    }

    /**
     * 使用待结算资金付款(废弃)
     */
    @RequestMapping("/paymentWaitMoney")
    public Response paymentAndUpdete(@RequestParam("orderNo") Long orderNo,@RequestParam("storeId") Long storeId,@RequestParam("payMoney")Double payMoney){
        logger.info("storeId="+storeId+";"+"orderNo="+orderNo);
        return fictitiousService.selectMoney(orderNo, storeId,payMoney);
    }

    /**
     * 使用已结算资金进行付款
     */
    @RequestMapping("/paymentAndUpdate")
    public Response paymentAndUpdateT(@RequestParam("orderNo") Long orderNo,@RequestParam("storeId") Long storeId){
        logger.info("storeId="+storeId+";"+"orderNo="+orderNo);
        return fictitiousService.selectRealMoney(orderNo, storeId);
    }
}
