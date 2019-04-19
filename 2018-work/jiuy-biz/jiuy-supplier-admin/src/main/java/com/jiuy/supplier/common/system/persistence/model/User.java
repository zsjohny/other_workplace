package com.jiuy.supplier.common.system.persistence.model;

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
 * 管理员表
 * </p>
 *
 * @author nijin
 * @since 2017-10-14
 */
@TableName("supplier_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 头像
     */
	private String avatar;
    /**
     * 账号
     */
	private String account;
    /**
     * 密码
     */
	private String password;
    /**
     * md5密码盐
     */
	private String salt;
    /**
     * 名字
     */
	private String name;
    /**
     * 生日
     */
	private Date birthday;
    /**
     * 性别（1：男 2：女）
     */
	private Integer sex;
    /**
     * 电子邮件
     */
	private String email;
    /**
     * 电话
     */
	private String phone;
    /**
     * 角色id
     */
	private String roleid;
    /**
     * 部门id
     */
	private Integer deptid;
    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
	private Integer status;
    /**
     * 创建时间
     */
	private Date createtime;
    /**
     * 保留字段
     */
	private Integer version;
    /**
     * 支付宝关联人姓名
     */
	@TableField("alipay_name")
	private String alipayName;
    /**
     * 商家名称
     */
	@TableField("business_name")
	private String businessName;
    /**
     * 公司名称
     */
	@TableField("company_name")
	private String companyName;
    /**
     * 商家地址
     */
	@TableField("business_address")
	private String businessAddress;
    /**
     * 营业执照号或者统一社会信用代码
     */
	@TableField("license_number")
	private String licenseNumber;
    /**
     * 税务登记号
     */
	private String taxid;
    /**
     * 法定代表人
     */
	@TableField("legal_person")
	private String legalPerson;
    /**
     * 身份证号码
     */
	@TableField("id_card_number")
	private String idCardNumber;
    /**
     * 所在省份
     */
	private String province;
    /**
     * 所在城市
     */
	private String city;
    /**
     * 银行卡选择标记 0 未选中， 1选中
     */
	@TableField("bank_card_flag")
	private Integer bankCardFlag;
    /**
     * 银行开户名称
     */
	@TableField("bank_account_name")
	private String bankAccountName;
    /**
     * 开户银行名称
     */
	@TableField("bank_name")
	private String bankName;
    /**
     * 银行开户号
     */
	@TableField("bank_account_no")
	private String bankAccountNo;
    /**
     * 支付宝选择标记 0 未选中， 1选中
     */
	@TableField("alipay_flag")
	private Integer alipayFlag;
    /**
     * 支付宝账号
     */
	@TableField("alipay_account")
	private String alipayAccount;
    /**
     * 品牌仓库Id
     */
	@TableField("lowarehouse_id")
	private Long lowarehouseId;
    /**
     * 关联品牌Id
     */
	@TableField("brand_id")
	private Long brandId;
    /**
     * 更新时间
     */
	private Long updatetime;
    /**
     * 分销门店数

     */
	@TableField("store_number")
	private Integer storeNumber;
    /**
     * 账户资金
     */
	@TableField("cash_income")
	private BigDecimal cashIncome;
    /**
     * 可提现余额
     */
	@TableField("available_balance")
	private BigDecimal availableBalance;
    /**
     * 组织机构号
     */
	@TableField("organization_no")
	private String organizationNo;
    /**
     * 品牌描述
     */
	@TableField("brand_description")
	private String brandDescription;
    /**
     * 品牌LOGO

     */
	@TableField("brand_logo")
	private String brandLogo;
    /**
     * 品牌展示图片
     */
	@TableField("brand_show_imgs")
	private String brandShowImgs;
    /**
     * 提成百分比
     */
	@TableField("commission_percentage")
	private BigDecimal commissionPercentage;
    /**
     * 结算日期
     */
	@TableField("settlement_date")
	private Integer settlementDate;
    /**
     * 微信选择标记 0 未选中， 1选中
     */
	@TableField("weixin_flag")
	private Integer weixinFlag;
    /**
     * 微信账号
     */
	@TableField("weixin_account")
	private String weixinAccount;
    /**
     * 微信关联姓名
     */
	@TableField("weixin_name")
	private String weixinName;
    /**
     * 提款设置 0 所有费用合并提款， 1不同款项分开取款 
     */
	@TableField("withdraw_setting")
	private Integer withdrawSetting;
    /**
     * 物流余额
     */
	@TableField("express_cash_income")
	private BigDecimal expressCashIncome;
    /**
     * 可提现物流余额
     */
	@TableField("available_express_cash")
	private BigDecimal availableExpressCash;
    /**
     * 最低体现额
     */
	@TableField("min_withdrawal")
	private BigDecimal minWithdrawal;
    /**
     * 保证金
     */
	private BigDecimal bond;
    /**
     * 商家号
     */
	@TableField("business_number")
	private Long businessNumber;
    /**
     * 物流银行开户名称
     */
	@TableField("express_bank_account_name")
	private String expressBankAccountName;
    /**
     * 物流开户银行名称
     */
	@TableField("express_bank_name")
	private String expressBankName;
    /**
     * 物流银行账户号
     */
	@TableField("express_bank_account_no")
	private String expressBankAccountNo;
    /**
     * 创建时间,长整型
     */
	@TableField("add_createtime")
	private Long addCreatetime;
    /**
     * 是否使用初始化密码,0：不是，1：是
     */
	@TableField("is_originalpassword")
	private Integer isOriginalpassword;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getAlipayName() {
		return alipayName;
	}

	public void setAlipayName(String alipayName) {
		this.alipayName = alipayName;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getTaxid() {
		return taxid;
	}

	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
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

	public Integer getBankCardFlag() {
		return bankCardFlag;
	}

	public void setBankCardFlag(Integer bankCardFlag) {
		this.bankCardFlag = bankCardFlag;
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

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public Integer getAlipayFlag() {
		return alipayFlag;
	}

	public void setAlipayFlag(Integer alipayFlag) {
		this.alipayFlag = alipayFlag;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public Long getLowarehouseId() {
		return lowarehouseId;
	}

	public void setLowarehouseId(Long lowarehouseId) {
		this.lowarehouseId = lowarehouseId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer storeNumber) {
		this.storeNumber = storeNumber;
	}

	public BigDecimal getCashIncome() {
		return cashIncome;
	}

	public void setCashIncome(BigDecimal cashIncome) {
		this.cashIncome = cashIncome;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getOrganizationNo() {
		return organizationNo;
	}

	public void setOrganizationNo(String organizationNo) {
		this.organizationNo = organizationNo;
	}

	public String getBrandDescription() {
		return brandDescription;
	}

	public void setBrandDescription(String brandDescription) {
		this.brandDescription = brandDescription;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}

	public String getBrandShowImgs() {
		return brandShowImgs;
	}

	public void setBrandShowImgs(String brandShowImgs) {
		this.brandShowImgs = brandShowImgs;
	}

	public BigDecimal getCommissionPercentage() {
		return commissionPercentage;
	}

	public void setCommissionPercentage(BigDecimal commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}

	public Integer getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Integer settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Integer getWeixinFlag() {
		return weixinFlag;
	}

	public void setWeixinFlag(Integer weixinFlag) {
		this.weixinFlag = weixinFlag;
	}

	public String getWeixinAccount() {
		return weixinAccount;
	}

	public void setWeixinAccount(String weixinAccount) {
		this.weixinAccount = weixinAccount;
	}

	public String getWeixinName() {
		return weixinName;
	}

	public void setWeixinName(String weixinName) {
		this.weixinName = weixinName;
	}

	public Integer getWithdrawSetting() {
		return withdrawSetting;
	}

	public void setWithdrawSetting(Integer withdrawSetting) {
		this.withdrawSetting = withdrawSetting;
	}

	public BigDecimal getExpressCashIncome() {
		return expressCashIncome;
	}

	public void setExpressCashIncome(BigDecimal expressCashIncome) {
		this.expressCashIncome = expressCashIncome;
	}

	public BigDecimal getAvailableExpressCash() {
		return availableExpressCash;
	}

	public void setAvailableExpressCash(BigDecimal availableExpressCash) {
		this.availableExpressCash = availableExpressCash;
	}

	public BigDecimal getMinWithdrawal() {
		return minWithdrawal;
	}

	public void setMinWithdrawal(BigDecimal minWithdrawal) {
		this.minWithdrawal = minWithdrawal;
	}

	public BigDecimal getBond() {
		return bond;
	}

	public void setBond(BigDecimal bond) {
		this.bond = bond;
	}

	public Long getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(Long businessNumber) {
		this.businessNumber = businessNumber;
	}

	public String getExpressBankAccountName() {
		return expressBankAccountName;
	}

	public void setExpressBankAccountName(String expressBankAccountName) {
		this.expressBankAccountName = expressBankAccountName;
	}

	public String getExpressBankName() {
		return expressBankName;
	}

	public void setExpressBankName(String expressBankName) {
		this.expressBankName = expressBankName;
	}

	public String getExpressBankAccountNo() {
		return expressBankAccountNo;
	}

	public void setExpressBankAccountNo(String expressBankAccountNo) {
		this.expressBankAccountNo = expressBankAccountNo;
	}

	public Long getAddCreatetime() {
		return addCreatetime;
	}

	public void setAddCreatetime(Long addCreatetime) {
		this.addCreatetime = addCreatetime;
	}

	public Integer getIsOriginalpassword() {
		return isOriginalpassword;
	}

	public void setIsOriginalpassword(Integer isOriginalpassword) {
		this.isOriginalpassword = isOriginalpassword;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
