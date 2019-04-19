package com.jiuyuan.entity.newentity.ground;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 地推用户奖金发放表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-15
 */
@TableName("ground_bonus_grant")
public class GroundBonusGrant extends Model<GroundBonusGrant> {

    private static final long serialVersionUID = 1L;
    
    //奖金来源类型：0个人、1团体
//    public static final int SOURCE_TYPE_INDIVIDUAL = 0;//0个人
//    public static final int SOURCE_TYPE_TEAM = 1;//1团体
    
    //奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金）、4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金）
    public static final int BONUS_TYPE_REGISTER = 1;//1(门店注册奖金)
    public static final int BONUS_TYPE_ACTIVE = 2;//2（门店激活）
    public static final int BONUS_TYPE_FIRST_STAGE = 3;//3（第一阶段门店交易奖金）
    public static final int BONUS_TYPE_SECOND_STAGE = 4;//4（第二阶段门店交易奖金）
    public static final int BONUS_TYPE_THIRD_STAGE = 5;//5（第三阶段门店交易奖金）
    
    //结算状态：0未结算、1已结算
    public static final int SETTLEMENT_STATE_NO = 0;//0未结算
    public static final int SETTLEMENT_STATE_YES = 1;//1已结算
    
    

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 所有上级ids,例如(,1,3,5,6,)
     */
	@TableField("super_ids")
	private String superIds;
	 /**
     * 地推用户id
     */
	@TableField("ground_user_id")
	private Long groundUserId;
	 /**
     * 被贡献人该奖金相关直接下级ID
     */
	@TableField("direct_ground_user_id")
	private Long directGroundUserId;
	
    /**
     * 奖金贡献来源地推用户id,可为自身
     */
	@TableField("source_ground_user_id")
	private Long sourceGroundUserId;
	/**
     * 奖金规则:阶段1 2 3 为:提成百分比, 注册激活为:奖金金额
     */
	@TableField("bonus_rule")
	private Double bonusRule;
	
    /**
     * 奖金来源类型：0个人、1团体
     */
	@TableField("source_type")
	private Integer sourceType;
    /**
     * 奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金），4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金）
     */
	@TableField("bonus_type")
	private Integer bonusType;
    /**
     * 相关id，门店注册奖金时为门店Id、其他类型时为订单号
     */
	@TableField("related_id")
	private Long relatedId;
    /**
     * 相关门店用户id
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 相关订单金额（订单商品总金额+邮费，用来统计地推人员销售额）
     */
	@TableField("order_price")
	private Double orderPrice;
    /**
     * 备注
     */
	private String remark;
    /**
     * 奖金金额
     */
	private Double cash;
    /**
     * 奖金入账时间,时间戳
     */
	@TableField("into_time")
	private Long intoTime;
    /**
     * 允许提现时间,时间戳
     */
	@TableField("allow_get_out_time")
	private Long allowGetOutTime;
	/**
     * 奖金入账日期，格式例：20170909
     */
	@TableField("into_date")
	private Integer intoDate;
	
	/**
     * 允许提现日期，格式例：20170909
     */
	@TableField("allow_get_out_date")
	private Integer allowGetOutDate;
	
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
	
    /**
     * 结算状态：0未结算、1已结算
     */
	@TableField("settlement_state")
	private Integer settlementState;

	
	
	
	public Double getBonusRule() {
		return bonusRule;
	}

	public void setBonusRule(Double bonusRule) {
		this.bonusRule = bonusRule;
	}

	public Long getDirectGroundUserId() {
		return directGroundUserId;
	}

	public void setDirectGroundUserId(Long directGroundUserId) {
		this.directGroundUserId = directGroundUserId;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getSuperIds() {
		return superIds;
	}

	public void setSuperIds(String superIds) {
		this.superIds = superIds;
	}

	public Long getGroundUserId() {
		return groundUserId;
	}

	public void setGroundUserId(Long groundUserId) {
		this.groundUserId = groundUserId;
	}

	public Long getSourceGroundUserId() {
		return sourceGroundUserId;
	}

	public void setSourceGroundUserId(Long sourceGroundUserId) {
		this.sourceGroundUserId = sourceGroundUserId;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getBonusType() {
		return bonusType;
	}

	public void setBonusType(Integer bonusType) {
		this.bonusType = bonusType;
	}

	public Long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		this.cash = cash;
	}

	public Long getIntoTime() {
		return intoTime;
	}

	public void setIntoTime(Long intoTime) {
		this.intoTime = intoTime;
	}

	public Long getAllowGetOutTime() {
		return allowGetOutTime;
	}

	public void setAllowGetOutTime(Long allowGetOutTime) {
		this.allowGetOutTime = allowGetOutTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

	public Integer getSettlementState() {
		return settlementState;
	}

	public void setSettlementState(Integer settlementState) {
		this.settlementState = settlementState;
	}

	public Integer getIntoDate() {
		return intoDate;
	}

	public void setIntoDate(Integer intoDate) {
		this.intoDate = intoDate;
	}

	public Integer getAllowGetOutDate() {
		return allowGetOutDate;
	}

	public void setAllowGetOutDate(Integer allowGetOutDate) {
		this.allowGetOutDate = allowGetOutDate;
	}

}