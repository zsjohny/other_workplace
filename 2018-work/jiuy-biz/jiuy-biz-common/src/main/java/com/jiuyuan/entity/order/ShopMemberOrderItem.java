package com.jiuyuan.entity.order;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员订单明细表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-05
 */
@TableName("shop_member_order_item")
public class ShopMemberOrderItem extends Model<ShopMemberOrderItem> {

    private static final long serialVersionUID = 1L;
    
    public static int  platform_product  = 0 ;//平台商品
	public static int  OWN_PRODUCT  = 1 ;//自有商品

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 订单ID
     */
	@TableField("order_id")
	private Long orderId;
    /**
     * 订单编号
     */
	@TableField("order_number")
	private String orderNumber;
    /**
     * 商家商品ID
     */
	@TableField("shop_product_id")
	private Long shopProductId;
    /**
     * 商家商品类型(自有商品或平台商品):1是自有商品，0平台商品
     */
	private Integer own;
	/**
	 * 直播商品id
	 */
	@TableField("live_product_id")
	private Long liveProductId;
    /**
     * 平台商品ID
     */
	@TableField("product_id")
	private Long productId;
    /**
     * SKUID
     */
	@TableField("product_sku_id")
	private Long productSkuId;
    /**
     * 购买数量
     */
	private Integer count;
    /**
     * 商品主图
     */
	@TableField("summary_images")
	private String summaryImages;
    /**
     * 商品标题
     */
	private String name;
    /**
     * 商品颜色名称
     */
	private String color;
    /**
     * 商品尺码名称
     */
	private String size;
    /**
     * 商品价格
     */
	private Double price;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;

	@TableField("self_count")
	private Integer selfCount;

	@TableField("supplier_count")
	private Integer supplierCount;

	public Integer getSelfCount() {
		return selfCount;
	}

	public void setSelfCount(Integer selfCount) {
		this.selfCount = selfCount;
	}

	public Integer getSupplierCount() {
		return supplierCount;
	}

	public void setSupplierCount(Integer supplierCount) {
		this.supplierCount = supplierCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getShopProductId() {
		return shopProductId;
	}

	public void setShopProductId(Long shopProductId) {
		this.shopProductId = shopProductId;
	}

	public Integer getOwn() {
		return own;
	}

	public void setOwn(Integer own) {
		this.own = own;
	}

	public Long getLiveProductId() {
		return liveProductId;
	}

	public void setLiveProductId(Long liveProductId) {
		this.liveProductId = liveProductId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getProductSkuId() {
		return productSkuId;
	}

	public void setProductSkuId(Long productSkuId) {
		this.productSkuId = productSkuId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSummaryImages() {
		return summaryImages;
	}

	public void setSummaryImages(String summaryImages) {
		this.summaryImages = summaryImages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
