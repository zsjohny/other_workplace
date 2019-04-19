package com.store.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品销量
 * </p>
 *
 * @author Aison
 * @since 2018-06-19
 */
@TableName("yjj_sales_volume_product")
public class SalesVolumeProduct extends Model<SalesVolumeProduct> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品主键
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 商品销量
     */
	@TableField("sales_volume")
	private Long salesVolume;
    /**
     * 收藏数量
     */
	@TableField("collection_count")
	private Long collectionCount;
    /**
     * 点赞数量
     */
	@TableField("star_count")
	private Long starCount;
    /**
     * 下单数量
     */
	@TableField("order_count")
	private Long orderCount;
    /**
     * 成功订单的数量
     */
	@TableField("order_success_count")
	private Long orderSuccessCount;
    /**
     * 退款数量
     */
	@TableField("refund_count")
	private Long refundCount;
    /**
     * 浏览次数
     */
	@TableField("view_count")
	private Long viewCount;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private Date updateTime;
    /**
     * 商品类型:1 限时抢购商品,2 普通商品
     */
	@TableField("product_type")
	private Integer productType;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Long salesVolume) {
		this.salesVolume = salesVolume;
	}

	public Long getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Long collectionCount) {
		this.collectionCount = collectionCount;
	}

	public Long getStarCount() {
		return starCount;
	}

	public void setStarCount(Long starCount) {
		this.starCount = starCount;
	}

	public Long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public Long getOrderSuccessCount() {
		return orderSuccessCount;
	}

	public void setOrderSuccessCount(Long orderSuccessCount) {
		this.orderSuccessCount = orderSuccessCount;
	}

	public Long getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Long refundCount) {
		this.refundCount = refundCount;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
