package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 供应商分组表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
@TableName("supplier_customer_group")
public class SupplierCustomerGroup extends Model<SupplierCustomerGroup> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 供应商Id
     */
	@TableField("supplier_id")
	private Long supplierId;
    /**
     * 分组名称
     */
	@TableField("group_name")
	private String groupName;
    /**
     * 备注
     */
	private String remark;
    /**
     *   状态  -1：删除   0：正常
     */
	private Integer status;
	/**
	 * 默认分组：0 否 1是
	 */
 	@TableField("default_group")
 	private Integer defaultGroup;
 	
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

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(Integer defaultGroup) {
		this.defaultGroup = defaultGroup;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
