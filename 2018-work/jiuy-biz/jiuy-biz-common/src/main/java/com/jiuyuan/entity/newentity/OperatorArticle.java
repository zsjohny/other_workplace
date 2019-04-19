package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.jiuyuan.util.DateUtil;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 官网文章
 * </p>
 *
 * @author nijin
 * @since 2018-04-13
 */
@TableName("operator_article")
public class OperatorArticle extends Model<OperatorArticle> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 标题
     */
	private String title;
    /**
     * 预览图
     */
	@TableField("preview_image_url")
	private String previewImageUrl;
    /**
     * 摘要
     */
	private String abstracts;
    /**
     * 文章
     */
	private String content;
    /**
     * 状态：0:正常 -1:删除
     */
	private Integer status;
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
    /**
     * 删除时间
     */
	@TableField("delete_time")
	private Long deleteTime;
    /**
     * 创建管理员ID
     */
	@TableField("create_operator_id")
	private Long createOperatorId;
    /**
     * 删除管理员ID
     */
	@TableField("delete_operator_id")
	private Long deleteOperatorId;
    /**
     * seo标题
     */
	@TableField("seo_title")
	private String seoTitle;
    /**
     * seo描述
     */
	@TableField("seo_descriptor")
	private String seoDescriptor;
    /**
     * seo关键词
     */
	@TableField("seo_keywords")
	private String seoKeywords;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPreviewImageUrl() {
		return previewImageUrl;
	}

	public void setPreviewImageUrl(String previewImageUrl) {
		this.previewImageUrl = previewImageUrl;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Long getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Long deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Long getCreateOperatorId() {
		return createOperatorId;
	}

	public void setCreateOperatorId(Long createOperatorId) {
		this.createOperatorId = createOperatorId;
	}

	public Long getDeleteOperatorId() {
		return deleteOperatorId;
	}

	public void setDeleteOperatorId(Long deleteOperatorId) {
		this.deleteOperatorId = deleteOperatorId;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoDescriptor() {
		return seoDescriptor;
	}

	public void setSeoDescriptor(String seoDescriptor) {
		this.seoDescriptor = seoDescriptor;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}
	
	public String getCreateDate(){
		if(this.getCreateTime() == null){
			return "";
		}
		return DateUtil.getDateByLongTime(this.getCreateTime());
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
