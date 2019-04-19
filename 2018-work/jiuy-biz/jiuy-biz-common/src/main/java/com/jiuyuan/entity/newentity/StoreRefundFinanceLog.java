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
 * 售后退款日志
 * </p>
 *
 * @author nijin
 * @since 2017-12-26
 */
@TableName("store_refund_finance_log")
public class StoreRefundFinanceLog extends Model<StoreRefundFinanceLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家id
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 1：退款记录
     */
	private Integer type;
    /**
     * 订单号
     */
	@TableField("order_no")
	private Long orderNo;
    /**
     * 售后ID
     */
	@TableField("refund_order_id")
	private Long refundOrderId;
    /**
     * 退款金额
     */
	@TableField("refund_cost")
	private Double refundCost;
    /**
     * 创建时间
     */
	private Long createtime;
    /**
     * 更新时间
     */
	private Long updatetime;
    /**
     * 付款方式：0:未知 2:支付宝 3:微信
     */
	@TableField("payment_type")
	private Integer paymentType;
    /**
     * 第三方支付订单号
     */
	@TableField("payment_no")
	private String paymentNo;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Long getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(Long refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public Double getRefundCost() {
		return refundCost;
	}

	public void setRefundCost(Double refundCost) {
		this.refundCost = refundCost;
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

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "StoreRefundFinanceLog{" +
			"id=" + id +
			", storeId=" + storeId +
			", type=" + type +
			", orderNo=" + orderNo +
			", refundOrderId=" + refundOrderId +
			", refundCost=" + refundCost +
			", createtime=" + createtime +
			", updatetime=" + updatetime +
			", paymentType=" + paymentType +
			", paymentNo=" + paymentNo +
			"}";
	}
}
