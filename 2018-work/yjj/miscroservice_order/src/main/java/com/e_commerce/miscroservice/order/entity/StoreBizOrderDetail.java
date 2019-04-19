//package com.e_commerce.miscroservice.order.entity;
//
//import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
//import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
//import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
//import lombok.Data;
//
///**
// * @author Charlie
// * @version V1.0
// * @date 2018/12/11 10:29
// * @Copyright 玖远网络
// */
//@Data
//@Table("store_biz_order_detail")
//public class StoreBizOrderDetail{
//
//    @Id
//    private Long id;
//
//    @Column(value = "order_no", commit = "订单编号", isNUll = false, length = 50)
//    private String orderNo;
//
//    @Column(value = "order_id", commit = "订单id", isNUll = false, length = 20)
//    private Long orderId;
//
//    @Column(value = "seller_id", commit = "卖家id(brandId)", length = 20)
//    private Long sellerId;
//
//    @Column( value = "product_id", commit = "商品id", length = 20, isNUll = false)
//    private Long productId;
//
//    @Column(value = "productName", commit = "商品名", length = 200)
//    private String productName;
//
//    @Column( value = "sku_id", commit = "sku", length = 20 )
//    private Long skuId;
//
//    @Column(value = "size", commit = "尺码", length = 20)
//    private String size;
//
//    @Column(value = "color", commit = "颜色", length = 20)
//    private String color;
//
//    @Column( value = "buy_count", commit = "购买数量", defaultVal = "1" )
//    private Integer buyCount;
//
//
//    //todo... 其他
//}
