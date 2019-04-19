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
 * 地推奖金规则表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-15
 */
@TableName("ground_bonus_rule")
public class GroundBonusRule extends Model<GroundBonusRule> {

    private static final long serialVersionUID = 1L;
    
//  状态(-1：删除 0：可用  1：禁用)
	public static final int ACTIVE_STATUS_TRUE = 0;
	public static final int ACTIVE_STATUS_DELETE = -1;
	public static final int ACTIVE_STATUS_FALSE = 1;
	
////  奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金），4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金）
//	public static final int REGISTER_STAGE = 1;
//	public static final int ACTIVATION_STAGE = 2;
//	public static final int FIRST_DEAL_STAGE = 3;
//	public static final int SECOND_DEAL_STAGE = 4;
//	public static final int THIRD_DEAL_STAGE = 5;
//	 奖金类型 0:个人 1:管理
//	public static final int BONUS_TYPE_PERSONAL = 0;
//	public static final int BONUS_TYPE_GROUP = 1;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户类型：1(大区总监)、2(省区经理)、3(区域主管)、4(城市经理)
     */
	@TableField("user_type")
	private Integer userType;
    /**
     * 奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金），4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金）
     */
	@TableField("bonus_type")
	private Integer bonusType;
    /**
     * 奖金来源类型：0个人、1团队
     */
	@TableField("source_type")
	private Integer sourceType;
    /**
     * 阶段1 2 3 为:提成百分比, 注册激活为:奖金金额
     */
	private Double bonus;
    /**
     * 状态(-1：删除 0：可用  1：禁用)
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getBonusType() {
		return bonusType;
	}

	public void setBonusType(Integer bonusType) {
		this.bonusType = bonusType;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
