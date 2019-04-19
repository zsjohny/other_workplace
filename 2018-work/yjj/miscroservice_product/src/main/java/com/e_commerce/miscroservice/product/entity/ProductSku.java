package com.e_commerce.miscroservice.product.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 20:13
 * @Copyright 玖远网络
 */
@Data
//yjj_productsku
public class ProductSku{

    /**
     * id
     */
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 小程序id
     */
    private Long wxaProductId;

    /**
     * 商品SKU属性值聚合，PropertyNameId:PropertyValueId形式，多个以英文,隔开
     */
    private String propertyIds;

    /**
     * 价格，人民币以分为单位，玖币以1为单位
     */
    private Integer price;

    /**
     * 库存
     */
    private Integer remainCount;

    /**
     * 对应SKU的图片信息
     */
    private String specificImage;

    /**
     * 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * sku编码
     */
    private Long skuNo;

    private BigDecimal cash;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     *

     货品名称

     */
    private String name;

    /**
     * 市场价（吊牌价）
     */
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 'sku款号'
     */
    private String clothesNumber;

    /**
     * 主仓库
     */
    private Long lOWarehouseId;

    /**
     * '库存保留时间' 天
     */
    private Integer remainKeepTime;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 上架时间
     */
    private Long saleStartTime;

    /**
     * 下架时间
     */
    private Long saleEndTime;

    private Integer sort;

    /**
     * 库存锁定量
     */
    private Integer remainCountLock;

    /**
     * 库存锁定开始时间
     */
    private Long remainCountStartTime;

    /**
     * 库存锁定结束时间
     */
    private Long remainCountEndTime;

    /**
     * 是否锁库存
     */
    private Integer isRemainCountLock;

    /**
     * 推送erp时间
     */
    private Long pushTime;

    /**
     * 推广销量

     */
    private Integer promotionSaleCount;

    /**
     * 推广访问量

     */
    private Integer promotionVisitCount;

    /**
     * 副仓库库存
     */
    private Integer remainCount2;

    /**
     * 副仓库
     */
    private Long lOWarehouseId2;

    private Integer setLOWarehouseId2;

    /**
     * 货架位置格式  1--2（表示1号2排）

     */
    private String position;

    /**
     * 颜色ID
     */
    private Long colorId;

    /**
     * 颜色名称
     */
    private String colorName;

    /**
     * 尺码ID
     */
    private Long sizeId;

    /**
     * 尺码名称
     */
    private String sizeName;

    /**
     * 定时更新库存时间, 时间为上架后N天 或者 指定日期时间戳(根据timing_set_type判断)
     */
    private Long timingSetRemainCountTime;

    /**
     * 定时更新库存类型, 0关闭(不更新), 1指定日期更新 ,2上架后N天更新
     */
    private Integer timingSetType;

    /**
     * 定时更新库存数
     */
    private Integer timingSetCount;

    /**
     * sku类别 1:品牌商sku,2:门店sku
     */
    private Integer ownType;


}
