package com.e_commerce.miscroservice.store.entity;

import com.e_commerce.miscroservice.store.entity.vo.StoreRefundOrder;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/30 16:40
 * @Copyright 玖远网络
 */
public class ExchangeType {


    //售后订单供应商自动确认收货时间15天
    public static final long refundOrderSupplierAutoTakeTime = 15 * 24 * 60 * 60 * 1000;
    //售后订单买家发货限制时间
    public static final long refundOrderRestrictiveDeliverTime = 3 * 24 * 60 * 60 * 1000;
    //售后订单卖家限制确认时间
    public static final long refundOrderRestrictiveConfirmTime = 3 * 24 * 60 * 60 * 1000;

    /**
     * 根据列表售后订单状态返回显示名称
     *
     * @param refundStatus
     * @return
     */
    public static String buildInfoRefundStatusName(int refundStatus) {
        //  售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
        String refundStatusName = "";
        if (refundStatus == 1) {// 1(待卖家确认、默认)、
            refundStatusName = "待卖家确认";
        } else if (refundStatus == 2) {// 2（待买家退货）
            refundStatusName = "卖家已同意，待买家退货";
        } else if (refundStatus == 3) {// 、3（待卖家确认收货）、
            refundStatusName = "买家已发货，待卖家退款";
        } else if (refundStatus == 4) {// 4(退款成功)、
            refundStatusName = "退款成功";
        } else if (refundStatus == 5) {// 5(卖家拒绝售后关闭)、
            refundStatusName = "卖家拒绝";
        } else if (refundStatus == 6) {// 6（买家超时未发货自动关闭）、
            refundStatusName = "已失效";
        } else if (refundStatus == 7) {// 7(卖家同意前买家主动关闭)、
            refundStatusName = "已撤销";
        } else if (refundStatus == 8) {// 8（平台客服主动关闭）
            refundStatusName = "平台关闭";
        } else if (refundStatus == 9) {// 9（卖家同意后买家主动关闭）
            refundStatusName = "已撤销";
        } else {
//            logger.info("未知售后订单状态,请尽快处理");
//            throw new RuntimeException("未知售后订单状态");
            return null;
        }
        return refundStatusName;
    }

    /**
     * 货物状态
     */
    public static String buildTakeProductStateName(StoreRefundOrder refundOrder) {
        String takeProductState = "";
        int refundType = refundOrder.getRefundType();
        if (refundType == StaticVariableEntity.EFUND_TYPE_REFUND_AND_PRODUCT) {// 2.退货退款
            takeProductState = "已收到货";
        }
        return takeProductState;
    }
    /**
     * 剩余卖家确认时间毫秒数
     *
     * @param refundOrder
     * @return
     */
    public static long buildSurplusAffirmTime(StoreRefundOrder refundOrder) {

        long applyTime = refundOrder.getApplyTime();//申请售后时间
        if(applyTime == 0 ){
            return 0;
        }
        long endAffirmTime = applyTime + refundOrderRestrictiveConfirmTime;//结束确认时间
        long time = System.currentTimeMillis();//当前时间
        long surplusAffirmTime = 0;//剩余卖家确认时间
        surplusAffirmTime = endAffirmTime - time;
        if(surplusAffirmTime < 0){
            surplusAffirmTime = 0;
        }
        return surplusAffirmTime;
    }
    /**
     * 剩余买家发货时间毫秒数
     *
     * @param refundOrder
     * @return
     */
    public static long buildSurplusDeliverTime(StoreRefundOrder refundOrder) {

        long storeAllowRefundTime = refundOrder.getStoreAllowRefundTime();//卖家同意时间
        if(storeAllowRefundTime == 0 ){
            return 0;
        }
        long deliverTime = storeAllowRefundTime + refundOrderRestrictiveDeliverTime;//结束发货时间
        long time = System.currentTimeMillis();//当前时间
        long surplusDeliverTime = 0;//剩余卖家确认时间
        surplusDeliverTime = deliverTime - time;
        if(surplusDeliverTime < 0){
            surplusDeliverTime = 0;
        }
        return surplusDeliverTime;
    }

    /**
     * 剩余卖家自动确认收货时间毫秒数
     * @param refundOrder
     * @return
     */
    public static long buildSurplusSupplierAutoTakeTime(StoreRefundOrder refundOrder) {

        long customerReturnTime = refundOrder.getCustomerReturnTime();//买家发货时间
        if(customerReturnTime == 0 ){//未发货则返回0
            return 0;
        }

        long supplierAutoTakeDeliveryPauseTime = refundOrder.getSupplierAutoTakeDeliveryPauseTime();//卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
        long supplierAutoTakeDeliveryPauseTimeLength = refundOrder.getSupplierAutoTakeDeliveryPauseTimeLength();//卖家自动确认收货总暂停时长（毫秒）
        long time = 0;//
        if(supplierAutoTakeDeliveryPauseTime == 0){//未暂停
            time = System.currentTimeMillis();//当前时间
        }else{//卖家申请平台介入售后订单暂停
            time = supplierAutoTakeDeliveryPauseTime;//暂停时间
        }

        long supplierAutoTakeTime = customerReturnTime + refundOrderSupplierAutoTakeTime + supplierAutoTakeDeliveryPauseTimeLength;//自动确认收货时间节点
        long surplusSupplierAutoTakeTime = 0;//剩余卖家确认时间
        surplusSupplierAutoTakeTime = supplierAutoTakeTime - time; //剩余自动确认收货时间  =  自动确认收货时间节点  - 当前时间或暂停时间
        if(surplusSupplierAutoTakeTime < 0){
            surplusSupplierAutoTakeTime = 0;
        }
        return surplusSupplierAutoTakeTime;

    }

}
