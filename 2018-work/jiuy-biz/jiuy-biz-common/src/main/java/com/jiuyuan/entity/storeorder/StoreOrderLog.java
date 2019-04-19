package com.jiuyuan.entity.storeorder;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 订单日志记录
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-25
 */
@TableName("store_OrderLog")
public class StoreOrderLog extends Model<StoreOrderLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 关联User表的userId
     */
	private Long StoreId;
    /**
     * 关联Order表的id
     */
	private Long OrderNo;
    /**
     * 老的订单状态
     */
	private Integer OldStatus;
    /**
     * 更新的订单状态
     */
	private Integer NewStatus;
    /**
     * 记录创建时间
     */
	private Long CreateTime;
	private Long UpdateTime;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getStoreId() {
		return StoreId;
	}

	public void setStoreId(Long StoreId) {
		this.StoreId = StoreId;
	}

	public Long getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(Long OrderNo) {
		this.OrderNo = OrderNo;
	}

	public Integer getOldStatus() {
		return OldStatus;
	}

	public void setOldStatus(Integer OldStatus) {
		this.OldStatus = OldStatus;
	}

	public Integer getNewStatus() {
		return NewStatus;
	}

	public void setNewStatus(Integer NewStatus) {
		this.NewStatus = NewStatus;
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
