package com.store.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 预约订单表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-07-31
 */
@TableName("shop_subscribe_order")
public class SubscribeOrder extends Model<SubscribeOrder> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	  /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
	
	/**
     * 商品ID
     */
	@TableField("shop_product_id")
	private Long shopProductId;
	
	/**
     * 商品名称
     */
	@TableField("shop_product_name")
	private String shopProductName;
	
	/**
     * 商品图片
     */
	@TableField("shop_product_img")
	private String shopProductImg;
	
	
    /**
     * 收件人姓名
     */
	private String name;
    /**
     * 收件人手机号
     */
	private String phone;
    /**
     * 收货地址
     */
	private String address;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Long getShopProductId() {
		return shopProductId;
	}

	public void setShopProductId(Long shopProductId) {
		this.shopProductId = shopProductId;
	}

	public String getShopProductName() {
		return shopProductName;
	}

	public void setShopProductName(String shopProductName) {
		this.shopProductName = shopProductName;
	}

	public String getShopProductImg() {
		return shopProductImg;
	}

	public void setShopProductImg(String shopProductImg) {
		this.shopProductImg = shopProductImg;
	}

	
	
	

}
