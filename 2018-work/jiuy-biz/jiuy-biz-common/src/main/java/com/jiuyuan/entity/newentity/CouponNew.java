package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 优惠券管理

 * </p>
 *
 * @author nijin
 * @since 2017-11-02
 */
@TableName("yjj_Coupon")
public class CouponNew extends Model<CouponNew> {

    private static final long serialVersionUID = 1L;


	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
	private Long OrderNo;
    /**
     * 兑换码(NULL代表未填写兑换码)
     */
	private String Code;
    /**
     * 优惠券模板id
     */
	private Long CouponTemplateId;
    /**
     * 模板名称
     */
	private String TemplateName;
    /**
     * 0 :指定用户的 代金券，1：所有用户的代金券
     */
	private Integer Type;
    /**
     * 面值
     */
	private Double Money;
    /**
     * 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品 4.限定品牌 5.免邮
     */
	private Integer RangeType;
    /**
     * 范围 格式：例如{ "productIds":[2,3]}
     */
	private String RangeContent;
    /**
     * 有效开始时间
     */
	private Long ValidityStartTime;
    /**
     * 有效结束时间 0:无限制
     */
	private Long ValidityEndTime;
    /**
     * 优惠限制 0:无 1:有
     */
	private Integer IsLimit;
    /**
     * 优惠券共用性 0:不可共用 1：可共用
     */
	private Integer Coexist;
    /**
     * 用户id
     */
	private Long UserId;
    /**
     * 俞姐姐号
     */
	private Long YJJNumber;
    /**
     * -1:作废  0:未用 1:已分配 or 已使用
     */
	private Integer Status;
	private Long CreateTime;
	private Long UpdateTime;
    /**
     * -1:不需要推，0：待推送，1：已推送
     */
	private Integer PushStatus;
    /**
     * 推送标题
     */
	private String PushTitle;
    /**
     * 推送描述
     */
	private String PushDescription;
    /**
     * 推送url，若不填写URL，提交发放，默认为“我的代金券”界面
     */
	private String PushUrl;
    /**
     * 推送图片
     */
	private String PushImage;
    /**
     * 发行操作的管理员
     */
	private Long PublishAdminId;
    /**
     * 发放操作的管理员
     */
	private Long GrantAdminId;
    /**
     * 获取方式 0：发放 1：领取 2：邀请
     */
	private Integer GetWay;
    /**
     * 订单限额
     */
	private BigDecimal LimitMoney;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(Long OrderNo) {
		this.OrderNo = OrderNo;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String Code) {
		this.Code = Code;
	}

	public Long getCouponTemplateId() {
		return CouponTemplateId;
	}

	public void setCouponTemplateId(Long CouponTemplateId) {
		this.CouponTemplateId = CouponTemplateId;
	}

	public String getTemplateName() {
		return TemplateName;
	}

	public void setTemplateName(String TemplateName) {
		this.TemplateName = TemplateName;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer Type) {
		this.Type = Type;
	}

	public Double getMoney() {
		return Money;
	}

	public void setMoney(Double Money) {
		this.Money = Money;
	}

	public Integer getRangeType() {
		return RangeType;
	}

	public void setRangeType(Integer RangeType) {
		this.RangeType = RangeType;
	}

	public String getRangeContent() {
		return RangeContent;
	}

	public void setRangeContent(String RangeContent) {
		this.RangeContent = RangeContent;
	}

	public Long getValidityStartTime() {
		return ValidityStartTime;
	}

	public void setValidityStartTime(Long ValidityStartTime) {
		this.ValidityStartTime = ValidityStartTime;
	}

	public Long getValidityEndTime() {
		return ValidityEndTime;
	}

	public void setValidityEndTime(Long ValidityEndTime) {
		this.ValidityEndTime = ValidityEndTime;
	}

	public Integer getIsLimit() {
		return IsLimit;
	}

	public void setIsLimit(Integer IsLimit) {
		this.IsLimit = IsLimit;
	}

	public Integer getCoexist() {
		return Coexist;
	}

	public void setCoexist(Integer Coexist) {
		this.Coexist = Coexist;
	}

	public Long getUserId() {
		return UserId;
	}

	public void setUserId(Long UserId) {
		this.UserId = UserId;
	}

	public Long getYJJNumber() {
		return YJJNumber;
	}

	public void setYJJNumber(Long YJJNumber) {
		this.YJJNumber = YJJNumber;
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

	public Integer getPushStatus() {
		return PushStatus;
	}

	public void setPushStatus(Integer PushStatus) {
		this.PushStatus = PushStatus;
	}

	public String getPushTitle() {
		return PushTitle;
	}

	public void setPushTitle(String PushTitle) {
		this.PushTitle = PushTitle;
	}

	public String getPushDescription() {
		return PushDescription;
	}

	public void setPushDescription(String PushDescription) {
		this.PushDescription = PushDescription;
	}

	public String getPushUrl() {
		return PushUrl;
	}

	public void setPushUrl(String PushUrl) {
		this.PushUrl = PushUrl;
	}

	public String getPushImage() {
		return PushImage;
	}

	public void setPushImage(String PushImage) {
		this.PushImage = PushImage;
	}

	public Long getPublishAdminId() {
		return PublishAdminId;
	}

	public void setPublishAdminId(Long PublishAdminId) {
		this.PublishAdminId = PublishAdminId;
	}

	public Long getGrantAdminId() {
		return GrantAdminId;
	}

	public void setGrantAdminId(Long GrantAdminId) {
		this.GrantAdminId = GrantAdminId;
	}

	public Integer getGetWay() {
		return GetWay;
	}

	public void setGetWay(Integer GetWay) {
		this.GetWay = GetWay;
	}

	public BigDecimal getLimitMoney() {
		return LimitMoney;
	}

	public void setLimitMoney(BigDecimal LimitMoney) {
		this.LimitMoney = LimitMoney;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

	@Override
	public String toString() {
		return "YjjCoupon{" +
			"Id=" + Id +
			", OrderNo=" + OrderNo +
			", Code=" + Code +
			", CouponTemplateId=" + CouponTemplateId +
			", TemplateName=" + TemplateName +
			", Type=" + Type +
			", Money=" + Money +
			", RangeType=" + RangeType +
			", RangeContent=" + RangeContent +
			", ValidityStartTime=" + ValidityStartTime +
			", ValidityEndTime=" + ValidityEndTime +
			", IsLimit=" + IsLimit +
			", Coexist=" + Coexist +
			", UserId=" + UserId +
			", YJJNumber=" + YJJNumber +
			", Status=" + Status +
			", CreateTime=" + CreateTime +
			", UpdateTime=" + UpdateTime +
			", PushStatus=" + PushStatus +
			", PushTitle=" + PushTitle +
			", PushDescription=" + PushDescription +
			", PushUrl=" + PushUrl +
			", PushImage=" + PushImage +
			", PublishAdminId=" + PublishAdminId +
			", GrantAdminId=" + GrantAdminId +
			", GetWay=" + GetWay +
			", LimitMoney=" + LimitMoney +
			"}";
	}
}
