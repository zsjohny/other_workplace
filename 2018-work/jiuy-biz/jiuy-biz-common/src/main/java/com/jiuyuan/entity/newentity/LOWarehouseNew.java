package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 物流仓库
 * </p>
 *
 * @author nijin
 * @since 2017-10-20
 */
@TableName("yjj_LOWarehouse")
public class LOWarehouseNew extends Model<LOWarehouseNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 仓库名字
     */
	private String Name;
    /**
     * 发货地
     */
	private Long DeliveryLocation;
	private String Description;
    /**
     * 是否免邮 0:否 1:是
     */
	private Integer IsFree;
    /**
     * 几件免邮，在IsFree为1的情况下有效

     */
	private Integer FreeCount;
    /**
     * 0:正常， -1：删除
     */
	private Integer Status;
    /**
     * 创建时间 
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;
    /**
     * 库存同步设置 0:人工 1:同步ERP

     */
	private Integer RemainCountSyncSet;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public Long getDeliveryLocation() {
		return DeliveryLocation;
	}

	public void setDeliveryLocation(Long DeliveryLocation) {
		this.DeliveryLocation = DeliveryLocation;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public Integer getIsFree() {
		return IsFree;
	}

	public void setIsFree(Integer IsFree) {
		this.IsFree = IsFree;
	}

	public Integer getFreeCount() {
		return FreeCount;
	}

	public void setFreeCount(Integer FreeCount) {
		this.FreeCount = FreeCount;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
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

	public Integer getRemainCountSyncSet() {
		return RemainCountSyncSet;
	}

	public void setRemainCountSyncSet(Integer RemainCountSyncSet) {
		this.RemainCountSyncSet = RemainCountSyncSet;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

	@Override
	public String toString() {
		return "YjjLOWarehouse{" +
			"Id=" + Id +
			", Name=" + Name +
			", DeliveryLocation=" + DeliveryLocation +
			", Description=" + Description +
			", IsFree=" + IsFree +
			", FreeCount=" + FreeCount +
			", Status=" + Status +
			", CreateTime=" + CreateTime +
			", UpdateTime=" + UpdateTime +
			", RemainCountSyncSet=" + RemainCountSyncSet +
			"}";
	}
}
