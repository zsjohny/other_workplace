package com.e_commerce.miscroservice.product.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 17:13
 * @Copyright 玖远网络
 */
@Data
@Table("shop_goods_car")
public class ShopGoodsCar{


    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 商品ID
     */
    @Column(value = "product_sku_id", commit = "商品ID", length = 20, isNUll = false)
    private Long productSkuId;
    /**
     * 会员id
     */
    @Column(value = "member_id", commit = "会员id", length = 20, isNUll = false)
    private Long memberId;
    /**
     * 商品数量
     */
    @Column(value = "sku_number", commit = "商品数量", length = 11, defaultVal = "1")
    private Long skuNumber;
    /**
     * 创建时间
     */
    @Column(value = "create_time", length = 20)
    private Long createTime;
    /**
     * 购物车中商品状态  -1 删除，0禁用, 1正常，2失效
     */
    @Column(value = "car_suk_status", length = 4, commit = "购物车中商品状态  -1 删除，0禁用, 1正常，2失效", defaultVal = "1")
    private Integer carSukStatus;
    /**
     * 最后修改时间
     */
    @Column(value = "last_update_time", length = 20, commit = "最后修改时间")
    private Long lastUpdateTime;

    /**
     * 商品大类
     */
    @Column(value = "product_id", commit = "商品id", length = 20)
    private Long productId;

    /**
     * 商家信息
     */
    @Column(value = "store_id", commit = "门店id" , length = 20)
    private Long storeId;

    /**
     * shop_product 表主键
     */
    @Column(value = "shop_product_id", commit = "shop_product 表主键", length = 20)
    private Long shopProductId;

    /**
     * 直播商品主键
     */
    @Column(value = "live_product_id", commit =  "直播商品主键", length = 20, defaultVal = "0")
    private Long liveProductId;

    /**
     * selected 是否选中
     */
    @Column(value = "selected", length = 4, commit = "是否选择", defaultVal = "0")
    private Integer selected;
}
