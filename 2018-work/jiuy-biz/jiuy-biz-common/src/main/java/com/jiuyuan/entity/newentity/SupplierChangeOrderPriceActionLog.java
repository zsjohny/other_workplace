package com.jiuyuan.entity.newentity;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 供应商改价操作日志
 * </p>
 *
 * @author nijin
 * @since 2018-02-26
 */
@TableName("supplier_change_order_price_action_log")
public class SupplierChangeOrderPriceActionLog extends Model<SupplierChangeOrderPriceActionLog> {

    private static final long serialVersionUID = 1L;
    
    public static final String ACTION_NAME_CHANGE_PRICE = "供应商改价";
    public static final String ACTION_NAME_RESTORE_PRICE = "供应商恢复价格";
    
    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 订单号
     */
	@TableField("order_no")
	private Long orderNo;
    /**
     * 供应商ID
     */
	@TableField("supplier_id")
	private Long supplierId;
    /**
     * 订单改价前实付金额（不包含邮费）
     */
	@TableField("old_totalPay")
	private Double oldTotalPay;
    /**
     * 订单改价后实付金额（不包含邮费）
     */
	@TableField("new_totalPay")
	private Double newTotalPay;
    /**
     * 订单原始的实付金额（不包含邮费）
     */
	@TableField("original_totalPay")
	private Double originalTotalPay;
    /**
     * 登录ip
     */
	private String ip;
    /**
     * 操作时间
     */
	@TableField("action_time")
	private Long actionTime;
    /**
     * 操作名称
     */
	@TableField("action_name")
	private String actionName;
	/**
	 * 订单支付时生成的订单号,用于微信支付,支付宝支付
	 */
	@TableField("out_trade_no")
	private String outTradeNo;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Double getOldTotalPay() {
		return oldTotalPay;
	}

	public void setOldTotalPay(Double oldTotalPay) {
		this.oldTotalPay = oldTotalPay;
	}

	public Double getNewTotalPay() {
		return newTotalPay;
	}

	public void setNewTotalPay(Double newTotalPay) {
		this.newTotalPay = newTotalPay;
	}

	public Double getOriginalTotalPay() {
		return originalTotalPay;
	}

	public void setOriginalTotalPay(Double originalTotalPay) {
		this.originalTotalPay = originalTotalPay;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getActionTime() {
		return actionTime;
	}

	public void setActionTime(Long actionTime) {
		this.actionTime = actionTime;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Override
	public String toString() {
		return "SupplierChangeOrderPriceActionLog{" +
			"id=" + id +
			", orderNo=" + orderNo +
			", supplierId=" + supplierId +
			", oldTotalPay=" + oldTotalPay +
			", newTotalPay=" + newTotalPay +
			", originalTotalPay=" + originalTotalPay +
			", ip=" + ip +
			", actionTime=" + actionTime +
			", outTradeNo=" + outTradeNo +
			"}";
	}
}
