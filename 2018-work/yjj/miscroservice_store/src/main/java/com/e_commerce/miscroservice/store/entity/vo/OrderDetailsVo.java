package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单列表详情
 * @author hyf
 * @version V1.0
 * @date 2018/9/29 10:27
 * @Copyright 玖远网络
 */
@Data
public class OrderDetailsVo implements Serializable {
    /**
     * 明细id
     */
    private Long itemId;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * skuid
     */
    private Long skuId;

    /**
     * 总金额
     */
    private Double totalMoney;

    /**
     * 金额
     */
    private Double money;

    /**
     * 总邮费
     */
    private Double totalExpressMoney;

    /**
     * 数量
     */
    private Integer buyCount;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 颜色
     */
    private String colorName;

    /**
     * 尺码
     */
    private String sizeName;

    /**
     * 主图
     */
    private String mainImg;

    /**
     * 品牌
     */
    private String brandName;


}
