package com.e_commerce.miscroservice.supplier.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrderRequest;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.supplier.entity.request.RefundGoodsMoneyRequest;
import com.e_commerce.miscroservice.supplier.service.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/supplier/refundOrder")
public class RefundOrderController {

    private Log logger = Log.getInstance(AllOrderController.class);
    @Autowired
    private RefundOrderService refundOrderService;
    /**
     * 获取所有售后工单
     * @param refundOrderNo 售后单号
     * @param orderNo  订单号
     * @param storePhone 订单号码
     * @param storeName  订单人姓名
     * @param shopName  商品名称
     * @param refundStatus   售后状态
     * @param refundType    售后类型
     * @param refundReason  退单理由
     * @param refundCostMin 申请退款费用
     * @param refundCostMax  申请退款费用
     * @param refundMoneyMin 实际退款金额
     * @param refundMoneyMax  实际退款金额
     * @param returnCountMin 申请退款数量
     * @param returnCountMax  申请退款数量
     * @param applyTimeMin  申请时间
     * @param applyTimeMax  申请时间
     * @param storeDealRefundTimeMin 受理时间
     * @param storeDealRefundTimeMax 受理时间
     * @return
     */
    @Consume(RefundOrderRequest.class)
    @RequestMapping("/findAllRefundOrder")
    public Response findAllOrder(@RequestParam(value="refundOrderNo", required=false) Long refundOrderNo,
                                 @RequestParam(value="orderNo", required=false) Long orderNo,
                                 @RequestParam(value="storePhone", required=false) String storePhone,
                                 @RequestParam(value="storeName", required=false) String storeName,
                                 @RequestParam(value="shopName", required=false) String shopName,
                                 @RequestParam(value="refundStatus", required=false) Integer refundStatus,
                                 @RequestParam(value="refundType", required=false) Integer refundType,
                                 @RequestParam(value="refundReason", required=false) String refundReason,
                                 @RequestParam(value="refundCostMin", required=false) Double refundCostMin,
                                 @RequestParam(value="refundCostMax", required=false) Double refundCostMax,
                                 @RequestParam(value="refundMoneyMin", required=false) Double refundMoneyMin,
                                 @RequestParam(value="refundMoneyMax", required=false) Double refundMoneyMax,
                                 @RequestParam(value="returnCountMin", required=false) Integer returnCountMin,
                                 @RequestParam(value="returnCountMax", required=false) Integer returnCountMax,
                                 @RequestParam(value="applyTimeMin", required=false) String applyTimeMin,
                                 @RequestParam(value="applyTimeMax", required=false) String applyTimeMax,
                                 @RequestParam(value="storeDealRefundTimeMin", required=false) String storeDealRefundTimeMin,
                                 @RequestParam(value="storeDealRefundTimeMax", required=false) String storeDealRefundTimeMax,
                                 @RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(value="pageNumber", defaultValue="1") Integer pageNumber,
                                 @RequestParam(value="supplierName",required = false,defaultValue = "9000063")String supplierName) {
//        Integer id = IdUtil.getId();
//        if (supplierId-id!=0){
//            return Response.errorMsg("供应商不匹配请重新登录");
//        }
        return refundOrderService.findAllRefundOrder((RefundOrderRequest) ConsumeHelper.getObj());
    }

    /**
     *  获取售后订单信息
     * productId 商品ID
     * @return
     */
    @RequestMapping(value = "/getRefundOrderDetial")
    public Response getRefundOrderInfo(@RequestParam(value = "refundOrderId",defaultValue = "20180403172514364") long refundOrderId// 售后订单ID
    ) {
        try {
            Map<String,Object> data = refundOrderService.getRefundOrderInfo(refundOrderId);
            return Response.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return Response.errorMsg(e.getMessage());
        }
    }

    /**
     * 校验退款金额
     */
    @RequestMapping(value = "/equalsMoney")
    public Response equalsMoney(@RequestParam(value="orderNo", required=false)long orderNo,// 售后订单ID
                                @RequestParam(value="skuId", required=false) Long skuId,
                                @RequestParam(value="money", required=false) Double money
    ) {
        try {
            Integer integer = refundOrderService.equalsMoney(orderNo, skuId, money);
            return Response.success(integer);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return Response.errorMsg(e.getMessage());
        }
    }


    /**
     * 售后处理 退货退款
     * @param id
     * @param status 0 同意 1 拒绝
     * @param money 退款金额
     * @param msg 理由
     * @return
     */
    @RequestMapping("/refund/goods/money")
    @Consume(RefundGoodsMoneyRequest.class)
    public Response refundGoodsMoney ( Long id, Integer status, Double money, String msg,Integer type,String receiverName,String receiverPhone,String receiverAddress ){
        return refundOrderService.refundGoodsMoney((RefundGoodsMoneyRequest)ConsumeHelper.getObj());
    }

    /**
     * 售后处理 退款
     * @param id
     * @param status 0 同意 1 拒绝
     * @param money 退款金额
     * @param msg 理由
     * @return
     */
    @RequestMapping("/refund/money")
    @Consume(RefundGoodsMoneyRequest.class)
    public Response refundMoney ( Long id, Integer status, Double money, String msg,Integer type){
        return refundOrderService.refundMoney((RefundGoodsMoneyRequest)ConsumeHelper.getObj());
    }

    /**
     * 确认收货
     * @param id
     * @return
     */
    @RequestMapping("/confirm/tack/goods")
    public Response confirmTackGoods(Long id){
        return refundOrderService.confirmTackGoods(id);
    }
}
