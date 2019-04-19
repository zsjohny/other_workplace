package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 官网seo信息
 * </p>
 *
 * @author nijin
 * @since 2018-04-13
 */
@TableName("operator_seo")
public class OperatorSeo extends Model<OperatorSeo> {

    private static final long serialVersionUID = 1L;
    
    public static final int SEO_TYPE_HOME = 1;
    
    public static final int SEO_TYPE_DEFAULT = 2;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * seo标题
     */
	private String title;
    /**
     * seo描述
     */
	private String descriptor;
    /**
     * seo关键词
     */
	private String keywords;
    /**
     * seo类型:1:首页 2:页面默认seo
     */
	@TableField("seo_type")
	private Integer seoType;
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

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getSeoType() {
		return seoType;
	}

	public void setSeoType(Integer seoType) {
		this.seoType = seoType;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
