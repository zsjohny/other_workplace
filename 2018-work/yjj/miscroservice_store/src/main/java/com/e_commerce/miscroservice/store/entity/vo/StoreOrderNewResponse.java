package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class StoreOrderNewResponse extends StoreOrderNew {
    private List<StoreOrderItemNew> list;
    private String img;//商品图片
    private String shopName;//商品名称
    private String size;
    private String color;

    private Double orderTotalPay;
    private Double orderExpress;
    private Double orderTotalMoney;


}
