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
 * 商品审核信息表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-13
 */
@TableName("yjj_ProductAudit")
public class ProductAudit extends Model<ProductAudit> {

    private static final long serialVersionUID = 1L;

    //审核类型：0买手审核、1客服审核
//    public static final int auditType_server = 0;//客服审核
//    public static final int auditType_buyer = 1;//买手审核
//    审核状态：0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、4买手已通过、5买手已拒绝
	public static final int auditState_server_wait = 0;// 待审核
	public static final int auditState_server_pass = 1;//通过
	public static final int auditState_server_no_pass = 2;//拒绝
	public static final int auditState_buyer_wait = 3;// 待审核
	public static final int auditState_buyer_pass = 4;//通过
	public static final int auditState_buyer_no_pass = 5;//拒绝
    

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 审核状态：0客服待审核、1客服已通过、2客服已拒绝、3买手待审核、4买手已通过、5买手已拒绝
     */
	private Integer auditState;
    /**
     * 审核不通过理由
     */
	private String noPassReason;
	 /**
     * 审核时间 `submitAuditTime` bigint(20) DEFAULT '0' COMMENT '提交审核时间',
     */
	private Long auditTime;
	 /**
     * 提交审核时间
     */
	private Long submitAuditTime;
    /**
     * 商家ID
     */
	private Long supplierId;
    /**
     * 品牌ID
     */
	private Long brandId;
    /**
     * 品牌名称
     */
	private String brandName;
    /**
     * 品牌Logo
     */
	private String brandLogo;
    /**
     * 商品ID
     */
	private Long productId;
    /**
     * 商品名称
     */
	private String productName;
    /**
     * 商品款号
     */
	private String clothesNumber;
    /**
     * 橱窗图片集合，英文逗号分隔
     */
	private String showcaseImgs;
    /**
     * 详情图片集合，英文逗号分隔
     */
	private String detailImgs;
    /**
     * 商品视频url
     */
	private String videoUrl;
    /**
     * 商品主图
     */
	private String mainImg;
    /**
     * 商品品类名称，只用于显示，格式例：裙装 -> 连衣裙
     */
	private String categoryName;
    /**
     * 商品SKUJSON
     */
	private String skuJSON;
    /**
     * 搭配商品ID集合，英文逗号分隔
     */
	private String matchProductIds;
    /**
     * 阶梯价格JSON
     */
	private String ladderPriceJson;
    /**
     * 最大阶梯价格
     */
	private Double maxLadderPrice;
    /**
     * 最小阶梯价格
     */
	private Double minLadderPrice;
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

	

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public String getNoPassReason() {
		return noPassReason;
	}

	public void setNoPassReason(String noPassReason) {
		this.noPassReason = noPassReason;
	}

	public Long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Long auditTime) {
		this.auditTime = auditTime;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
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

	public String getShowcaseImgs() {
		return showcaseImgs;
	}

	public void setShowcaseImgs(String showcaseImgs) {
		this.showcaseImgs = showcaseImgs;
	}

	public String getDetailImgs() {
		return detailImgs;
	}

	public void setDetailImgs(String detailImgs) {
		this.detailImgs = detailImgs;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getMainImg() {
		return mainImg;
	}

	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSkuJSON() {
		return skuJSON;
	}

	public void setSkuJSON(String skuJSON) {
		this.skuJSON = skuJSON;
	}

	

	public String getMatchProductIds() {
		return matchProductIds;
	}

	public void setMatchProductIds(String matchProductIds) {
		this.matchProductIds = matchProductIds;
	}

	public String getLadderPriceJson() {
		return ladderPriceJson;
	}

	public void setLadderPriceJson(String ladderPriceJson) {
		this.ladderPriceJson = ladderPriceJson;
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

	public Double getMaxLadderPrice() {
		return maxLadderPrice;
	}

	public void setMaxLadderPrice(Double maxLadderPrice) {
		this.maxLadderPrice = maxLadderPrice;
	}

	public Double getMinLadderPrice() {
		return minLadderPrice;
	}

	public void setMinLadderPrice(Double minLadderPrice) {
		this.minLadderPrice = minLadderPrice;
	}

	public Long getSubmitAuditTime() {
		return submitAuditTime;
	}

	public void setSubmitAuditTime(Long submitAuditTime) {
		this.submitAuditTime = submitAuditTime;
	}

	

}
