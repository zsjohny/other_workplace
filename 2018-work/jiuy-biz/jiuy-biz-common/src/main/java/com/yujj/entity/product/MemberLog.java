package com.yujj.entity.product;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户会员日志表
 * </p>
 *
 * @author Charlie
 * @since 2018-08-16
 */
@TableName("yjj_member_log")
public class MemberLog extends Model<MemberLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员表id
     */
	@TableField("member_id")
	private Long memberId;
    /**
     * 操作来源(原因)
     */
	private String source;
    /**
     * 订单号
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 操作前删除状态
     */
	@TableField("before_del_state")
	private Integer beforeDelState;
    /**
     * 操作后删除状态
     */
	@TableField("after_del_state")
	private Integer afterDelState;
    /**
     * 操作前金额
     */
	@TableField("before_total_money")
	private Double beforeTotalMoney;
    /**
     * 操作后金额
     */
	@TableField("after_total_money")
	private Double afterTotalMoney;
    /**
     * 操作前有效期
     */
	@TableField("before_end_time")
	private Long beforeEndTime;
    /**
     * 操作后有效期
     */
	@TableField("after_end_time")
	private Long afterEndTime;
    /**
     * 操作前有效期记录
     */
	@TableField("before_time_queue")
	private String beforeTimeQueue;
    /**
     * 操作后有效期记录
     */
	@TableField("after_time_queue")
	private String afterTimeQueue;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 操作人
     */
	@TableField("create_user_id")
	private Long createUserId;
	/**
     * 操作人
     */
	@TableField("before_wx_closed_time")
	private Long beforeWxClosedTime;
	/**
     * 操作人
     */
	@TableField("after_wx_closed_time")
	private Long afterWxClosedTime;

	/**
	 * 获取一个默认的对象
	 *
	 * @param member member
	 * @return com.yujj.entity.product.MemberLog
	 * @author Charlie
	 * @date 2018/8/16 17:38
	 */
    public static MemberLog createLog(YjjMember member) {
		MemberLog log = new MemberLog ();
		log.setMemberId (member.getId ());
		log.setBeforeDelState (member.getDelState ());
		log.setBeforeEndTime (member.getEndTime ());
		log.setBeforeTimeQueue (member.getValidTimeQueue ());
		BigDecimal moneyTotal = member.getMoneyTotal ();
		log.setBeforeTotalMoney (moneyTotal == null ? 0D : moneyTotal.doubleValue ());
		return log;
	}

	/**
	 * 组装更新后的数据
	 *
	 * @param member member
	 * @param log log
	 * @return com.yujj.entity.product.MemberLog
	 * @author Charlie
	 * @date 2018/8/16 17:44
	 */
	public static MemberLog recAfterData(YjjMember member, MemberLog log) {
    	log.setAfterDelState (member.getDelState ());
    	log.setAfterEndTime (member.getEndTime ());
    	log.setAfterTimeQueue (member.getValidTimeQueue ());
		BigDecimal moneyTotal = member.getMoneyTotal ();
		log.setAfterTotalMoney (moneyTotal == null ? 0D : moneyTotal.doubleValue ());
    	return log;
	}

	public Long getBeforeWxClosedTime() {
		return beforeWxClosedTime;
	}

	public void setBeforeWxClosedTime(Long beforeWxClosedTime) {
		this.beforeWxClosedTime = beforeWxClosedTime;
	}

	public Long getAfterWxClosedTime() {
		return afterWxClosedTime;
	}

	public void setAfterWxClosedTime(Long afterWxClosedTime) {
		this.afterWxClosedTime = afterWxClosedTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getBeforeDelState() {
		return beforeDelState;
	}

	public void setBeforeDelState(Integer beforeDelState) {
		this.beforeDelState = beforeDelState;
	}

	public Integer getAfterDelState() {
		return afterDelState;
	}

	public void setAfterDelState(Integer afterDelState) {
		this.afterDelState = afterDelState;
	}

	public Double getBeforeTotalMoney() {
		return beforeTotalMoney;
	}

	public void setBeforeTotalMoney(Double beforeTotalMoney) {
		this.beforeTotalMoney = beforeTotalMoney;
	}

	public Double getAfterTotalMoney() {
		return afterTotalMoney;
	}

	public void setAfterTotalMoney(Double afterTotalMoney) {
		this.afterTotalMoney = afterTotalMoney;
	}

	public Long getBeforeEndTime() {
		return beforeEndTime;
	}

	public void setBeforeEndTime(Long beforeEndTime) {
		this.beforeEndTime = beforeEndTime;
	}

	public Long getAfterEndTime() {
		return afterEndTime;
	}

	public void setAfterEndTime(Long afterEndTime) {
		this.afterEndTime = afterEndTime;
	}

	public String getBeforeTimeQueue() {
		return beforeTimeQueue;
	}

	public void setBeforeTimeQueue(String beforeTimeQueue) {
		this.beforeTimeQueue = beforeTimeQueue;
	}

	public String getAfterTimeQueue() {
		return afterTimeQueue;
	}

	public void setAfterTimeQueue(String afterTimeQueue) {
		this.afterTimeQueue = afterTimeQueue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
