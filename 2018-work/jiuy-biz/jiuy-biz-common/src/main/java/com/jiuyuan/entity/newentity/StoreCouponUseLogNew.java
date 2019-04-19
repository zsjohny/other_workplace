package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author nijin
 * @since 2018-03-13
 */
@TableName("store_CouponUseLog")
public class StoreCouponUseLogNew extends Model<StoreCouponUseLogNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 代金券id
     */
	private Long CouponId;
	private Long UserId;
    /**
     * 相关订单号
     */
	private Long OrderNo;
    /**
     * 代金券实际抵扣金额
     */
	private Double ActualDiscount;
    /**
     * 0：使用，1：订单取消归还
     */
	private Integer Status;
	private Long CreateTime;
    /**
     * 供应商ID，为0表示平台优惠券
     */
	@TableField("supplier_id")
	private Long supplierId;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getCouponId() {
		return CouponId;
	}

	public void setCouponId(Long CouponId) {
		this.CouponId = CouponId;
	}

	public Long getUserId() {
		return UserId;
	}

	public void setUserId(Long UserId) {
		this.UserId = UserId;
	}

	public Long getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(Long OrderNo) {
		this.OrderNo = OrderNo;
	}

	public Double getActualDiscount() {
		return ActualDiscount;
	}

	public void setActualDiscount(Double ActualDiscount) {
		this.ActualDiscount = ActualDiscount;
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

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
