package com.jiuyuan.entity.newentity.ground;

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
 * 地推人员业务统计日报表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-15
 */
@TableName("ground_day_report")
public class GroundDayReport extends Model<GroundDayReport> {

    private static final long serialVersionUID = 1L;

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
     * 个人第一阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
     */
	@TableField("first_stage_individual_client_count")
	private Integer firstStageIndividualClientCount;
    /**
     * 个人第二阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
     */
	@TableField("second_stage_individual_client_count")
	private Integer secondStageIndividualClientCount;
    /**
     * 个人第三阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
     */
	@TableField("third_stage_individual_client_count")
	private Integer thirdStageIndividualClientCount;
    /**
     * 其他阶段个人当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
     */
	@TableField("other_stage_individual_client_count")
	private Integer otherStageIndividualClientCount;
    /**
     * 团队第一阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
     */
	@TableField("first_stage_team_client_count")
	private Integer firstStageTeamClientCount;
    /**
     * 团队第二阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
     */
	@TableField("second_stage_team_client_count")
	private Integer secondStageTeamClientCount;
    /**
     * 团队第三阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
     */
	@TableField("third_stage_team_client_count")
	private Integer thirdStageTeamClientCount;
    /**
     * 其他阶段团队当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
     */
	@TableField("other_stage_team_client_count")
	private Integer otherStageTeamClientCount;
	/**
     * 当日个人新增客户总数
     */
	@TableField("individual_client_count")
	private Integer individualClientCount;
	
	/**
     * 当日团队新增客户总数
     */
	@TableField("team_client_count")
	private Integer teamClientCount;
	
	/**
     * 当日个人注册奖金笔数
     */
	@TableField("day_oneself_reg_bonus_count")
	private Integer dayOneselfRegBonusCount;
	
	/**
     * 当日团队注册奖金笔数
     */
	@TableField("day_team_reg_bonus_count")
	private Integer dayTeamRegBonusCount;
  
    /**
     * 个人当日增加激活客户数
     */
	@TableField("active_individual_client_count")
	private Integer activeIndividualClientCount;
    /**
     * 团队当日增加激活数
     */
	@TableField("active_team_client_count")
	private Integer activeTeamClientCount;
    /**
     * 个人当日增加成交订单数
     */
	@TableField("trade_individual_client_count")
	private Integer tradeIndividualClientCount;
    /**
     * 团队当日增加成交订单数
     */
	@TableField("trade_team_client_count")
	private Integer tradeTeamClientCount;

	/**
     * 个人当日增加销售总额
     */
	@TableField("individual_total_sale_amount")
	private Double individualTotalSaleAmount;
    /**
     * 团队当日增加销售总额
     */
	@TableField("team_total_sale_amount")
	private Double teamTotalSaleAmount;
    /**
     * 日报日期，格式例：20170909
     */
	@TableField("report_date")
	private Integer reportDate;
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
	
	
	 
	
	
	 /**
     * 当日总收入：当日个人总收入+当日团队总收入
     */
	@TableField("day_total_cost")
	private Double dayTotalCost;
	/**
	 * 当日个人总收入：当日个人注册总收入+当日个人激活总收入+当日个人成交总收入
	 */
	@TableField("day_oneself_cost")
	private Double dayOneselfCost;
	/**
	 * 当日团队总收入：当日团队注册总收入+当日团队激活总收入+当日团队成交总收入
	 */
	@TableField("day_team_cost")
	private Double dayTeamCost;
	/**
	 * 当日注册总收入：当日个人注册总收入+当日团队注册总收入
	 */
	@TableField("day_reg_cost")
	private Double dayRegCost;
	/**
	 * 当日激活总收入：当日个人激活总收入+当日团队激活总收入
	 */
	@TableField("day_activate_cost")
	private Double dayActivateCost;
	/**
	 * 当日成交总收入：当日个人成交总收入+当日团队成交总收入
	 */
	@TableField("day_order_cost")
	private Double dayOrderCost;
	/**
	 * 当日个人注册总收入
	 */
	@TableField("day_oneself_reg_cost")
	private Double dayOneselfRegCost;
	/**
	 * 当日团队注册总收入
	 */
	@TableField("day_team_reg_cost")
	private Double dayTeamRegCost;
	/**
	 * 当日个人激活总收入
	 */
	@TableField("day_oneself_activate_cost")
	private Double dayOneselfActivateCost;
	/**
	 * 当日团队激活总收入
	 */
	@TableField("day_team_activate_cost")
	private Double dayTeamActivateCost;
	/**
	 * 当日个人成交总收入
	 */
	@TableField("day_oneself_order_cost")
	private Double dayOneselfOrderCost;
	/**
	 * 当日团队成交总收入
	 */
	@TableField("day_team_order_cost")
	private Double dayTeamOrderCost;

	
	
	
	
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

