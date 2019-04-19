package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopMemberOrderResponse extends ShopMemberOrder {
    //商品颜色
    //private String color;
    //商品的尺码
    //private String size;
    //商品的数量
    //private Integer count;
    //商品的数量
    //private String name;
    //商品的价格
    //private BigDecimal price;
    //商品的skuId
    //private Long skuId;
    //商品的主图
    //private String summaryImages;

    //订单详情列表
    private List<ShopMemberOrderItemResponse> list;

    //团购
    private Integer buttonStatus;
    private Long productId;
    private String shopProductName;

    //支付时间
    private String pendingPaymentTime;
    //订单时候在售后中
    private String isRefund;
}
