package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品临时信息表,用来存放小程序商品草稿状态时临时信息
 * </p>
 *
 * @author Charlie
 * @since 2018-09-13
 */
@TableName("shop_product_temp_info")
public class ShopProductTempInfo extends Model<ShopProductTempInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 小程序商品id
     */
	@TableField("shop_product_id")
	private Long shopProductId;
    /**
     * sku json字符串
     */
	@TableField("sku_json")
	private String skuJson;
    /**
     * 状态 0删除,1正常
     */
	private Integer status;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopProductId() {
		return shopProductId;
	}

	public void setShopProductId(Long shopProductId) {
		this.shopProductId = shopProductId;
	}

	public String getSkuJson() {
		return skuJson;
	}

	public void setSkuJson(String skuJson) {
		this.skuJson = skuJson;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
