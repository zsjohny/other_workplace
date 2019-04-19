package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

@Data
public class ShopMemberOrderItemResponse extends ShopMemberOrderItem {
    private Integer refundStatus;//订单售后状态
    private Integer buyCount;//商品购买数量
    private String afterSaleId;//售后订单号
}
