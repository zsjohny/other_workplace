package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
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
@TableName("yjj_restriction_activity_product")
public class RestrictionActivityProduct extends Model<RestrictionActivityProduct> {

    private static final long serialVersionUID = 1L;
    
    /**
     * 0:待上架
     */
    public static final int to_be_on_the_shelves = 0;
    /**
     * 1:已上架
     */
    public static final int on_the_shelves = 1;
    /**
     * 2:已下架
     */
    public static final int has_been_removed = 2;
    /**
     * 3:已删除
     */
    public static final int deleted = 3;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 原商品id
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 活动商品名称
     */
	@TableField("product_name")
	private String productName;
    /**
     * 活动商品款号
     */
	@TableField("clothes_number")
	private String clothesNumber;
    /**
     * 0:待上架;1:已上架;2:已下架;3:已删除
     */
	@TableField("product_status")
	private Integer productStatus;
    /**
     * 活动商品的sku个数
     */
	@TableField("sku_count")
	private Integer skuCount;
    /**
     * 活动商品预览图
     */
	@TableField("main_image")
	private String mainImage;
    /**
     * 默认活动商品推广图片,原商品橱窗图
     */
	@TableField("showcase_image")
	private String showcaseImage;
    /**
     * 活动商品推广图片
     */
	@TableField("promotion_image")
	private String promotionImage;
    /**
     * 活动当前剩余库存量
     */
	@TableField("remain_count")
	private Integer remainCount;
    /**
     * 活动总库存量
     */
	@TableField("total_remain_count")
	private Integer totalRemainCount;
    /**
     * 销量
     */
	@TableField("sale_count")
	private Integer saleCount;
	/**
     * 限购数量
     */
	@TableField("restriction_count")
	private Integer restrictionCount;
    /**
     * 活动商品价格
     */
	@TableField("activity_product_price")
	private Double activityProductPrice;
    /**
     * 活动商品原价
     */
	@TableField("product_price")
	private Double productPrice;
    /**
     * 活动开始时间
     */
	@TableField("activity_begin_time")
	private Long activityBeginTime;
    /**
     * 活动结束时间
     */
	@TableField("activity_end_time")
	private Long activityEndTime;
    /**
     * 活动商品上架时间
     */
	@TableField("activity_product_shelf_time")
	private Long activityProductShelfTime;
    /**
     * 活动商品下架时间
     */
	@TableField("activity_product_drop_off_time")
	private Long activityProductDropOffTime;
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
	/**
	 * 最小购买量/起订量
	 */
	@TableField("mini_purchase_count")
	private Integer miniPurchaseCount;


	public Integer getMiniPurchaseCount() {
		return miniPurchaseCount;
	}

	public void setMiniPurchaseCount(Integer miniPurchaseCount) {
		this.miniPurchaseCount = miniPurchaseCount;
	}

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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getClothesNumber() {
		return clothesNumber;
	}

	public void setClothesNumber(String clothesNumber) {
		this.clothesNumber = clothesNumber;
	}

	public Integer getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	public Integer getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public String getShowcaseImage() {
		return showcaseImage;
	}

	public void setShowcaseImage(String showcaseImage) {
		this.showcaseImage = showcaseImage;
	}

	public String getPromotionImage() {
		return promotionImage;
	}

	public void setPromotionImage(String promotionImage) {
		this.promotionImage = promotionImage;
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

	public Integer getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
	}

	public Integer getRestrictionCount() {
		return restrictionCount;
	}

	public void setRestrictionCount(Integer restrictionCount) {
		this.restrictionCount = restrictionCount;
	}

	public Double getActivityProductPrice() {
		return activityProductPrice;
	}

	public void setActivityProductPrice(Double activityProductPrice) {
		this.activityProductPrice = activityProductPrice;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public Long getActivityBeginTime() {
		return activityBeginTime;
	}

	public void setActivityBeginTime(Long activityBeginTime) {
		this.activityBeginTime = activityBeginTime;
	}

	public Long getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Long activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public Long getActivityProductShelfTime() {
		return activityProductShelfTime;
	}

	public void setActivityProductShelfTime(Long activityProductShelfTime) {
		this.activityProductShelfTime = activityProductShelfTime;
	}

	public Long getActivityProductDropOffTime() {
		return activityProductDropOffTime;
	}

	public void setActivityProductDropOffTime(Long activityProductDropOffTime) {
		this.activityProductDropOffTime = activityProductDropOffTime;
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
