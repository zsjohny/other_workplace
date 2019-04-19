package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品属性值组表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-19
 */
@TableName("yjj_PropertyValueGroup")
public class PropertyValueGroup extends Model<PropertyValueGroup> {

    private static final long serialVersionUID = 1L;

    /**
     * 属性值组id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 属性值组名称
     */
	private String GroupName;
    /**
     * 对应属性名id
     */
	private Long PropertyNameId;
    /**
     * 对应商品分类id
     */
	private Long categoryId;
    /**
     * 状态:-1删除，0正常
     */
	private Integer Status;
    /**
     * 排序索引
     */
	private Integer OrderIndex;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String GroupName) {
		this.GroupName = GroupName;
	}

	public Long getPropertyNameId() {
		return PropertyNameId;
	}

	public void setPropertyNameId(Long PropertyNameId) {
		this.PropertyNameId = PropertyNameId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Integer getOrderIndex() {
		return OrderIndex;
	}

	public void setOrderIndex(Integer OrderIndex) {
		this.OrderIndex = OrderIndex;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	public Long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Long UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
