package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 售后订单表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-18
 */
@TableName("store_refund_order_action_log")
public class RefundOrderActionLog extends Model<RefundOrderActionLog> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 售后订单ID
     */
	@TableField("refund_order_id")
	private Long refundOrderId;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(Long refundOrderId) {
		this.refundOrderId = refundOrderId;
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

}
