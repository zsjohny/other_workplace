package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

@Data
public class RefundRquest extends BaseEntity {
    private Long storeId;
    private Long userId;
    //售后订单状态
    private Integer status;
    //退款类型
    private Integer type;
    private Long orderId;
}
