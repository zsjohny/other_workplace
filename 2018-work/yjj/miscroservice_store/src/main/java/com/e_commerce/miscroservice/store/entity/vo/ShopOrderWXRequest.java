package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

@Data
public class ShopOrderWXRequest extends BaseEntity{
    private Integer type;
    private Integer status;
    private Long storeId;
    private Long refundOrderNo;
    private String msg;
    private Double realMoney;
    private Integer style;//店中店标识  0默认  1 店中店用户
}
