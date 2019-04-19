package com.jiuyuan.entity.newentity.dynamicproperty;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 属性值表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@TableName("yjj_dynamic_property_value")
public class DynamicPropertyValue extends Model<DynamicPropertyValue> {

    private static final long serialVersionUID = 1L;
    public static final int DYNA_PROP_VALUE_STATUS_ON=1;
    public static final int DYNA_PROP_VALUE_STATUS_OFF=0;
    
    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 属性id
     */
	@TableField("dyna_prop_id")
	private Long dynaPropId;
    /**
     * 属性值
     */
	@TableField("dyna_prop_value")
	private String dynaPropValue;
    /**
     * 状态 0：禁用，1：启用
     */
	private Integer status;
	/**
     * 排序值
     */
	private Integer weight;
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

	public Long getDynaPropId() {
		return dynaPropId;
	}

	public void setDynaPropId(Long dynaPropId) {
		this.dynaPropId = dynaPropId;
	}

	public String getDynaPropValue() {
		return dynaPropValue;
	}

	public void setDynaPropValue(String dynaPropValue) {
		this.dynaPropValue = dynaPropValue;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
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
