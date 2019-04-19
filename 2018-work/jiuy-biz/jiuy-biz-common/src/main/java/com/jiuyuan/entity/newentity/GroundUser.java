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
 * 地推用户表
 * </p>
 *
 * @author nijin
 * @since 2017-11-14
 */
@TableName("ground_user")
public class GroundUser extends Model<GroundUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 状态(-1：删除 0：可用  1：禁用)
     */
	private Integer status;
    /**
     * 用户姓名
     */
	private String name;
    /**
     * 手机号码
     */
	private String phone;
    /**
     * 密码
     */
	private String password;
    /**
     * 盐值（用于用户数据加密）
     */
	private String salt;
    /**
     * 用户类型：1(大区总监)、2(省区经理)、3(区域主管)、4(城市经理)
     */
	@TableField("user_type")
	private Integer userType;
    /**
     * 上级id
     */
	private Long pid;
    /**
     * 上级用户类型：1(大区总监)、2(省区经理)、3(区域主管)、4(城市经理)
     */
	@TableField("puser_type")
	private Integer puserType;
    /**
     * 上级姓名
     */
	private String pname;
    /**
     * 上级手机号码
     */
	private String pphone;
    /**
     * 所有上级ids,例如(,1,3,5,6,)首尾都有逗号
     */
	@TableField("super_ids")
	private String superIds;
    /**
     * 所在省份
     */
	private String province;
    /**
     * 所在城市
     */
	private String city;
    /**
     * 所在区/县
     */
	private String district;
    /**
     * 银行卡号
     */
	@TableField("bank_account_no")
	private String bankAccountNo;
    /**
     * 银行户名
     */
	@TableField("bank_account_name")
	private String bankAccountName;
    /**
     * 开户银行
     */
	@TableField("bank_name")
	private String bankName;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
	
	
    /**
     * 个人客户数
     */
	@TableField("individual_client_count")
	private Integer individualClientCount;
    /**
     * 个人第一阶段客户数
     */
	@TableField("first_stage_individual_client_count")
	private Integer firstStageIndividualClientCount;
    /**
     * 个人第二阶段客户数
     */
	@TableField("second_stage_individual_client_count")
	private Integer secondStageIndividualClientCount;
    /**
     * 个人第三阶段客户数
     */
	@TableField("third_stage_individual_client_count")
	private Integer thirdStageIndividualClientCount;
    /**
     * 个人其他阶段客户数
     */
	@TableField("other_stage_individual_client_count")
	private Integer otherStageIndividualClientCount;
    /**
     * 团队客户数
     */
	@TableField("team_client_count")
	private Integer teamClientCount;
    /**
     * 团队第一阶段客户数
     */
	@TableField("first_stage_team_client_count")
	private Integer firstStageTeamClientCount;
    /**
     * 团队第二阶段客户数
     */
	@TableField("second_stage_team_client_count")
	private Integer secondStageTeamClientCount;
    /**
     * 团队第三阶段客户数
     */
	@TableField("third_stage_team_client_count")
	private Integer thirdStageTeamClientCount;
    /**
     * 团队其他阶段客户数
     */
	@TableField("other_stage_team_client_count")
	private Integer otherStageTeamClientCount;
    /**
     * 个人客户激活率
     */
	@TableField("individual_client_active_rate")
	private Double individualClientActiveRate;
    /**
     * 团队客户激活率
     */
	@TableField("team_client_active_rate")
	private Double teamClientActiveRate;
	
	/**
     * 个人销售总额
     */
	@TableField("individual_total_sale_amount")
	private Double individualTotalSaleAmount;
    /**
     * 团队销售总额
     */
	@TableField("team_total_sale_amount")
	private Double teamTotalSaleAmount;
	
	
//    /**
//     * 个人收入
//     */
//	@TableField("individual_income")
//	private Double individualIncome;
//    /**
//     * 团队收入
//     */
//	@TableField("team_income")
//	private Double teamIncome;
	/**
     * 可提现余额
     */
	@TableField("available_balance")
	private Double availableBalance;
	
