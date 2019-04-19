package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-10
 */
@TableName("shop_member_reservations_order")
public class ShopMemberReservationsOrder extends Model<ShopMemberReservationsOrder> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 会员ID
     */
	@TableField("shop_member_id")
	private Long shopMemberId;
    /**
     * 小程序商品ID
     */
	@TableField("shop_product_id")
	private Long shopProductId;
	/**
     * 小程序平台商品SKUID
     */
	@TableField("platform_product_sku_id")
	private Long platformProductSkuId;
    /**
     * 商品名称
     */
	@TableField("shop_product_name")
	private String shopProductName;
    /**
     * 尺码名称
     */
	@TableField("shop_product_size_name")
	private String shopProductSizeName;
    /**
     * 颜色名称
     */
	@TableField("shop_product_color_name")
	private String shopProductColorName;
    /**
     * 收件人姓名
     */
	@TableField("shop_member_name")
	private String shopMemberName;
    /**
     * 收件人手机号
     */
	@TableField("shop_member_phone")
	private String shopMemberPhone;
	/**
     * 商品款号
     */
	@TableField("clothes_number")
	private String clothesNumber;
	/**
     * 门店价,就是小程序商品的零售价
     */
	@TableField("price")
	private Double price;
	/**
     * 是否是自有商品：1是自有商品，0平台商品
     */
	@TableField("own")
	private Integer own;
	/**
     * 商品主图
     */
	@TableField("summary_image")
	private String summaryImage;
	/**
     * 预约时间
     */
	@TableField("appointment_time")
	private Long appointmentTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getShopMemberId() {
		return shopMemberId;
	}

	public void setShopMemberId(Long shopMemberId) {
		this.shopMemberId = shopMemberId;
	}

	public Long getShopProductId() {
		return shopProductId;
	}

	public void setShopProductId(Long shopProductId) {
		this.shopProductId = shopProductId;
	}

	public Long getPlatformProductSkuId() {
		return platformProductSkuId;
	}

	public void setPlatformProductSkuId(Long platformProductSkuId) {
		this.platformProductSkuId = platformProductSkuId;
	}

	public String getShopProductName() {
		return shopProductName;
	}

	public void setShopProductName(String shopProductName) {
		this.shopProductName = shopProductName;
	}

	public String getShopProductSizeName() {
		return shopProductSizeName;
	}

	public void setShopProductSizeName(String shopProductSizeName) {
		this.shopProductSizeName = shopProductSizeName;
	}

	public String getShopProductColorName() {
		return shopProductColorName;
	}

	public void setShopProductColorName(String shopProductColorName) {
		this.shopProductColorName = shopProductColorName;
	}

	public String getShopMemberName() {
		return shopMemberName;
	}

	public void setShopMemberName(String shopMemberName) {
		this.shopMemberName = shopMemberName;
	}

	public String getShopMemberPhone() {
		return shopMemberPhone;
	}

	public void setShopMemberPhone(String shopMemberPhone) {
		this.shopMemberPhone = shopMemberPhone;
	}

	public String getClothesNumber() {
		return clothesNumber;
	}

	public void setClothesNumber(String clothesNumber) {
		this.clothesNumber = clothesNumber;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getOwn() {
		return own;
	}

	public void setOwn(Integer own) {
		this.own = own;
	}
	public String getSummaryImage() {
		return summaryImage;
	}

	public void setSummaryImage(String summaryImage) {
		this.summaryImage = summaryImage;
	}

	public Long getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(Long appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
