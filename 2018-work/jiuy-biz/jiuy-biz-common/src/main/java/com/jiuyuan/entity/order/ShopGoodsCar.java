package com.jiuyuan.entity.order;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 小程序商城购物车
 * </p>
 *
 * @author Aison
 * @since 2018-04-19
 */
@TableName("shop_goods_car")
public class ShopGoodsCar extends Model<ShopGoodsCar> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品ID
     */
	@TableField("product_sku_id")
	private Long productSkuId;
    /**
     * 会员id
     */
	@TableField("member_id")
	private Long memberId;
    /**
     * 商品数量
     */
	@TableField("sku_number")
	private Long skuNumber;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 购物车中商品状态  -1 删除，0禁用, 1正常，2失效
     */
	@TableField("car_suk_status")
	private Integer carSukStatus;
    /**
     * 最后修改时间
     */
	@TableField("last_update_time")
	private Long lastUpdateTime;

	/**
	 * 商品大类
	 */
	@TableField("product_id")
	private Long productId;

	/**
	 * 商家信息
	 */
	@TableField("store_id")
	private Long storeId;

	/**
	 * shop_product 表主键
	 */
	@TableField("shop_product_id")
	private Long shopProductId;

	/**
	 * selected 是否选中
	 */
	@TableField("selected")
	private Integer selected;

	/**
	 * 直播商品
	 */
	@TableField("live_product_id")
	private Long liveProductId;

	public Long getLiveProductId() {
		return liveProductId;
	}

	public void setLiveProductId(Long liveProductId) {
		this.liveProductId = liveProductId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductSkuId() {
		return productSkuId;
	}

	public void setProductSkuId(Long productSkuId) {
		this.productSkuId = productSkuId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getSkuNumber() {
		return skuNumber;
	}

	public void setSkuNumber(Long skuNumber) {
		this.skuNumber = skuNumber;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getCarSukStatus() {
		return carSukStatus;
	}

	public void setCarSukStatus(Integer carSukStatus) {
		this.carSukStatus = carSukStatus;
	}

	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Long getProductId() {
		return productId;
	}
	public Long getStoreId() {
		return storeId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getShopProductId() {
		return shopProductId;
	}

	public void setShopProductId(Long shopProductId) {
		this.shopProductId = shopProductId;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
