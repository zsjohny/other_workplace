package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundOrderNewL {
    private Long id;
    private Long refundOrderNo;
    private Long storeId;
    private String storePhone;
    private String storeName;
    private Long supplierId;
    private Long orderNo;
    private Long brandId;
    private String brandName;
    private Integer refundType;
    private Integer refundStatus;
    private BigDecimal refundCost;
    private Integer refundCount;
    private Long refundReasonId;
    private String refundReason;
    private String refundRemark;
    private String refundProofImages;
    private Integer refundWay;
    private Long refundTime;
    private Long applyTime;
    private Long customerReturnTime;
    private Long confirmTime;
    private Long platformInterveneTime;
    private Long platformInterveneCloseTime;
    private String customerExpressNo;
    private String customerExpressCompany;
    private Long storeAllowRefundTime;
    private Long store_refuseRefundTime;
    private String storeRefuseReason;
    private String handlingSuggestion;
    private String storeAgreeRemark;
    private String platformCloseReason;
    private Integer platformInterveneState;
    private String receiver;
    private String supplierReceiveAddress;
    private String receiverPhone;
    private Long supplierAutoTakeDeliveryPauseTime;
    private Long supplierAutoTakeDeliveryPauseTimeLength;
    private Long customerCancelTime;
    private Long customerOvertimeTimeNoDelivery;
    private String expressInfo;
    private String customerExpressCompanyName;
    private Long skuId;
    private Double storeBackMoney;
    private Double realBackMoney;
}
