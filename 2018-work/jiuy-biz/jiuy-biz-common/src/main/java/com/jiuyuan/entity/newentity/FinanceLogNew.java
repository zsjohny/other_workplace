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
 * 供应商财务收入支出表
 * </p>
 *
 * @author nijin
 * @since 2017-10-18
 */
@TableName("supplier_finance_log")
public class FinanceLogNew extends Model<FinanceLogNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 供应商id
     */
	@TableField("supplier_id")
	private Long supplierId;
    /**
     * 1：收入-现金收益(提成) relatedId代表订单号， 2：支出-供应商提现申请后审核通过  relatedId代表商家提现申请审批表id
     */
	private Integer type;
    /**
     * 相关id
     */
	private Long relatedid;
    /**
     * 现金(提成)
     */
	private BigDecimal cash;
    /**
     * 创建时间
     */
	private Long createtime;
    /**
     * 更新时间
     */
	private Long updatetime;
	/**
	 * 原供应商可提现金额
	 */
	@TableField("old_available_balance")
	private BigDecimal oldAvailableBalance;
	/**
	 * 当前供应商可提现金额
	 */
	@TableField("new_available_balance")
	private BigDecimal newAvailableBalance;
	
	


	public BigDecimal getOldAvailableBalance() {
		return oldAvailableBalance;
	}

	public void setOldAvailableBalance(BigDecimal oldAvailableBalance) {
		this.oldAvailableBalance = oldAvailableBalance;
	}

	public BigDecimal getNewAvailableBalance() {
		return newAvailableBalance;
	}

	public void setNewAvailableBalance(BigDecimal newAvailableBalance) {
		this.newAvailableBalance = newAvailableBalance;
	}

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getRelatedid() {
		return relatedid;
	}

	public void setRelatedid(Long relatedid) {
		this.relatedid = relatedid;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public Long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}

	public Long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SupplierFinanceLog [id=" + id + ", supplierId=" + supplierId + ", type=" + type + ", relatedid="
				+ relatedid + ", cash=" + cash + ", createtime=" + createtime + ", updatetime=" + updatetime
				+ ", oldAvailableBalance=" + oldAvailableBalance + ", newAvailableBalance=" + newAvailableBalance + "]";
	}

	

}
