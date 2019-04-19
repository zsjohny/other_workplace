package com.jiuyuan.entity.newentity;

import lombok.Data;

@Data
//@Table("store_refund_order_action_log")
public class StoreRefundOrderActionLog {
//@Id
private Long id;
private Long refundOrderId;
private Long actionTime;
private String actionName;
}