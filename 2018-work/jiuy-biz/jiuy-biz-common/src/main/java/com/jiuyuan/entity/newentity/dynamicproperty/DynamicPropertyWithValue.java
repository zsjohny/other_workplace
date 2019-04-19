package com.jiuyuan.entity.newentity.dynamicproperty;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 动态属性和动态属性值
 * </p>
 *
 * @author Charlie(唐静)
 * @date 18/05/11
 */
@TableName("yjj_dynamic_property")
public class DynamicPropertyWithValue extends Model<DynamicPropertyWithValue> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 属性组id
     */
	@TableField("dyna_prop_group_id")
	private Long dynaPropGroupId;
    /**
     * 名称
     */
	private String name;
    /**
     * 权重
     */
	private Integer weight;
    /**
     * 是否必填1:必填,0：非必填
     */
	@TableField("is_fill")
	private Integer isFill;
    /**
     * 是否显示属性名称（仅在商品新建编辑时使用）1:显示,0:不显示
     */
	@TableField("is_display")
	private Integer isDisplay;
    /**
     * 表单类型 0：单选，1：多选，2：列表单选，3：列表多选，4：单行文字输入，5：多行文字输入
     */
	@TableField("form_type")
	private Integer formType;
    /**
     * 状态 0：禁用，1：启用
     */
	private Integer status;
	/**
     * 备注
     */
	private String remark;
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

	private List<DynamicPropertyValue> dynamicPropertyValueList;

	public List<DynamicPropertyValue> getDynamicPropertyValueList() {
		return dynamicPropertyValueList;
	}

	public void setDynamicPropertyValueList(List<DynamicPropertyValue> dynamicPropertyValueList) {
		this.dynamicPropertyValueList = dynamicPropertyValueList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDynaPropGroupId() {
		return dynaPropGroupId;
	}

	public void setDynaPropGroupId(Long dynaPropGroupId) {
		this.dynaPropGroupId = dynaPropGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getIsFill() {
		return isFill;
	}

	public void setIsFill(Integer isFill) {
		this.isFill = isFill;
	}

	public Integer getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}

	public Integer getFormType() {
		return formType;
	}

	public void setFormType(Integer formType) {
		this.formType = formType;
	}

	public Integer getStatus() {
		return status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
