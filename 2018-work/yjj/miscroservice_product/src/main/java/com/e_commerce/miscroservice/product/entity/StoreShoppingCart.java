package com.e_commerce.miscroservice.product.entity;

import lombok.Data;


/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-10
 */
@Data
public class StoreShoppingCart {

    /**
     * id
     */
	private Long id;
    /**
     * 门店id
     */
	private Long storeId;
    /**
     * 商品id
     */
	private Long productId;
    /**
     * 对应ProductSKU的id
     */
	private Long skuId;
    /**
     * 购买数量
     */
	private Integer buyCount;
    /**
     * 在购物车中是否被选中:0未选中,1选中
     */
	private Integer isSelected;
    /**
     * 状态:-1删除，0正常
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
     * 统计识别码
     */
	private Long statisticsId;



}
