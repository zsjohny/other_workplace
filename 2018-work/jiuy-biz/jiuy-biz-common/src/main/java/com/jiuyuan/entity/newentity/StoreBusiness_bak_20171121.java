package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 门店商家
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-11
 */
@TableName("yjj_StoreBusiness")
public class StoreBusiness_bak_20171121 extends Model<StoreBusiness_bak_20171121> {

    private static final long serialVersionUID = 1L;
    
	/*店铺类型：1实体店，2网店，3微商*/
	public static final int STORETYPE_PHYSICALSTORE=1;
	public static final int STORETYPE_ONLINESTORE=2;
	public static final int STORETYPE_WXASTORE=3;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 商家号
     */
	private Long BusinessNumber;
    /**
     * 商家名称
     */
	private String BusinessName;
    /**
     * 公司名称
     */
	private String CompanyName;
    /**
     * 账户状态0正常，-1删除，1 禁用
     */
	private Integer Status;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;
    /**
     * 会员数
     */
	private Integer MemberNumber;
    /**
     * 0正常，1 禁用
     */
	private Integer DistributionStatus;
    /**
     * 手机号码
     */
	private String PhoneNumber;
    /**
     * 账户资金
     */
	private double CashIncome;
    /**
     * 提成百分比
     */
	private double CommissionPercentage;
    /**
     * 会员购物提成百分比
     */
	private double MemberCommissionPercentage;
    /**
     * 默认下级商家提成百分比
     */
	private double DefaultCommissionPercentage;
    /**
     * 上级商家号   例如(,1,3,5,6,)    首尾都有逗号
     */
	private String SuperBusinessIds;
    /**
     * 可提现余额
     */
	private double AvailableBalance;
    /**
     * 提现申请次数
     */
	private Integer WithdrawApply;
    /**
     * 商家地址
     */
	private String BusinessAddress;
    /**
     * 营业执照号或者统一社会信用代码

     */
	private String LicenseNumber;
    /**
     * 组织机构号
     */
	private String OrganizationNo;
    /**
     * 税务登记号
     */
	private String TaxId;
    /**
     * 法定代表人
     */
	private String LegalPerson;
    /**
     * 老板手机号码
     */
	private String LegalPhone;
    /**
     * 所在省份

     */
	private String Province;
    /**
     * 所在城市

     */
	private String City;
    /**
     * 所在区县
     */
	private String County;
    /**
     * 门店建筑面积
     */
	private double StoreArea;
    /**
     * 门店描述
     */
	private String StoreDescription;
    /**
     * 门店LOGO

     */
	private String StoreLogo;
    /**
     * 门店展示图片
     */
	private String StoreShowImgs;
    /**
     * 门店全景展示图片
     */
	private String StorePanoramaImg;
    /**
     * 银行开户名称
     */
	private String BankAccountName;
    /**
     * 开户银行名称
     */
	private String BankName;
    /**
     * 银行账户号
     */
	private String BankAccountNo;
    /**
     * 用户名
     */
	private String UserName;
	private String UserPassword;
    /**
     * 银行卡选择标记 0 未选中， 1选中
     */
	private Integer BankCardFlag;
    /**
     * 支付宝选择标记 0 未选中， 1选中
     */
	private Integer AlipayFlag;
    /**
     * 支付宝账号
     */
	private String AlipayAccount;
    /**
     * 支付宝关联人姓名
     */
	private String AlipayName;
    /**
     * 微信选择标记 0 未选中， 1选中
     */
	private Integer WeixinFlag;
    /**
     * 微信账号
     */
	private String WeixinAccount;
    /**
     * 微信关联姓名
     */
	private String WeixinName;
    /**
     * 身份证号
     */
	private String IdCardNumber;
    /**
     * 商家类型 0企业 1个人
     */
	private Integer BusinessType;
    /**
     * 提现密码
     */
	private String WithdrawPassword;
    /**
     * 门店等级 参考门店全局设置
     */
	private Integer Grade;
    /**
     * 营业时间
     */
	private String BusinessHours;
    /**
     * 签约日期
     */
	private String SignTime;
    /**
     * 账号激活时间
     */
	private Long ActiveTime;
    /**
     * 用户cid
     */
	private String UserCID;
    /**
     * 门店选择银行卡标记 0未选中， 1选中
     */
	private Integer BankCardUseFlag;
    /**
     * 门店选择支付宝标记 0未选中， 1选中
     */
	private Integer AlipayUseFlag;
    /**
     * 门店选择微信标记 0未选中， 1选中
     */
	private Integer WeixinUseFlag;
    /**
     * 近期首次账户输入错误时间
     */
	private Long LastErrorWithdrawPasswordTime;
    /**
     * 输错次数
     */
	private Integer ErrorCount;
    /**
     * 门店层级
     */
	private Long Deep;
    /**
     * 微信登录ID
     */
	private String BindWeixinId;
    /**
     * 绑定微信账号名称
     */
	private String BindWeixinName;
    /**
     * 绑定微信账号头像URL
     */
	private String BindWeixinIcon;
    /**
     * 法人身份证号码
     */
	private String LegalIdNumber;
	private Long ProtocolTime;
    /**
     * 已废弃,可随时删除
     */
	private String AppId;
    /**
     * 小程序appId
     */
	private String WxaAppId;
    /**
     * 开发者微信号
     */
	private String WeiXinNum;
    /**
     * 商家公告
     */
	private String storeNotice;
    /**
     * VIP门店：0不是Vip门店，1是Vip门店
     */
	private Integer vip;
    /**
     * 商家优惠券核销人数
     */
	@TableField("used_coupon_total_member_count")
	private Integer usedCouponTotalMemberCount;
    /**
     * 商家优惠券核销张数
     */
	@TableField("used_coupon_total_count")
	private Integer usedCouponTotalCount;
    /**
     * 商家优惠券核销面值总金额（单位元）
     */
	@TableField("used_coupon_total_money")
	private double usedCouponTotalMoney;
    /**
     * 是否开通小程序：0未开通，1已开通
     */
	@TableField("is_open_wxa")
	private Integer isOpenWxa;
    /**
     * 小程序类型：0个人、1企业小程序
     */
	private Integer wxaType;
    /**
     * 资质证明图片，英文逗号分隔的字符串
     */
	private String qualificationProofImages;
    /**
     * 倍率 保留一位小数
     */
	private double rate;
    /**
     * 同步上新按钮状态 0：关闭  1：开启
     */
	private Integer synchronousButtonStatus;
	
