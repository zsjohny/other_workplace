package com.jiuyuan.entity.newentity.dynamicproperty;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 动态属性与商品表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-08
 */
@TableName("yjj_dynamic_property_product")
public class DynamicPropertyProduct extends Model<DynamicPropertyProduct> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品id
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 属性id
     */
	@TableField("dyna_prop_id")
	private Long dynaPropId;
    /**
     * 属性值id
     */
	@TableField("dyna_prop_value_id")
	private Long dynaPropValueId;
    /**
     * 属性值
     */
	@TableField("dyna_prop_value")
	private String dynaPropValue;
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getDynaPropId() {
		return dynaPropId;
	}

	public void setDynaPropId(Long dynaPropId) {
		this.dynaPropId = dynaPropId;
	}

	public Long getDynaPropValueId() {
		return dynaPropValueId;
	}

	public void setDynaPropValueId(Long dynaPropValueId) {
		this.dynaPropValueId = dynaPropValueId;
	}

	public String getDynaPropValue() {
		return dynaPropValue;
	}

	public void setDynaPropValue(String dynaPropValue) {
		this.dynaPropValue = dynaPropValue;
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
