package com.e_commerce.miscroservice.order.entity;

import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/22 14:21
 */
@Data
public class ShopOrderAfterSale {

    private Long id;

    private String afterSaleId;
    private Long storeId;
    private Long memberId;
    private String name;
    private Integer refundCount;
    private String refundName;
    private String phone;
    private String orderId;

    private Long applyTime;

    private Long operateTime;

    private Integer type;

    private Integer status;
    private Double applyBackMoney;

    private Double backMoney;

    private Long skuId;


}
