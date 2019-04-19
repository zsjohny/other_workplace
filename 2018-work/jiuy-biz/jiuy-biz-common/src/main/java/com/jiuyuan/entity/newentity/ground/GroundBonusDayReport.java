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
 * 奖金贡献日报表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-07
 */
@TableName("ground_bonus_day_report")
public class GroundBonusDayReport extends Model<GroundBonusDayReport> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 日报日期，格式例：20170909
     */
	@TableField("report_date")
	private Integer reportDate;
    /**
     * 被贡献人Id
     */
	@TableField("ground_user_id")
	private Long groundUserId;
    /**
     * 被贡献人Id用户姓名
     */
	@TableField("ground_user_name")
	private String groundUserName;
    /**
     * 被贡献人Id用户类型：1(大区总监)、2(省区经理)、3(区域主管)、4(城市经理)
     */
	@TableField("ground_user_type")
	private Integer groundUserType;
    /**
     * 被贡献人该奖金相关直接下级ID
     */
	@TableField("direct_ground_user_id")
	private Long directGroundUserId;
    /**
     * 被贡献人该奖金相关直接下级用户姓名
     */
	@TableField("direct_ground_user_name")
	private String directGroundUserName;
    /**
     * 被贡献人该奖金相关直接下级用户类型：1(大区总监)、2(省区经理)、3(区域主管)、4(城市经理)
     */
	@TableField("direct_ground_user_type")
	private Integer directGroundUserType;
    /**
     * 奖金来源类型：0个人、1团队
     */
	@TableField("source_type")
	private Integer sourceType;
    /**
     * 当日贡献奖金总金额（奖金来源类型为个人时为个人的，团队时为直接下级的个人加团队的）
     */
	@TableField("day_total_cost")
	private BigDecimal dayTotalCost;
    /**
     * 当日贡献注册奖金相关注册客户数（奖金来源类型为个人时为个人的，团队时为直接下级的个人加团队的）
     */
	@TableField("day_reg_count")
	private Integer dayRegCount;
    /**
     * 当日贡献注册奖金相关激活客户数（奖金来源类型为个人时为个人的，团队时为直接下级的个人加团队的）
     */
	@TableField("day_activate_count")
	private Integer dayActivateCount;
    /**
     * 当日贡献成交奖金相关成交订单数（奖金来源类型为个人时为个人的，团队时为直接下级的个人加团队的）
     */
	@TableField("day_order_count")
	private Integer dayOrderCount;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Date updateTime;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Integer getReportDate() {
		return reportDate;
	}

	public void setReportDate(Integer reportDate) {
		this.reportDate = reportDate;
	}

	public Long getGroundUserId() {
		return groundUserId;
	}

	public void setGroundUserId(Long groundUserId) {
		this.groundUserId = groundUserId;
	}

	public String getGroundUserName() {
		return groundUserName;
	}

	public void setGroundUserName(String groundUserName) {
		this.groundUserName = groundUserName;
	}

	public Integer getGroundUserType() {
		return groundUserType;
	}

	public void setGroundUserType(Integer groundUserType) {
		this.groundUserType = groundUserType;
	}

	public Long getDirectGroundUserId() {
		return directGroundUserId;
	}

	public void setDirectGroundUserId(Long directGroundUserId) {
		this.directGroundUserId = directGroundUserId;
	}

	public String getDirectGroundUserName() {
		return directGroundUserName;
	}

	public void setDirectGroundUserName(String directGroundUserName) {
		this.directGroundUserName = directGroundUserName;
	}

	public Integer getDirectGroundUserType() {
		return directGroundUserType;
	}

	public void setDirectGroundUserType(Integer directGroundUserType) {
		this.directGroundUserType = directGroundUserType;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public BigDecimal getDayTotalCost() {
		return dayTotalCost;
	}

	public void setDayTotalCost(BigDecimal dayTotalCost) {
		this.dayTotalCost = dayTotalCost;
	}

	public Integer getDayRegCount() {
		return dayRegCount;
	}

	public void setDayRegCount(Integer dayRegCount) {
		this.dayRegCount = dayRegCount;
	}

	public Integer getDayActivateCount() {
		return dayActivateCount;
	}

	public void setDayActivateCount(Integer dayActivateCount) {
		this.dayActivateCount = dayActivateCount;
	}

	public Integer getDayOrderCount() {
		return dayOrderCount;
	}

	public void setDayOrderCount(Integer dayOrderCount) {
		this.dayOrderCount = dayOrderCount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