	private boolean firstStore;//是否是一级门店
	
	private String receiverName;
	
	private String telephone;
	
	private String superStoreNameAndNumber;//上级门店名称及号码
	
	private long superBusinessNumber;//上级门店商家号
	
	private String addrFull;
	
    private long supplierId;//供应商ID
    /**
     * 0:老用户,1:首次审核通过登录,2:非首次登录
     */
    @TableField("first_login_status")
    private int firstLoginStatus;//是否首次登录
	/**
     * 第一阶段结束时间
     */
	@TableField("one_stage_time")
	private int oneStageTime;
    /**
     * 第二阶段结束时间
     */
	@TableField("two_stage_time")
	private int twoStageTime;
    /**
     * 第三阶段结束时间
     */
	@TableField("three_stage_time")
	private int threeStageTime;
    /**
     * 地推用户id
     */
	@TableField("ground_user_id")
	private Long groundUserId;
    /**
     * 所有上级ids,例如(,1,3,5,6,)
     */
	@TableField("super_ids")
	private String superIds;
    /**
     * 门店头图,英文逗号分隔
     */
    @TableField("store_display_images")
	private String storeDisplayImages;
    /**
     * 0：提交审核 1：审核通过   -1:审核不通过 -2:禁用
     */
	@TableField("audit_status")
	private Integer auditStatus;
    /**
     * 0:未第一次审核通过；>0审核通过时间
     */
	@TableField("audit_time")
	private Long auditTime;
	/**
	 * 0:未激活；>0激活时间
	 */
	@TableField("activation_time")
	private Long activationTime;
	
