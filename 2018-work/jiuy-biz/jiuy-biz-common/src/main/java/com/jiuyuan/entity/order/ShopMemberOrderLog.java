package com.jiuyuan.entity.order;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-12
 */
@TableName("shop_member_order_log")
public class ShopMemberOrderLog extends Model<ShopMemberOrderLog> {

    private static final long serialVersionUID = 1L;
    
    //订单状态
    //待付款
    public static final int order_status_pending_payment = 0;
    //已付款
    public static final int order_status_paid = 1;
    //退款中
    public static final int order_status_refund = 2;
    //订单关闭
    public static final int order_status_order_closed = 3;
    //订单完成
    public static final int order_status_order_fulfillment = 4;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 关联yjj_StoreBusiness表的id
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 关联shop_Member表的id
     */
	@TableField("member_id")
	private Long memberId;
    /**
     * 关联Order表的id
     */
	@TableField("order_id")
	private Long orderId;
    /**
     * 老的订单状态：0:待付款;1:已付款;2:退款中;3:订单关闭;4:订单完成
     */
	@TableField("old_status")
	private Integer oldStatus;
    /**
     * 新的订单状态：0:待付款;1:已付款;2:退款中;3:订单关闭;4:订单完成
     */
	@TableField("new_status")
	private Integer newStatus;
    /**
     * 记录创建时间
     */
	@TableField("create_time")
	private Long createTime;


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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Integer oldStatus) {
		this.oldStatus = oldStatus;
	}

	public Integer getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(Integer newStatus) {
		this.newStatus = newStatus;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
