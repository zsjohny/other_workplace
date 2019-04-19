package com.e_commerce.miscroservice.supplier.entity.request;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

@Data
@Table("store_refund_order_action_log")
public class StoreRefundOrderActionLog {
@Id
private Long id;
private Long refundOrderId;
private Long actionTime;
private String actionName;
}