	public Integer getFirstStageIndividualClientCount() {
		return firstStageIndividualClientCount;
	}

	public void setFirstStageIndividualClientCount(Integer firstStageIndividualClientCount) {
		this.firstStageIndividualClientCount = firstStageIndividualClientCount;
	}

	public Integer getSecondStageIndividualClientCount() {
		return secondStageIndividualClientCount;
	}

	public void setSecondStageIndividualClientCount(Integer secondStageIndividualClientCount) {
		this.secondStageIndividualClientCount = secondStageIndividualClientCount;
	}

	public Integer getThirdStageIndividualClientCount() {
		return thirdStageIndividualClientCount;
	}

	public void setThirdStageIndividualClientCount(Integer thirdStageIndividualClientCount) {
		this.thirdStageIndividualClientCount = thirdStageIndividualClientCount;
	}

	public Integer getOtherStageIndividualClientCount() {
		return otherStageIndividualClientCount;
	}

	public void setOtherStageIndividualClientCount(Integer otherStageIndividualClientCount) {
		this.otherStageIndividualClientCount = otherStageIndividualClientCount;
	}

	public Integer getFirstStageTeamClientCount() {
		return firstStageTeamClientCount;
	}

	public void setFirstStageTeamClientCount(Integer firstStageTeamClientCount) {
		this.firstStageTeamClientCount = firstStageTeamClientCount;
	}

	public Integer getSecondStageTeamClientCount() {
		return secondStageTeamClientCount;
	}

	public void setSecondStageTeamClientCount(Integer secondStageTeamClientCount) {
		this.secondStageTeamClientCount = secondStageTeamClientCount;
	}

	public Integer getThirdStageTeamClientCount() {
		return thirdStageTeamClientCount;
	}

	public void setThirdStageTeamClientCount(Integer thirdStageTeamClientCount) {
		this.thirdStageTeamClientCount = thirdStageTeamClientCount;
	}

	public Integer getOtherStageTeamClientCount() {
		return otherStageTeamClientCount;
	}

	public void setOtherStageTeamClientCount(Integer otherStageTeamClientCount) {
		this.otherStageTeamClientCount = otherStageTeamClientCount;
	}

	public Integer getIndividualClientCount() {
		return individualClientCount;
	}

	public void setIndividualClientCount(Integer individualClientCount) {
		this.individualClientCount = individualClientCount;
	}

	public Integer getTeamClientCount() {
		return teamClientCount;
	}

	public void setTeamClientCount(Integer teamClientCount) {
		this.teamClientCount = teamClientCount;
	}

	public Integer getActiveIndividualClientCount() {
		return activeIndividualClientCount;
	}

	public void setActiveIndividualClientCount(Integer activeIndividualClientCount) {
		this.activeIndividualClientCount = activeIndividualClientCount;
	}

	public Integer getActiveTeamClientCount() {
		return activeTeamClientCount;
	}

	public void setActiveTeamClientCount(Integer activeTeamClientCount) {
		this.activeTeamClientCount = activeTeamClientCount;
	}

	public Integer getTradeIndividualClientCount() {
		return tradeIndividualClientCount;
	}

	public void setTradeIndividualClientCount(Integer tradeIndividualClientCount) {
		this.tradeIndividualClientCount = tradeIndividualClientCount;
	}

