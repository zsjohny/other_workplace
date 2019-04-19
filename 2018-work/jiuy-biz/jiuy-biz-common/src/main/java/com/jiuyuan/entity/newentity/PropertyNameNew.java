package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品属性名表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-19
 */
@TableName("yjj_PropertyName")
public class PropertyNameNew extends Model<PropertyNameNew> {

    private static final long serialVersionUID = 1L;

    /**
     * 属性名id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 属性名称
     */
	private String PropertyName;
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

	public String getPropertyName() {
		return PropertyName;
	}

	public void setPropertyName(String PropertyName) {
		this.PropertyName = PropertyName;
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