	public int getFirstLoginStatus() {
		return firstLoginStatus;
	}

	public void setFirstLoginStatus(int firstLoginStatus) {
		this.firstLoginStatus = firstLoginStatus;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public StoreBusiness_bak_20171121() {
		super();
	}

	public StoreBusiness_bak_20171121(String businessName2, String companyName2, String phoneNumber2,
			int k, String businessAddress2, String legalPerson2, String idCardNumber2, int businessType2) {
		this.BusinessName=businessName2;
		this.CompanyName=companyName2;
		this.PhoneNumber=phoneNumber2;
		this.CommissionPercentage=k;
		this.BusinessAddress=businessAddress2;
		this.LegalPerson=legalPerson2;
		this.IdCardNumber=idCardNumber2;
		this.BusinessType=businessType2;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getBusinessNumber() {
		return BusinessNumber;
	}

	public void setBusinessNumber(Long BusinessNumber) {
		this.BusinessNumber = BusinessNumber;
	}

	public String getBusinessName() {
		return BusinessName;
	}

	public void setBusinessName(String BusinessName) {
		this.BusinessName = BusinessName;
	}

	public String getCompanyName() {
		return CompanyName;
	}

	public void setCompanyName(String CompanyName) {
		this.CompanyName = CompanyName;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	public Long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Long UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	public Integer getMemberNumber() {
		return MemberNumber;
	}

	public void setMemberNumber(Integer MemberNumber) {
		this.MemberNumber = MemberNumber;
	}

	public Integer getDistributionStatus() {
		return DistributionStatus;
	}

	public void setDistributionStatus(Integer DistributionStatus) {
		this.DistributionStatus = DistributionStatus;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String PhoneNumber) {
		this.PhoneNumber = PhoneNumber;
	}

	public double getCashIncome() {
		return CashIncome;
	}

	public void setCashIncome(double CashIncome) {
		this.CashIncome = CashIncome;
	}

	public double getCommissionPercentage() {
		return CommissionPercentage;
	}

	public void setCommissionPercentage(double CommissionPercentage) {
		this.CommissionPercentage = CommissionPercentage;
	}

	public double getMemberCommissionPercentage() {
		return MemberCommissionPercentage;
	}

	public void setMemberCommissionPercentage(double MemberCommissionPercentage) {
		this.MemberCommissionPercentage = MemberCommissionPercentage;
	}

	public double getDefaultCommissionPercentage() {
		return DefaultCommissionPercentage;
	}

	public void setDefaultCommissionPercentage(double DefaultCommissionPercentage) {
		this.DefaultCommissionPercentage = DefaultCommissionPercentage;
	}

	public String getSuperBusinessIds() {
		return SuperBusinessIds;
	}

	public void setSuperBusinessIds(String SuperBusinessIds) {
		this.SuperBusinessIds = SuperBusinessIds;
	}

	public double getAvailableBalance() {
		return AvailableBalance;
	}

	public void setAvailableBalance(double AvailableBalance) {
		this.AvailableBalance = AvailableBalance;
	}

	public Integer getWithdrawApply() {
		return WithdrawApply;
	}

	public void setWithdrawApply(Integer WithdrawApply) {
		this.WithdrawApply = WithdrawApply;
	}

	public String getBusinessAddress() {
		return BusinessAddress;
	}

	public void setBusinessAddress(String BusinessAddress) {
		this.BusinessAddress = BusinessAddress;
	}

	public String getLicenseNumber() {
		return LicenseNumber;
	}

	public void setLicenseNumber(String LicenseNumber) {
		this.LicenseNumber = LicenseNumber;
	}

	public String getOrganizationNo() {
		return OrganizationNo;
	}

	public void setOrganizationNo(String OrganizationNo) {
		this.OrganizationNo = OrganizationNo;
	}

	public String getTaxId() {
		return TaxId;
	}

	public void setTaxId(String TaxId) {
		this.TaxId = TaxId;
	}

	public String getLegalPerson() {
		return LegalPerson;
	}

	public void setLegalPerson(String LegalPerson) {
		this.LegalPerson = LegalPerson;
	}

	public String getLegalPhone() {
		return LegalPhone;
	}

	public void setLegalPhone(String LegalPhone) {
		this.LegalPhone = LegalPhone;
	}

	public String getProvince() {
		return Province;
	}

	public void setProvince(String Province) {
		this.Province = Province;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String City) {
		this.City = City;
	}

	public String getCounty() {
		return County;
	}

	public void setCounty(String County) {
		this.County = County;
	}

	public double getStoreArea() {
		return StoreArea;
	}

	public void setStoreArea(double StoreArea) {
		this.StoreArea = StoreArea;
	}

	public String getStoreDescription() {
		return StoreDescription;
	}

	public void setStoreDescription(String StoreDescription) {
		this.StoreDescription = StoreDescription;
	}

	public String getStoreLogo() {
		return StoreLogo;
	}

	public void setStoreLogo(String StoreLogo) {
		this.StoreLogo = StoreLogo;
	}

	public String getStoreShowImgs() {
		return StoreShowImgs;
	}

	public void setStoreShowImgs(String StoreShowImgs) {
		this.StoreShowImgs = StoreShowImgs;
	}

	public String getStorePanoramaImg() {
		return StorePanoramaImg;
	}

	public void setStorePanoramaImg(String StorePanoramaImg) {
		this.StorePanoramaImg = StorePanoramaImg;
	}

	public String getBankAccountName() {
		return BankAccountName;
	}

	public void setBankAccountName(String BankAccountName) {
		this.BankAccountName = BankAccountName;
	}

	public String getBankName() {
		return BankName;
	}

	public void setBankName(String BankName) {
		this.BankName = BankName;
	}

	public String getBankAccountNo() {
		return BankAccountNo;
	}

	public void setBankAccountNo(String BankAccountNo) {
		this.BankAccountNo = BankAccountNo;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String UserName) {
		this.UserName = UserName;
	}

	public String getUserPassword() {
		return UserPassword;
	}

	public void setUserPassword(String UserPassword) {
		this.UserPassword = UserPassword;
	}

	public Integer getBankCardFlag() {
		return BankCardFlag;
	}

	public void setBankCardFlag(Integer BankCardFlag) {
		this.BankCardFlag = BankCardFlag;
	}

	public Integer getAlipayFlag() {
		return AlipayFlag;
	}

	public void setAlipayFlag(Integer AlipayFlag) {
		this.AlipayFlag = AlipayFlag;
	}

	public String getAlipayAccount() {
		return AlipayAccount;
	}

	public void setAlipayAccount(String AlipayAccount) {
		this.AlipayAccount = AlipayAccount;
	}

	public String getAlipayName() {
		return AlipayName;
	}

	public void setAlipayName(String AlipayName) {
		this.AlipayName = AlipayName;
	}

	public Integer getWeixinFlag() {
		return WeixinFlag;
	}

	public void setWeixinFlag(Integer WeixinFlag) {
		this.WeixinFlag = WeixinFlag;
	}

	public String getWeixinAccount() {
		return WeixinAccount;
	}

	public void setWeixinAccount(String WeixinAccount) {
		this.WeixinAccount = WeixinAccount;
	}

	public String getWeixinName() {
		return WeixinName;
	}

	public void setWeixinName(String WeixinName) {
		this.WeixinName = WeixinName;
	}

	public String getIdCardNumber() {
		return IdCardNumber;
	}

	public void setIdCardNumber(String IdCardNumber) {
		this.IdCardNumber = IdCardNumber;
	}

	public Integer getBusinessType() {
		return BusinessType;
	}

	public void setBusinessType(Integer BusinessType) {
		this.BusinessType = BusinessType;
	}

	public String getWithdrawPassword() {
		return WithdrawPassword;
	}

	public void setWithdrawPassword(String WithdrawPassword) {
		this.WithdrawPassword = WithdrawPassword;
	}

	public Integer getGrade() {
		return Grade;
	}

	public void setGrade(Integer Grade) {
		this.Grade = Grade;
	}

	public String getBusinessHours() {
		return BusinessHours;
	}

	public void setBusinessHours(String BusinessHours) {
		this.BusinessHours = BusinessHours;
	}

	public String getSignTime() {
		return SignTime;
	}

	public void setSignTime(String SignTime) {
		this.SignTime = SignTime;
	}

	public Long getActiveTime() {
		return ActiveTime;
	}

	public void setActiveTime(Long ActiveTime) {
		this.ActiveTime = ActiveTime;
	}

	public String getUserCID() {
		return UserCID;
	}

	public void setUserCID(String UserCID) {
		this.UserCID = UserCID;
	}

	public Integer getBankCardUseFlag() {
		return BankCardUseFlag;
	}

	public void setBankCardUseFlag(Integer BankCardUseFlag) {
		this.BankCardUseFlag = BankCardUseFlag;
	}

	public Integer getAlipayUseFlag() {
		return AlipayUseFlag;
	}

	public void setAlipayUseFlag(Integer AlipayUseFlag) {
		this.AlipayUseFlag = AlipayUseFlag;
	}

	public Integer getWeixinUseFlag() {
		return WeixinUseFlag;
	}

	public void setWeixinUseFlag(Integer WeixinUseFlag) {
		this.WeixinUseFlag = WeixinUseFlag;
	}

	public Long getLastErrorWithdrawPasswordTime() {
		return LastErrorWithdrawPasswordTime;
	}

	public void setLastErrorWithdrawPasswordTime(Long LastErrorWithdrawPasswordTime) {
		this.LastErrorWithdrawPasswordTime = LastErrorWithdrawPasswordTime;
	}

	public Integer getErrorCount() {
		return ErrorCount;
	}

	public void setErrorCount(Integer ErrorCount) {
		this.ErrorCount = ErrorCount;
	}

	public Long getDeep() {
		return Deep;
	}

	public void setDeep(Long Deep) {
		this.Deep = Deep;
	}

	public String getBindWeixinId() {
		return BindWeixinId;
	}

	public void setBindWeixinId(String BindWeixinId) {
		this.BindWeixinId = BindWeixinId;
	}

	public String getBindWeixinName() {
		return BindWeixinName;
	}

	public void setBindWeixinName(String BindWeixinName) {
		this.BindWeixinName = BindWeixinName;
	}

	public String getBindWeixinIcon() {
		return BindWeixinIcon;
	}

	public void setBindWeixinIcon(String BindWeixinIcon) {
		this.BindWeixinIcon = BindWeixinIcon;
	}

	public String getLegalIdNumber() {
		return LegalIdNumber;
	}

	public void setLegalIdNumber(String LegalIdNumber) {
		this.LegalIdNumber = LegalIdNumber;
	}

	public Long getProtocolTime() {
		return ProtocolTime;
	}

	public void setProtocolTime(Long ProtocolTime) {
		this.ProtocolTime = ProtocolTime;
	}

	public String getAppId() {
		return AppId;
	}

	public void setAppId(String AppId) {
		this.AppId = AppId;
	}

	public String getWxaAppId() {
		return WxaAppId;
	}

	public void setWxaAppId(String WxaAppId) {
		this.WxaAppId = WxaAppId;
	}

	public String getWeiXinNum() {
		return WeiXinNum;
	}

	public void setWeiXinNum(String WeiXinNum) {
		this.WeiXinNum = WeiXinNum;
	}

	public String getStoreNotice() {
		return storeNotice;
	}

	public void setStoreNotice(String storeNotice) {
		this.storeNotice = storeNotice;
	}

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	public Integer getUsedCouponTotalMemberCount() {
		return usedCouponTotalMemberCount;
	}

	public void setUsedCouponTotalMemberCount(Integer usedCouponTotalMemberCount) {
		this.usedCouponTotalMemberCount = usedCouponTotalMemberCount;
	}

	public Integer getUsedCouponTotalCount() {
		return usedCouponTotalCount;
	}

	public void setUsedCouponTotalCount(Integer usedCouponTotalCount) {
		this.usedCouponTotalCount = usedCouponTotalCount;
	}

	public double getUsedCouponTotalMoney() {
		return usedCouponTotalMoney;
	}

	public void setUsedCouponTotalMoney(double usedCouponTotalMoney) {
		this.usedCouponTotalMoney = usedCouponTotalMoney;
	}

	public Integer getIsOpenWxa() {
		return isOpenWxa;
	}

	public void setIsOpenWxa(Integer isOpenWxa) {
		this.isOpenWxa = isOpenWxa;
	}

	public Integer getWxaType() {
		return wxaType;
	}

	public void setWxaType(Integer wxaType) {
		this.wxaType = wxaType;
	}

	public String getQualificationProofImages() {
		return qualificationProofImages;
	}

	public void setQualificationProofImages(String qualificationProofImages) {
		this.qualificationProofImages = qualificationProofImages;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Integer getSynchronousButtonStatus() {
		return synchronousButtonStatus;
	}

	public void setSynchronousButtonStatus(Integer synchronousButtonStatus) {
		this.synchronousButtonStatus = synchronousButtonStatus;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

	public boolean isFirstStore() {
		return firstStore;
	}

	public void setFirstStore(boolean firstStore) {
		this.firstStore = firstStore;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSuperStoreNameAndNumber() {
		return superStoreNameAndNumber;
	}

	public void setSuperStoreNameAndNumber(String superStoreNameAndNumber) {
		this.superStoreNameAndNumber = superStoreNameAndNumber;
	}

	public long getSuperBusinessNumber() {
		return superBusinessNumber;
	}

	public void setSuperBusinessNumber(long superBusinessNumber) {
		this.superBusinessNumber = superBusinessNumber;
	}

	public String getAddrFull() {
		return addrFull;
	}

	public void setAddrFull(String addrFull) {
		this.addrFull = addrFull;
	}

	public String getStoreDisplayImages() {
		return storeDisplayImages;
	}

	public void setStoreDisplayImages(String storeDisplayImages) {
		this.storeDisplayImages = storeDisplayImages;
	}
	public int getOneStageTime() {
		return oneStageTime;
	}

	public void setOneStageTime(int oneStageTime) {
		this.oneStageTime = oneStageTime;
	}

	public int getTwoStageTime() {
		return twoStageTime;
	}

	public void setTwoStageTime(int twoStageTime) {
		this.twoStageTime = twoStageTime;
	}

	public int getThreeStageTime() {
		return threeStageTime;
	}

	public void setThreeStageTime(int threeStageTime) {
		this.threeStageTime = threeStageTime;
	}

	public Long getGroundUserId() {
		return groundUserId;
	}

	public void setGroundUserId(Long groundUserId) {
		this.groundUserId = groundUserId;
	}

	public String getSuperIds() {
		return superIds;
	}

	public void setSuperIds(String superIds) {
		this.superIds = superIds;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Long auditTime) {
		this.auditTime = auditTime;
	}

	public Long getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(Long activationTime) {
		this.activationTime = activationTime;
	}

	@Override
	public String toString() {
		return "StoreBusiness [Id=" + Id + ", BusinessNumber=" + BusinessNumber + ", BusinessName=" + BusinessName
				+ ", CompanyName=" + CompanyName + ", Status=" + Status + ", CreateTime=" + CreateTime + ", UpdateTime="
				+ UpdateTime + ", MemberNumber=" + MemberNumber + ", DistributionStatus=" + DistributionStatus
				+ ", PhoneNumber=" + PhoneNumber + ", CashIncome=" + CashIncome + ", CommissionPercentage="
				+ CommissionPercentage + ", MemberCommissionPercentage=" + MemberCommissionPercentage
				+ ", DefaultCommissionPercentage=" + DefaultCommissionPercentage + ", SuperBusinessIds="
				+ SuperBusinessIds + ", AvailableBalance=" + AvailableBalance + ", WithdrawApply=" + WithdrawApply
				+ ", BusinessAddress=" + BusinessAddress + ", LicenseNumber=" + LicenseNumber + ", OrganizationNo="
				+ OrganizationNo + ", TaxId=" + TaxId + ", LegalPerson=" + LegalPerson + ", LegalPhone=" + LegalPhone
				+ ", Province=" + Province + ", City=" + City + ", County=" + County + ", StoreArea=" + StoreArea
				+ ", StoreDescription=" + StoreDescription + ", StoreLogo=" + StoreLogo + ", StoreShowImgs="
				+ StoreShowImgs + ", StorePanoramaImg=" + StorePanoramaImg + ", BankAccountName=" + BankAccountName
				+ ", BankName=" + BankName + ", BankAccountNo=" + BankAccountNo + ", UserName=" + UserName
				+ ", UserPassword=" + UserPassword + ", BankCardFlag=" + BankCardFlag + ", AlipayFlag=" + AlipayFlag
				+ ", AlipayAccount=" + AlipayAccount + ", AlipayName=" + AlipayName + ", WeixinFlag=" + WeixinFlag
				+ ", WeixinAccount=" + WeixinAccount + ", WeixinName=" + WeixinName + ", IdCardNumber=" + IdCardNumber
				+ ", BusinessType=" + BusinessType + ", WithdrawPassword=" + WithdrawPassword + ", Grade=" + Grade
				+ ", BusinessHours=" + BusinessHours + ", SignTime=" + SignTime + ", ActiveTime=" + ActiveTime
				+ ", UserCID=" + UserCID + ", BankCardUseFlag=" + BankCardUseFlag + ", AlipayUseFlag=" + AlipayUseFlag
				+ ", WeixinUseFlag=" + WeixinUseFlag + ", LastErrorWithdrawPasswordTime="
				+ LastErrorWithdrawPasswordTime + ", ErrorCount=" + ErrorCount + ", Deep=" + Deep + ", BindWeixinId="
				+ BindWeixinId + ", BindWeixinName=" + BindWeixinName + ", BindWeixinIcon=" + BindWeixinIcon
				+ ", LegalIdNumber=" + LegalIdNumber + ", ProtocolTime=" + ProtocolTime + ", AppId=" + AppId
				+ ", WxaAppId=" + WxaAppId + ", WeiXinNum=" + WeiXinNum + ", storeNotice=" + storeNotice + ", vip="
				+ vip + ", usedCouponTotalMemberCount=" + usedCouponTotalMemberCount + ", usedCouponTotalCount="
				+ usedCouponTotalCount + ", usedCouponTotalMoney=" + usedCouponTotalMoney + ", isOpenWxa=" + isOpenWxa
				+ ", wxaType=" + wxaType + ", qualificationProofImages=" + qualificationProofImages + ", rate=" + rate
				+ ", synchronousButtonStatus=" + synchronousButtonStatus + ", firstStore=" + firstStore
				+ ", receiverName=" + receiverName + ", telephone=" + telephone + ", superStoreNameAndNumber="
				+ superStoreNameAndNumber + ", superBusinessNumber=" + superBusinessNumber + ", addrFull=" + addrFull
				+ ", supplierId=" + supplierId + "]";
	}

}
