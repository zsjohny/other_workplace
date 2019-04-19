package com.jiuyuan.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * @author jeff.zhan
 * @version 2016年12月6日 下午2:27:21
 * 
 */

public class BrandBusiness implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1583857485858815381L;

	private long id;
	
	private long businessNumber;	//商家号
	
	private String businessName;	//商家名称
	
	private String companyName;		//公司名称
	
	private int status;				//账户状态  0正常	1禁用
	
	private long createTime;
	
	private long updateTime;
	
	private int storeNumber;		//门店数
	
	private String phoneNumber;		//手机号码
	
	private double cashIncome;		//账户资金
	
	private double availableBalance;	//可提现余额
	
	private long brandId;			//关联品牌
	
	private String businessAddress;	//商家地址
	
	private String licenseNumber;	//营业执照号或者统一社会信用代码
	
	private String organizationNo;	//组织机构号
	
	private String taxId;			//税务登记号
	
	private String legalPerson;		//法定代表人
	
	private String province;		//所在省份
	
	private String city;			//所在城市
	
	private String brandDescription;//品牌描述
	
	private String brandLogo;		//品牌logo
	
	private String brandShowImgs;	//品牌展示图片
	
	private double commissionPercentage;	//提成百分比
	
	private int settlementDate;		//结算日期
	
	private String bankAccountName;	//银行开户名称

	private String bankName;		//开户银行名称			
	
	private String bankAccountNo;	//银行账户号
	
	private String userName;		//用户名
	
	private String userPassword;	//用户密码
	
	private String code;	//验证码
	
	private int bankCardFlag;
	
	private int alipayFlag;
	
	private int weixinFlag;
	
	private String alipayAccount;
	
	private String alipayName;
	
	private String weixinAccount;
	
	private String weixinName;
	
	private String idCardNumber;

    // added by Dongzhong 2016-11-24
    private long lOWarehouseId; // 仓库id
    
    private long activeTime; // 激活时间
    
    private int withdrawSetting;
    
    private String expressBankAccountName;
    
    private String expressBankName;
    
    private String expressBankAccountNo;
    
    private double expressCashIncome;
    
    private double availableExpressCash;
    
    private double minWithdrawal;  //最低提现金额
    
    private String avatar;//头像
     
    
    public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public long getAddCreatetime() {
		return addCreatetime;
	}

	public void setAddCreatetime(long addCreatetime) {
		this.addCreatetime = addCreatetime;
	}

	public int getIsOriginalpassword() {
		return isOriginalpassword;
	}

	public void setIsOriginalpassword(int isOriginalpassword) {
		this.isOriginalpassword = isOriginalpassword;
	}

	private double bond;  //保证金
    
    private long addCreatetime;//创建时间,长整型
    
    private int isOriginalpassword;//是否使用初始化密码,0：不是，1：是
    
	public double getMinWithdrawal() {
		return minWithdrawal;
	}

	public void setMinWithdrawal(double minWithdrawal) {
		this.minWithdrawal = minWithdrawal;
	}

	public double getBond() {
		return bond;
	}

	public void setBond(double bond) {
		this.bond = bond;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(long businessNumber) {
		this.businessNumber = businessNumber;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(int storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public double getCashIncome() {
		return cashIncome;
	}

	public void setCashIncome(double cashIncome) {
		this.cashIncome = cashIncome;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
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

	public String getOrganizationNo() {
		return organizationNo;
	}

	public void setOrganizationNo(String organizationNo) {
		this.organizationNo = organizationNo;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
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

	public double getCommissionPercentage() {
		return commissionPercentage;
	}

	public void setCommissionPercentage(double commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}

	public int getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(int settlementDate) {
		this.settlementDate = settlementDate;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public int getBankCardFlag() {
		return bankCardFlag;
	}

	public void setBankCardFlag(int bankCardFlag) {
		this.bankCardFlag = bankCardFlag;
	}

	public int getAlipayFlag() {
		return alipayFlag;
	}

	public void setAlipayFlag(int alipayFlag) {
		this.alipayFlag = alipayFlag;
	}

	public int getWeixinFlag() {
		return weixinFlag;
	}

	public void setWeixinFlag(int weixinFlag) {
		this.weixinFlag = weixinFlag;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getAlipayName() {
		return alipayName;
	}

	public void setAlipayName(String alipayName) {
		this.alipayName = alipayName;
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

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String[] getbrandShowImgArray() {
		if (StringUtils.equals(null, brandShowImgs) || StringUtils.equals("", brandShowImgs)) {
			return new String[0];
		}
		JSONArray retArray = JSON.parseArray(getBrandShowImgs());
		return retArray.toArray(new String[] {});
	}

	public long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

	public long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

	public int getWithdrawSetting() {
		return withdrawSetting;
	}

	public void setWithdrawSetting(int withdrawSetting) {
		this.withdrawSetting = withdrawSetting;
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

	public double getExpressCashIncome() {
		return expressCashIncome;
	}

	public void setExpressCashIncome(double expressCashIncome) {
		this.expressCashIncome = expressCashIncome;
	}

	public double getAvailableExpressCash() {
		return availableExpressCash;
	}

	public void setAvailableExpressCash(double availableExpressCash) {
		this.availableExpressCash = availableExpressCash;
	}
	
	
}
