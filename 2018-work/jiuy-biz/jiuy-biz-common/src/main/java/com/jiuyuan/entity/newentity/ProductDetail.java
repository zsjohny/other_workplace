package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品详情信息表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-13
 */
@TableName("yjj_ProductDetail")
public class ProductDetail extends Model<ProductDetail> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品ID
     */
	private Long productId;
    /**
     * 橱窗图片集合
     */
	private String showcaseImgs;
    /**
     * 详情图片集合
     */
	private String detailImgs;
    /**
     * 商品视频url
     */
	private String videoUrl;
	 /**
     * 视频名称
     */
	private String videoName;
    /**
     * 商品视频fileId
     */
	private Long videoFileId;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 更新时间
     */
	private Long updateTime;
	/**
	 * 上架类型 1:审核通过后立即上架, 2:定时上架, 3:暂不上架(不设置)(default 3)
	 */
	private Integer putawayType;
	/**
	 * 定时上架时间(default 0)
	 */
	private Long timingPutwayTime;

	public Integer getPutawayType() {
		return putawayType;
	}

	public void setPutawayType(Integer putawayType) {
		this.putawayType = putawayType;
	}

	public Long getTimingPutwayTime() {
		return timingPutwayTime;
	}

	public void setTimingPutwayTime(Long timingPutwayTime) {
		this.timingPutwayTime = timingPutwayTime;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public Long getVideoFileId() {
		return videoFileId;
	}

	public void setVideoFileId(Long videoFileId) {
		this.videoFileId = videoFileId;
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