//	 /**
//     * 最低提现额
//     */
//	@TableField("min_withdrawal")
//	private Double minWithdrawal;
	
    /**
     * 是否使用初始化密码,0：不是，1：是
     */
	@TableField("is_originalpassword")
	private Integer isOriginalpassword;
    
	/**
     * 个人激活客户数
     */
	@TableField("oneself_activate_client_count")
	private Double oneselfActivateClientCount;

	/**
     * 个人激活客户数
     */
	@TableField("team_activate_client_count")
	private Double teamActivateClientCount;
    
	
    /**
     * 总收入：总个人收入+总团队收入
     */
	@TableField("total_cost")
	private Double totalCost;
    /**
     * 总个人收入：个人注册总收入+个人激活总收入+个人成交总收入
     */
	@TableField("oneself_cost")
	private Double oneselfCost;
    /**
     * 总团队收入：团队注册总收入+团队激活总收入+团队成交总收入
     */
	@TableField("team_cost")
	private Double teamCost;
    /**
     * 总注册收入：个人注册总收入+团队注册总收入
     */
	@TableField("reg_cost")
	private Double regCost;
    /**
     * 总激活收入：个人激活总收入+团队激活总收入
     */
	@TableField("activate_cost")
	private Double activateCost;
    /**
     * 总成交收入：个人成交总收入+团队成交总收入
     */
	@TableField("order_cost")
	private Double orderCost;

    /**
     * 个人注册总收入
     */
	@TableField("oneself_reg_cost")
	private Double oneselfRegCost;
    /**
     * 团队注册总收入
     */
	@TableField("team_reg_cost")
	private Double teamRegCost;
    /**
     * 个人激活总收入
     */
	@TableField("oneself_activate_cost")
	private Double oneselfActivateCost;
	
    /**
     * 团队激活总收入
     */
	@TableField("team_activate_cost")
	private Double teamActivateCost ;
    /**
     * 个人成交总收入
     */
	@TableField("oneself_order_cost")
	private Double oneselfOrderCost ;
    /**
     * 团队成交总收入
     */
	@TableField("team_order_cost")
	private Double teamOrderCost;
	
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Integer getPuserType() {
		return puserType;
	}

	public void setPuserType(Integer puserType) {
		this.puserType = puserType;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPphone() {
		return pphone;
	}

	public void setPphone(String pphone) {
		this.pphone = pphone;
	}

	public String getSuperIds() {
		return superIds;
	}

	public void setSuperIds(String superIds) {
		this.superIds = superIds;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getIndividualClientCount() {
		return individualClientCount;
	}

	public void setIndividualClientCount(Integer individualClientCount) {
		this.individualClientCount = individualClientCount;
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

	public Integer getTeamClientCount() {
		return teamClientCount;
	}

	public void setTeamClientCount(Integer teamClientCount) {
		this.teamClientCount = teamClientCount;
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

	public Double getIndividualClientActiveRate() {
		return individualClientActiveRate;
	}

	public void setIndividualClientActiveRate(Double individualClientActiveRate) {
		this.individualClientActiveRate = individualClientActiveRate;
	}

	public Double getTeamClientActiveRate() {
		return teamClientActiveRate;
	}

	public void setTeamClientActiveRate(Double teamClientActiveRate) {
		this.teamClientActiveRate = teamClientActiveRate;
	}

	

	public Integer getIsOriginalpassword() {
		return isOriginalpassword;
	}

	public void setIsOriginalpassword(Integer isOriginalpassword) {
		this.isOriginalpassword = isOriginalpassword;
	}

	public Double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(Double availableBalance) {
		this.availableBalance = availableBalance;
	}

//	public Double getMinWithdrawal() {
//		return minWithdrawal;
//	}
//
//	public void setMinWithdrawal(Double minWithdrawal) {
//		this.minWithdrawal = minWithdrawal;
//	}

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Double getOneselfCost() {
		return oneselfCost;
	}

	public void setOneselfCost(Double oneselfCost) {
		this.oneselfCost = oneselfCost;
	}

	public Double getTeamCost() {
		return teamCost;
	}

	public void setTeamCost(Double teamCost) {
		this.teamCost = teamCost;
	}

	public Double getRegCost() {
		return regCost;
	}

	public void setRegCost(Double regCost) {
		this.regCost = regCost;
	}

	public Double getActivateCost() {
		return activateCost;
	}

	public void setActivateCost(Double activateCost) {
		this.activateCost = activateCost;
	}

	public Double getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(Double orderCost) {
		this.orderCost = orderCost;
	}

	public Double getOneselfRegCost() {
		return oneselfRegCost;
	}

	public void setOneselfRegCost(Double oneselfRegCost) {
		this.oneselfRegCost = oneselfRegCost;
	}

	public Double getTeamRegCost() {
		return teamRegCost;
	}

	public void setTeamRegCost(Double teamRegCost) {
		this.teamRegCost = teamRegCost;
	}

	public Double getOneselfActivateCost() {
		return oneselfActivateCost;
	}

	public void setOneselfActivateCost(Double oneselfActivateCost) {
		this.oneselfActivateCost = oneselfActivateCost;
	}

	public Double getTeamActivateCost() {
		return teamActivateCost;
	}

	public void setTeamActivateCost(Double teamActivateCost) {
		this.teamActivateCost = teamActivateCost;
	}

	public Double getOneselfOrderCost() {
		return oneselfOrderCost;
	}

	public void setOneselfOrderCost(Double oneselfOrderCost) {
		this.oneselfOrderCost = oneselfOrderCost;
	}

	public Double getTeamOrderCost() {
		return teamOrderCost;
	}

	public void setTeamOrderCost(Double teamOrderCost) {
		this.teamOrderCost = teamOrderCost;
	}

	public Double getOneselfActivateClientCount() {
		return oneselfActivateClientCount;
	}

	public void setOneselfActivateClientCount(Double oneselfActivateClientCount) {
		this.oneselfActivateClientCount = oneselfActivateClientCount;
	}

	public Double getTeamActivateClientCount() {
		return teamActivateClientCount;
	}

	public void setTeamActivateClientCount(Double teamActivateClientCount) {
		this.teamActivateClientCount = teamActivateClientCount;
	}

	@Override
	public String toString() {
		return "GroundUser [id=" + id + ", status=" + status + ", name=" + name + ", phone=" + phone + ", password="
				+ password + ", salt=" + salt + ", userType=" + userType + ", pid=" + pid + ", puserType=" + puserType
				+ ", pname=" + pname + ", pphone=" + pphone + ", superIds=" + superIds + ", province=" + province
				+ ", city=" + city + ", district=" + district + ", bankAccountNo=" + bankAccountNo
				+ ", bankAccountName=" + bankAccountName + ", bankName=" + bankName + ", createTime=" + createTime
				+ ", individualClientCount=" + individualClientCount + ", firstStageIndividualClientCount="
				+ firstStageIndividualClientCount + ", secondStageIndividualClientCount="
				+ secondStageIndividualClientCount + ", thirdStageIndividualClientCount="
				+ thirdStageIndividualClientCount + ", otherStageIndividualClientCount="
				+ otherStageIndividualClientCount + ", teamClientCount=" + teamClientCount
				+ ", firstStageTeamClientCount=" + firstStageTeamClientCount + ", secondStageTeamClientCount="
				+ secondStageTeamClientCount + ", thirdStageTeamClientCount=" + thirdStageTeamClientCount
				+ ", otherStageTeamClientCount=" + otherStageTeamClientCount + ", individualClientActiveRate="
				+ individualClientActiveRate + ", teamClientActiveRate=" + teamClientActiveRate
				+ ", isOriginalpassword=" + isOriginalpassword + ", availableBalance=" + availableBalance
				+ ", individualTotalSaleAmount=" + individualTotalSaleAmount
				+ ", teamTotalSaleAmount=" + teamTotalSaleAmount + ", totalCost=" + totalCost + ", oneselfCost="
				+ oneselfCost + ", teamCost=" + teamCost + ", regCost=" + regCost + ", activateCost=" + activateCost
				+ ", orderCost=" + orderCost + ", oneselfRegCost=" + oneselfRegCost + ", teamRegCost=" + teamRegCost
				+ ", oneselfActivateCost=" + oneselfActivateCost + ", teamActivateCost=" + teamActivateCost
				+ ", oneselfOrderCost=" + oneselfOrderCost + ", teamOrderCost=" + teamOrderCost
				+ ", oneselfActivateClientCount=" + oneselfActivateClientCount + ", teamActivateClientCount="
				+ teamActivateClientCount + "]";
	}

	
}
