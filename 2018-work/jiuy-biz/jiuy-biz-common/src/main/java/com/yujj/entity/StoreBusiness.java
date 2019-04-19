package com.yujj.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午4:01:32
 * 
 */
public class StoreBusiness implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8574631764403116659L;

	private long id;

	private long businessNumber;	//商家号
	
	private String businessName;	//商家名称
	
	private String companyName;		//公司名称
	
	private int status;				//账户状态  0正常	1禁用
	
	private long createTime;
	
	private long updateTime;
	
	private int memberNumber;		//会员数
	
	private int distributionStatus;	//分销状态
	
	private String phoneNumber;		//手机号码
	
	private double cashIncome;		//账户资金
	
	private int withdrawApply;		//提现申请次数
	
	private int deep;		//门店层级
	
	private String businessAddress;	//商家地址
	
	private String licenseNumber;	//营业执照号或者统一社会信用代码
	
	private String organizationNo;	//组织机构号
	
	private String taxId;			//税务登记号
	
	private String legalPerson;		//法定代表人
	
	private String province;		//所在省份
	
	private String city;			//所在城市

	
	private String storeArea;		//门店建筑面积
	
	private String storeDescription;//门店描述
	
	private String storeLogo;		//门店logo
	
	private String storeShowImgs;	//门店展示图片
	
	private String storePanoramaImg;	//门店全景展示图片
	
	private double commissionPercentage;	//提成百分比
	
	private double memberCommissionPercentage;	//会员购物提成百分比
	
	private String bankAccountName;	//银行开户名称

	private String bankName;		//开户银行名称			
	
	private String bankAccountNo;	//银行账户号
	
	private String userPassword;	//用户密码
	
	private double availableBalance;	//可提现余额
	
	private String userCID;	//用户密码
	
	private String superBusinessIds;	//用户密码
	

	private String userName;	//用户名
	
	
	private int bankCardFlag;
	
	private int alipayFlag;
	
	private String alipayAccount;
	
	private String alipayName;
	
	private int weixinFlag;
	
	private String weixinAccount;
	
	private String weixinName;
	
	private String idCardNumber;
	
	private int businessType;
	
	private String withdrawPassword;
	
	private String grade;
	
	private String businessHours;
	
	private String signTime;
	
	private int bankCardUseFlag;
	
	private int alipayUseFlag;
	
	private int weixinUseFlag;
	
	private long lastErrorWithdrawPasswordTime;

	private int errorCount;
	
	private double defaultCommissionPercentage;//默认下级商家会员购物提成百分比
	
	
	private long protocolTime;

	private long activeTime;
	
	
	private String bindWeixinId;  //微信登录ID

	private String bindWeixinName;  //微信登录名称
	
	private String bindWeixinIcon;   //微信登录头像URL

	public long getProtocolTime() {
		return protocolTime;
	}

	public void setProtocolTime(long protocolTime) {
		this.protocolTime = protocolTime;
	}

	public long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public int getWeixinFlag() {
		return weixinFlag;
	}

	public void setWeixinFlag(int weixinFlag) {
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

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public int getBusinessType() {
		return businessType;
	}

	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}

	public String getWithdrawPassword() {
		return withdrawPassword;
	}

	public void setWithdrawPassword(String withdrawPassword) {
		this.withdrawPassword = withdrawPassword;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getBusinessHours() {
		return businessHours;
	}

	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public int getBankCardUseFlag() {
		return bankCardUseFlag;
	}

	public void setBankCardUseFlag(int bankCardUseFlag) {
		this.bankCardUseFlag = bankCardUseFlag;
	}

	public int getAlipayUseFlag() {
		return alipayUseFlag;
	}

	public void setAlipayUseFlag(int alipayUseFlag) {
		this.alipayUseFlag = alipayUseFlag;
	}

	public int getWeixinUseFlag() {
		return weixinUseFlag;
	}

	public void setWeixinUseFlag(int weixinUseFlag) {
		this.weixinUseFlag = weixinUseFlag;
	}

	public long getLastErrorWithdrawPasswordTime() {
		return lastErrorWithdrawPasswordTime;
	}

	public void setLastErrorWithdrawPasswordTime(long lastErrorWithdrawPasswordTime) {
		this.lastErrorWithdrawPasswordTime = lastErrorWithdrawPasswordTime;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public double getDefaultCommissionPercentage() {
		return defaultCommissionPercentage;
	}

	public void setDefaultCommissionPercentage(double defaultCommissionPercentage) {
		this.defaultCommissionPercentage = defaultCommissionPercentage;
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

	public int getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(int memberNumber) {
		this.memberNumber = memberNumber;
	}

	public int getDistributionStatus() {
		return distributionStatus;
	}

	public void setDistributionStatus(int distributionStatus) {
		this.distributionStatus = distributionStatus;
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

	public int getWithdrawApply() {
		return withdrawApply;
	}

	public void setWithdrawApply(int withdrawApply) {
		this.withdrawApply = withdrawApply;
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

	public String getStoreArea() {
		return storeArea;
	}

	public void setStoreArea(String storeArea) {
		this.storeArea = storeArea;
	}

	public String getStoreDescription() {
		return storeDescription;
	}

	public void setStoreDescription(String storeDescription) {
		this.storeDescription = storeDescription;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getStoreShowImgs() {
		return storeShowImgs;
	}

	public void setStoreShowImgs(String storeShowImgs) {
		this.storeShowImgs = storeShowImgs;
	}

	public String getStorePanoramaImg() {
		return storePanoramaImg;
	}

	public void setStorePanoramaImg(String storePanoramaImg) {
		this.storePanoramaImg = storePanoramaImg;
	}

	public double getCommissionPercentage() {
		return commissionPercentage;
	}

	public void setCommissionPercentage(double commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
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

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}
	
	public String[] getStoreShowImgArray() {
		if (StringUtils.equals(null, storeShowImgs) || StringUtils.equals("", storeShowImgs)) {
			return new String[0];
		}
		JSONArray retArray = JSON.parseArray(getStoreShowImgs());
		return retArray.toArray(new String[] {});
	}

	public String getUserCID() {
		return userCID;
	}

	public void setUserCID(String userCID) {
		this.userCID = userCID;
	}

	public double getMemberCommissionPercentage() {
		return memberCommissionPercentage;
	}

	public void setMemberCommissionPercentage(double memberCommissionPercentage) {
		this.memberCommissionPercentage = memberCommissionPercentage;
	}

	public int getDeep() {
		return deep;
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}

	public String getSuperBusinessIds() {
		return superBusinessIds;
	}

	public void setSuperBusinessIds(String superBusinessIds) {
		this.superBusinessIds = superBusinessIds;
	}

	public String getBindWeixinId() {
		return bindWeixinId;
	}

	public void setBindWeixinId(String bindWeixinId) {
		this.bindWeixinId = bindWeixinId;
	}

	public String getBindWeixinName() {
		return bindWeixinName;
	}

	public void setBindWeixinName(String bindWeixinName) {
		this.bindWeixinName = bindWeixinName;
	}

	public String getBindWeixinIcon() {
		return bindWeixinIcon;
	}

	public void setBindWeixinIcon(String bindWeixinIcon) {
		this.bindWeixinIcon = bindWeixinIcon;
	}
	
	
	
}
