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
 * @since 2018-03-13
 */
@TableName("yjj_restriction_activity_product_sku")
public class RestrictionActivityProductSku extends Model<RestrictionActivityProductSku> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 活动商品id
     */
	@TableField("activity_product_id")
	private Long activityProductId;
    /**
     * 原商品id
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 活动商品skuId
     */
	@TableField("product_sku_id")
	private Long productSkuId;
    /**
     * 颜色名称
     */
	@TableField("color_name")
	private String colorName;
    /**
     * 尺码名称
     */
	@TableField("size_name")
	private String sizeName;
    /**
     * 活动商品sku剩余库存量
     */
	@TableField("remain_count")
	private Integer remainCount;
    /**
     * 活动商品sku总库存量
     */
	@TableField("total_remain_count")
	private Integer totalRemainCount;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private Long updateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivityProductId() {
		return activityProductId;
	}

	public void setActivityProductId(Long activityProductId) {
		this.activityProductId = activityProductId;
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

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public Integer getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(Integer remainCount) {
		this.remainCount = remainCount;
	}

	public Integer getTotalRemainCount() {
		return totalRemainCount;
	}

	public void setTotalRemainCount(Integer totalRemainCount) {
		this.totalRemainCount = totalRemainCount;
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

	@Override
	public String toString() {
		return "RestrictionActivityProductSku [id=" + id + ", activityProductId=" + activityProductId + ", productId="
				+ productId + ", productSkuId=" + productSkuId + ", colorName=" + colorName + ", sizeName=" + sizeName
				+ ", remainCount=" + remainCount + ", totalRemainCount=" + totalRemainCount + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

}
