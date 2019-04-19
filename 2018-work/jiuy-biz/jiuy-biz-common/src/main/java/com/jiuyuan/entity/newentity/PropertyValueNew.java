package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品属性值表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-19
 */
@TableName("yjj_PropertyValue")
public class PropertyValueNew extends Model<PropertyValueNew> {

    private static final long serialVersionUID = 1L;

    /**
     * 属性值id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 属性值
     */
	private String PropertyValue;
    /**
     * 对应属性名id
     */
	private Long PropertyNameId;
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
    /**
     * 供应商ID(0:通用, -1:门店用户拥有, >0:供应商拥有)
     */
	private Long supplierId;
    /**
     * 属性值组ID
     */
	private Long propertyValueGroupId;

	/**
	 * 门店用户id
	 * 当门店用户不为0时,supplierId=-1
	 */
	private Long storeId;


	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getPropertyValue() {
		return PropertyValue;
	}

	public void setPropertyValue(String PropertyValue) {
		this.PropertyValue = PropertyValue;
	}

	public Long getPropertyNameId() {
		return PropertyNameId;
	}

	public void setPropertyNameId(Long PropertyNameId) {
		this.PropertyNameId = PropertyNameId;
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

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getPropertyValueGroupId() {
		return propertyValueGroupId;
	}

	public void setPropertyValueGroupId(Long propertyValueGroupId) {
		this.propertyValueGroupId = propertyValueGroupId;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
