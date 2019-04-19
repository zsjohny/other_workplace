package com.e_commerce.miscroservice.store.entity.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import java.math.BigDecimal;
import lombok.Data;
 @Data
 @Table("store_refund_order")
 public class StoreRefundOrder {
@Id
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
private Integer returnCount;
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
private Long storeRefuseRefundTime;
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
private String customerExpressCompanyName;
private String expressInfo;
private Long skuId;
private Double storeBackMoney;
private Double realBackMoney;
}