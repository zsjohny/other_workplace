package com.e_commerce.miscroservice.product.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;


/**
 * <p>
 * 商品临时信息表,用来存放小程序商品草稿状态时临时信息
 * </p>
 *
 * @author Charlie
 * @since 2018-09-13
 */
@Table("shop_product_temp_info")
@Data
public class ShopProductTempInfo {


    /**
     * 主键
     */
    @Id
	private Long id;
    /**
     * 小程序商品id
     */
	@Column(value = "shop_product_id", commit = "小程序商品id", length = 20)
	private Long shopProductId;
    /**
     * sku json字符串(text)
     */
	private String skuJson;
    /**
     * 状态 0删除,1正常
     */
	@Column(value = "status", commit = "状态 0删除,1正常")
	private Integer status;


}