	public Integer getTradeTeamClientCount() {
		return tradeTeamClientCount;
	}

	public void setTradeTeamClientCount(Integer tradeTeamClientCount) {
		this.tradeTeamClientCount = tradeTeamClientCount;
	}

	

	public Double getIndividualTotalSaleAmount() {
		return individualTotalSaleAmount;
	}

	public void setIndividualTotalSaleAmount(Double individualTotalSaleAmount) {
		this.individualTotalSaleAmount = individualTotalSaleAmount;
	}

	public Double getTeamTotalSaleAmount() {
		return teamTotalSaleAmount;
	}

	public void setTeamTotalSaleAmount(Double teamTotalSaleAmount) {
		this.teamTotalSaleAmount = teamTotalSaleAmount;
	}

	public Integer getReportDate() {
		return reportDate;
	}

	public void setReportDate(Integer reportDate) {
		this.reportDate = reportDate;
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
		return this.Id;
	}

	public Double getDayTotalCost() {
		return dayTotalCost;
	}

	public void setDayTotalCost(Double dayTotalCost) {
		this.dayTotalCost = dayTotalCost;
	}

	public Double getDayOneselfCost() {
		return dayOneselfCost;
	}

	public void setDayOneselfCost(Double dayOneselfCost) {
		this.dayOneselfCost = dayOneselfCost;
	}

	public Double getDayTeamCost() {
		return dayTeamCost;
	}

	public void setDayTeamCost(Double dayTeamCost) {
		this.dayTeamCost = dayTeamCost;
	}

	public Double getDayRegCost() {
		return dayRegCost;
	}

	public void setDayRegCost(Double dayRegCost) {
		this.dayRegCost = dayRegCost;
	}

	public Double getDayActivateCost() {
		return dayActivateCost;
	}

	public void setDayActivateCost(Double dayActivateCost) {
		this.dayActivateCost = dayActivateCost;
	}

	public Double getDayOrderCost() {
		return dayOrderCost;
	}

	public void setDayOrderCost(Double dayOrderCost) {
		this.dayOrderCost = dayOrderCost;
	}

	public Double getDayOneselfRegCost() {
		return dayOneselfRegCost;
	}

	public void setDayOneselfRegCost(Double dayOneselfRegCost) {
		this.dayOneselfRegCost = dayOneselfRegCost;
	}

	public Double getDayTeamRegCost() {
		return dayTeamRegCost;
	}

	public void setDayTeamRegCost(Double dayTeamRegCost) {
		this.dayTeamRegCost = dayTeamRegCost;
	}

	public Double getDayOneselfActivateCost() {
		return dayOneselfActivateCost;
	}

	public void setDayOneselfActivateCost(Double dayOneselfActivateCost) {
		this.dayOneselfActivateCost = dayOneselfActivateCost;
	}

	public Double getDayTeamActivateCost() {
		return dayTeamActivateCost;
	}

	public void setDayTeamActivateCost(Double dayTeamActivateCost) {
		this.dayTeamActivateCost = dayTeamActivateCost;
	}

	public Double getDayOneselfOrderCost() {
		return dayOneselfOrderCost;
	}

	public void setDayOneselfOrderCost(Double dayOneselfOrderCost) {
		this.dayOneselfOrderCost = dayOneselfOrderCost;
	}

	public Double getDayTeamOrderCost() {
		return dayTeamOrderCost;
	}

	public void setDayTeamOrderCost(Double dayTeamOrderCost) {
		this.dayTeamOrderCost = dayTeamOrderCost;
	}

	public Integer getDayOneselfRegBonusCount() {
		return dayOneselfRegBonusCount;
	}

	public void setDayOneselfRegBonusCount(Integer dayOneselfRegBonusCount) {
		this.dayOneselfRegBonusCount = dayOneselfRegBonusCount;
	}

	public Integer getDayTeamRegBonusCount() {
		return dayTeamRegBonusCount;
	}

	public void setDayTeamRegBonusCount(Integer dayTeamRegBonusCount) {
		this.dayTeamRegBonusCount = dayTeamRegBonusCount;
	}

}
