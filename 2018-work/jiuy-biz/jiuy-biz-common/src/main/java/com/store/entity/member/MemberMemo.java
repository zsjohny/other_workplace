package com.store.entity.member;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 小程序会员备注表
 * </p>
 *
 * @author 陈志勇
 * @since 2017-07-01
 */
@TableName("shop_member_memo")
public class MemberMemo extends Model<MemberMemo> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员Id
     */
	@TableField("member_id")
	private Long memberId;
	/**
	 * 门店Id
	 */
	@TableField("store_id")
	private Long storeId;
    /**
     * 备注名称
     */
	@TableField("memo_name")
	private String memoName;
    /**
     * 备注手机
     */
	@TableField("memo_phone")
	private String memoPhone;
    /**
     * 备注性别
     */
	@TableField("memo_gender")
	private String memoGender;
    /**
     * 备注职业
     */
	@TableField("memo_career")
	private String memoCareer;
    /**
     * 备注收入
     */
	@TableField("memo_earning")
	private String memoEarning;
    /**
     * 备注尺码
     */
	@TableField("memo_size")
	private String memoSize;
    /**
     * 备注性格
     */
	@TableField("memo_character")
	private String memoCharacter;
    /**
     * 省
     */
	@TableField("memo_province_name")
	private String memoProvinceName;
    /**
     * 市
     */
	@TableField("memo_city_name")
	private String memoCityName;
    /**
     * 区县
     */
	@TableField("memo_district_name")
	private String memoDistrictName;
    /**
     * 地址详情
     */
	@TableField("memo_address_detail")
	private String memoAddressDetail;
    /**
     * 备注描述
     */
	@TableField("memo_content")
	private String memoContent;
    /**
     * 状态 -1:删除 0:正常
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
    /**
     * 生日年份
     */
	@TableField("birthday_year")
	private String birthdayYear;
    /**
     * 生日月份
     */
	@TableField("birthday_month")
	private String birthdayMonth;
    /**
     * 生日天
     */
	@TableField("birthday_day")
	private String birthdayDay;
    /**
     * 胸围
     */
	@TableField("bust_size")
	private String bustSize;
    /**
     * 腰围
     */
	@TableField("waist_size")
	private String waistSize;
    /**
     * 臀围
     */
	@TableField("hip_size")
	private String hipSize;
    /**
     * 身高
     */
	private String height;
    /**
     * 体重
     */
	private String weight;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemoName() {
		return memoName;
	}

	public void setMemoName(String memoName) {
		this.memoName = memoName;
	}

	public String getMemoPhone() {
		return memoPhone;
	}

	public void setMemoPhone(String memoPhone) {
		this.memoPhone = memoPhone;
	}

	public String getMemoGender() {
		return memoGender;
	}

	public void setMemoGender(String memoGender) {
		this.memoGender = memoGender;
	}

	public String getMemoCareer() {
		return memoCareer;
	}

	public void setMemoCareer(String memoCareer) {
		this.memoCareer = memoCareer;
	}

	public String getMemoEarning() {
		return memoEarning;
	}

	public void setMemoEarning(String memoEarning) {
		this.memoEarning = memoEarning;
	}

	public String getMemoSize() {
		return memoSize;
	}

	public void setMemoSize(String memoSize) {
		this.memoSize = memoSize;
	}

	public String getMemoCharacter() {
		return memoCharacter;
	}

	public void setMemoCharacter(String memoCharacter) {
		this.memoCharacter = memoCharacter;
	}

	public String getMemoProvinceName() {
		return memoProvinceName;
	}

	public void setMemoProvinceName(String memoProvinceName) {
		this.memoProvinceName = memoProvinceName;
	}

	public String getMemoCityName() {
		return memoCityName;
	}

	public void setMemoCityName(String memoCityName) {
		this.memoCityName = memoCityName;
	}

	public String getMemoDistrictName() {
		return memoDistrictName;
	}

	public void setMemoDistrictName(String memoDistrictName) {
		this.memoDistrictName = memoDistrictName;
	}

	public String getMemoAddressDetail() {
		return memoAddressDetail;
	}

	public void setMemoAddressDetail(String memoAddressDetail) {
		this.memoAddressDetail = memoAddressDetail;
	}

	public String getMemoContent() {
		return memoContent;
	}

	public void setMemoContent(String memoContent) {
		this.memoContent = memoContent;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBirthdayYear() {
		return birthdayYear;
	}

	public void setBirthdayYear(String birthdayYear) {
		this.birthdayYear = birthdayYear;
	}

	public String getBirthdayMonth() {
		return birthdayMonth;
	}

	public void setBirthdayMonth(String birthdayMonth) {
		this.birthdayMonth = birthdayMonth;
	}

	public String getBirthdayDay() {
		return birthdayDay;
	}

	public void setBirthdayDay(String birthdayDay) {
		this.birthdayDay = birthdayDay;
	}

	public String getBustSize() {
		return bustSize;
	}

	public void setBustSize(String bustSize) {
		this.bustSize = bustSize;
	}

	public String getWaistSize() {
		return waistSize;
	}

	public void setWaistSize(String waistSize) {
		this.waistSize = waistSize;
	}

	public String getHipSize() {
		return hipSize;
	}

	public void setHipSize(String hipSize) {
		this.hipSize = hipSize;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
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

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

}
