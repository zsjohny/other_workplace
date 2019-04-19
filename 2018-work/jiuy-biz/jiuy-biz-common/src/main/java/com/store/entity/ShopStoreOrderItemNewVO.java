package com.store.entity;

import lombok.Data;

@Data
public class ShopStoreOrderItemNewVO  {
//	private long orderId;
    private long productId;
    private double money;
    private int buyCount;
    private String colorStr;
    private String sizeStr;
    private String skuSnapshot;
	private String clothesNumber;//商品款号
	private String productName;//	商品名称
	private String productMainImg;//	商品主图
	private Integer refundStatus;//售后商品状态
	 private long brandId;
	private String brandName;//	品牌名称
    private String platformProductState; //平台商品状态:0已上架、1已下架、2已删除
}