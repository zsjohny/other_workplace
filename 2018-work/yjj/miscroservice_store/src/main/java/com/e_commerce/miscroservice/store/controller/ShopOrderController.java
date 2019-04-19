package com.e_commerce.miscroservice.store.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.entity.vo.ShopMemberOrderRequest;
import com.e_commerce.miscroservice.store.entity.vo.ShopOrderRequest;
import com.e_commerce.miscroservice.store.service.ShopOrderService;
import com.e_commerce.miscroservice.store.service.StoreRefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 个人中心-订单
 * @author hyf
 * @version V1.0
 * @date 2018/9/28 11:01
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/shop/order")
public class ShopOrderController {

    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private StoreRefundOrderService shopRefundOrderService;
    /**
     * 获取到订单列表
     *
     * @param type       1:进货订单列表；2：供应商订单列表
     * @return
     */

//    public Response findOrderList(@RequestParam( value = "status", required = false, defaultValue = "5" ) Integer status,
//                                 @RequestParam( value = "type", required = false, defaultValue = "1" ) Integer type,
//                                  @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
//                                  @RequestParam(value = "storeId",defaultValue = "100")Long storeId,
//                                  @RequestParam(value = "supplierId",defaultValue = "100")Long supplierId) {
//        System.out.println("status="+status+",type="+type+",pageNum="+pageNum);
//        return shopOrderService.findOrderList(status,type,storeId,supplierId,pageNum);
//    }
    /**
     * hk
     * 获取订单列表
     */
    @Consume(ShopOrderRequest.class)
    @RequestMapping("/find/list")
    public Response findList(
            @RequestParam(value = "status",required = false)Integer status,//订单状态
            @RequestParam("storeId")Long storeId,//用户id
            @RequestParam(value = "supplierId",required = false) Long supplierId,//供应商id
            Integer pageSize,
            Integer pageNumber
    ){
        return shopRefundOrderService.selectOrderList((ShopOrderRequest)ConsumeHelper.getObj());
    }
    /**
     * 获取到订单详情
     *
     * @param orderNo
     * @return
     */
    /**
     * 查看订单详情
     */

    @RequestMapping("/getOrderDetail")
    public Response getOrderInfo(@RequestParam( "orderNo" ) Long orderNo,
                                 @RequestParam("storeId") Long storeId ){
        try {
            Map<String, Object> result = shopRefundOrderService.getOrderInfoMation(storeId, orderNo);
            return Response.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.errorMsg(e.getMessage());
        }
    }

}
