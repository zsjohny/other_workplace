package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

@Data
public class RefundResponse extends ShopOrderAfterSale {
    private Integer refundCount;
    private String refundName;
    private String refundSummaryImages;
    private String refundSize;
    private String color;
    private String isRefund;
    private String orderNumber;
}
