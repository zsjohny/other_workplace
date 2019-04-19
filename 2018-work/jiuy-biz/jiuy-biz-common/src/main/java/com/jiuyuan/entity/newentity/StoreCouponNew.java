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
 * 
 * </p>
 *
 * @author nijin
 * @since 2018-03-13
 */
@TableName("store_Coupon")
public class StoreCouponNew extends Model<StoreCouponNew> {

    private static final long serialVersionUID = 1L;
    
    public static final int TYPE_GIVEN_USER = 0;
    public static final int TYPE_ALL_USER = 1;

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
     * 0 :指定用户的代金券,1：所有用户的代金券,3：指定注册手机号的代金券
     */
	private Integer Type;
    /**
     * 面值
     */
	private Double Money;
    /**
     * 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品, 4:邮费, 5:品牌
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
     * 门店ID
     */
	private Long StoreId;
    /**
     * 商家号
     */
	private Long BusinessNumber;
    /**
     * -2删除 -1:作废  0:未用 1:已分配 or 已使用
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
    /**
     * 范围类型ID集合.格式:,id,id,id,
     */
	private String RangeTypeIds;
    /**
     * 范围类型名称集合.格式:,name,name,
     */
	private String RangeTypeNames;
    /**
     * 发放优惠券的供应商ID
     */
	@TableField("supplier_id")
	private Long supplierId;
    /**
     * 领取时间
     */
	@TableField("draw_time")
	private Long drawTime;
    /**
     * 领取开始时间
     */
	@TableField("draw_start_time")
	private Long drawStartTime;
    /**
     * 领取结束时间
     */
	@TableField("draw_end_time")
	private Long drawEndTime;
    /**
     * 发行方
     */
	private String publisher;


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

	public Long getStoreId() {
		return StoreId;
	}

	public void setStoreId(Long StoreId) {
		this.StoreId = StoreId;
	}

	public Long getBusinessNumber() {
		return BusinessNumber;
	}

	public void setBusinessNumber(Long BusinessNumber) {
		this.BusinessNumber = BusinessNumber;
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

	public String getRangeTypeIds() {
		return RangeTypeIds;
	}

	public void setRangeTypeIds(String RangeTypeIds) {
		this.RangeTypeIds = RangeTypeIds;
	}

	public String getRangeTypeNames() {
		return RangeTypeNames;
	}

	public void setRangeTypeNames(String RangeTypeNames) {
		this.RangeTypeNames = RangeTypeNames;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(Long drawTime) {
		this.drawTime = drawTime;
	}

	public Long getDrawStartTime() {
		return drawStartTime;
	}

	public void setDrawStartTime(Long drawStartTime) {
		this.drawStartTime = drawStartTime;
	}

	public Long getDrawEndTime() {
		return drawEndTime;
	}

	public void setDrawEndTime(Long drawEndTime) {
		this.drawEndTime = drawEndTime;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@Override
	public String toString() {
		return "StoreCouponNew [Id=" + Id + ", OrderNo=" + OrderNo + ", Code=" + Code + ", CouponTemplateId="
				+ CouponTemplateId + ", TemplateName=" + TemplateName + ", Type=" + Type + ", Money=" + Money
				+ ", RangeType=" + RangeType + ", RangeContent=" + RangeContent + ", ValidityStartTime="
				+ ValidityStartTime + ", ValidityEndTime=" + ValidityEndTime + ", IsLimit=" + IsLimit + ", Coexist="
				+ Coexist + ", StoreId=" + StoreId + ", BusinessNumber=" + BusinessNumber + ", Status=" + Status
				+ ", CreateTime=" + CreateTime + ", UpdateTime=" + UpdateTime + ", PushStatus=" + PushStatus
				+ ", PushTitle=" + PushTitle + ", PushDescription=" + PushDescription + ", PushUrl=" + PushUrl
				+ ", PushImage=" + PushImage + ", PublishAdminId=" + PublishAdminId + ", GrantAdminId=" + GrantAdminId
				+ ", GetWay=" + GetWay + ", LimitMoney=" + LimitMoney + ", RangeTypeIds=" + RangeTypeIds
				+ ", RangeTypeNames=" + RangeTypeNames + ", supplierId=" + supplierId + ", drawTime=" + drawTime
				+ ", drawStartTime=" + drawStartTime + ", drawEndTime=" + drawEndTime + ", publisher=" + publisher
				+ "]";
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
