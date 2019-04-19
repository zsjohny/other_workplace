package com.e_commerce.miscroservice.product.vo;

import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/15 13:29
 */
@Data
public class SupplierProductDTO {
    /*
     * 这里直接查询供应商用户关于商品信息的配置
     * 商品信息的配置不应该放在用户表里的, 建议以后拆到这个模块
     */
    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 批发起定量
     */
    private Integer wholesaleCount;

    /**
     * 批发起定额
     */
    private Double wholesaleCost;

}
