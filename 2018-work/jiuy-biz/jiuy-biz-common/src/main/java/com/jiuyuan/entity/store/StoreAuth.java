package com.jiuyuan.entity.store;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员认证信息
 * </p>
 *
 * @author 赵兴林
 * @since 2017-08-22
 */
@TableName("shop_store_auth")
public class StoreAuth extends Model<StoreAuth> {

    private static final long serialVersionUID = 1L;
//    认证审核状态：0未提交审核、1审核中、2审核通过、3审核不通过
    public static final int auth_state_no_submit = 0;
    public static final int auth_state_audit_ing= 1;
    public static final int auth_state_audit_pass = 2;
    public static final int auth_state_audit_no_pass = 3;
    
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 认证审核状态：0未提交审核、1审核中、2审核通过、3审核不通过
     */
	@TableField("auth_state")
	private Integer authState;
    /**
     * 认证审核不通过原因
     */
	@TableField("no_pass_reason")
	private String noPassReason;
    /**
     * 审核通过时间
     */
	@TableField("auth_pass_time")
	private Long authPassTime;
    /**
     * 审核不通过时间
     */
	@TableField("auth_no_pass_time")
	private Long authNoPassTime;
    /**
     * 认证类型：0线下门店有营业执照、1线下门店无营业执照、2线上门店、3其他
     */
	@TableField("auth_type")
	private Integer authType;
    /**
     * 实体店名
     */
	@TableField("physical_store_name")
	private String physicalStoreName;
    /**
     * 省份名称
     */
	private String province;
    /**
     * 城市名称
     */
	private String city;
    /**
     * 县区名称
     */
	private String county;
    /**
     * 详细地址
     */
	private String address;
    /**
     * 联系人
     */
	private String linkman;
    /**
     * 手机号
     */
	private String phone;
    /**
     * 微信号
     */
	private String weixin;
    /**
     * QQ
     */
	private String qq;
    /**
     * 邮箱
     */
	private String email;
    /**
     * 身份证正面
     */
	@TableField("identity_card_front")
	private String identityCardFront;
    /**
     * 身份证反面
     */
	@TableField("identity_card_reverse")
	private String identityCardReverse;
    /**
     * 营业执照图片
     */
	@TableField("business_license")
	private String businessLicense;
    /**
     * 门店照片集合，英文逗号分隔
     */
	@TableField("store_imgs")
	private String storeImgs;
    /**
     * 书面承诺书图片
     */
	@TableField("letter_of_commitment_img")
	private String letterOfCommitmentImg;
    /**
     * 经营平台
     */
	@TableField("manage_platform")
	private String managePlatform;
    /**
     * 网店名称
     */
	@TableField("online_store_name")
	private String onlineStoreName;
    /**
     * 网店链接
     */
	@TableField("online_store_url")
	private String onlineStoreUrl;
    /**
     * 资质证明图片集合
     */
	@TableField("qualification_evidence_imgs")
	private String qualificationEvidenceImgs;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getAuthState() {
		return authState;
	}

	public void setAuthState(Integer authState) {
		this.authState = authState;
	}

	public String getNoPassReason() {
		return noPassReason;
	}

	public void setNoPassReason(String noPassReason) {
		this.noPassReason = noPassReason;
	}

	public Long getAuthPassTime() {
		return authPassTime;
	}

	public void setAuthPassTime(Long authPassTime) {
		this.authPassTime = authPassTime;
	}

	public Long getAuthNoPassTime() {
		return authNoPassTime;
	}

	public void setAuthNoPassTime(Long authNoPassTime) {
		this.authNoPassTime = authNoPassTime;
	}

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public String getPhysicalStoreName() {
		return physicalStoreName;
	}

	public void setPhysicalStoreName(String physicalStoreName) {
		this.physicalStoreName = physicalStoreName;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentityCardFront() {
		return identityCardFront;
	}

	public void setIdentityCardFront(String identityCardFront) {
		this.identityCardFront = identityCardFront;
	}

	public String getIdentityCardReverse() {
		return identityCardReverse;
	}

	public void setIdentityCardReverse(String identityCardReverse) {
		this.identityCardReverse = identityCardReverse;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getStoreImgs() {
		return storeImgs;
	}

	public void setStoreImgs(String storeImgs) {
		this.storeImgs = storeImgs;
	}

	public String getLetterOfCommitmentImg() {
		return letterOfCommitmentImg;
	}

	public void setLetterOfCommitmentImg(String letterOfCommitmentImg) {
		this.letterOfCommitmentImg = letterOfCommitmentImg;
	}

	public String getManagePlatform() {
		return managePlatform;
	}

	public void setManagePlatform(String managePlatform) {
		this.managePlatform = managePlatform;
	}

	public String getOnlineStoreName() {
		return onlineStoreName;
	}

	public void setOnlineStoreName(String onlineStoreName) {
		this.onlineStoreName = onlineStoreName;
	}

	public String getOnlineStoreUrl() {
		return onlineStoreUrl;
	}

	public void setOnlineStoreUrl(String onlineStoreUrl) {
		this.onlineStoreUrl = onlineStoreUrl;
	}

	public String getQualificationEvidenceImgs() {
		return qualificationEvidenceImgs;
	}

	public void setQualificationEvidenceImgs(String qualificationEvidenceImgs) {
		this.qualificationEvidenceImgs = qualificationEvidenceImgs;
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
