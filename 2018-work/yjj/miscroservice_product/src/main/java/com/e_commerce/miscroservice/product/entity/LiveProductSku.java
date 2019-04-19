//package com.e_commerce.miscroservice.product.entity;
//
//import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
//import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
//import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
//import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
//import lombok.Data;
//
//import java.sql.Timestamp;
//
///**
// * @author Charlie
// * @version V1.0
// * @date 2019/1/11 9:56
// * @Copyright 玖远网络
// */
//@Data
//@Table("yjj_live_product_sku")
//public class LiveProductSku {
//

// 暂时不需要
//    @Id
//    private Long id;
//
//    /**
//     * 直播间商品id
//     */
//    @Column(value = "live_product_id", commit = "直播间商品id", length = 20, isNUll = false)
//    private Long liveProductId;
//
//    /**
//     * 小程序商品sku
//     */
//    @Column(value = "sku_id", commit = "skuId",  length = 20, defaultVal = "0")
//    private Long skuId;
//
//    /**
//     * 颜色id
//     */
//    @Column(value = "color_id", commit = "颜色id", length = 20, isNUll = false)
//    private Long colorId;
//
//    /**
//     * 颜色
//     */
//    @Column(value = "color", commit = "颜色", length = 30, isNUll = false)
//    private String color;
//
//    /**
//     * 尺码id
//     */
//    @Column( value = "size_id", commit = "尺码id", length = 20, isNUll = false )
//    private Long sizeId;
//
//    /**
//     * 尺码
//     */
//    @Column(value = "size", commit = "尺码", length = 30, isNUll = false)
//    private String size;
//
//    /**
//     * 0可用,1删除,2失效
//     */
//    @Column(value = "del_status", length = 4, defaultVal = "0")
//    private Integer delStatus;
//
//    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
//    private Timestamp createTime;
//
//    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
//    private Timestamp updateTime;
//
//
//
//}
