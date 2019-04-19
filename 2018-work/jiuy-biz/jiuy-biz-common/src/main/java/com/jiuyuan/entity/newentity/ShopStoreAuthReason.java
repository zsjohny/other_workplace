package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员认证不通过原因预设
 * </p>
 *
 * @author nijin
 * @since 2017-12-14
 */
@TableName("shop_store_auth_reason")
public class ShopStoreAuthReason extends Model<ShopStoreAuthReason> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 认证审核不通过原因
     */
	@TableField("no_pass_reason")
	private String noPassReason;
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
     * 拒绝原因类型：0：门店认证1：商品认证
     */
	@TableField("TYPE")
	private Integer type;
	/**
	 * 权重
	 */
	@TableField("weight")
	private Integer weight;
	/**
	 * 是否删除
	 */
	@TableField("is_delete")
	private Integer isDelete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNoPassReason() {
		return noPassReason;
	}

	public void setNoPassReason(String noPassReason) {
		this.noPassReason = noPassReason;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "ShopStoreAuthReason{" +
			"id=" + id +
			", noPassReason=" + noPassReason +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", type=" + type +
			"}";
	}
}
