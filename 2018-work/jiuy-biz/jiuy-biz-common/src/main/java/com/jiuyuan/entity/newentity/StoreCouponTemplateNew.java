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
@TableName("store_CouponTemplate")
public class StoreCouponTemplateNew extends Model<StoreCouponTemplateNew> {

    private static final long serialVersionUID = 1L;
    //范围类型
    public static final int RANGETYPE_COMMON_USE = 0;
    public static final int RANGETYPE_POST_FEE = 4;
    //优惠券类型
    public static final int TYPE_COUPON = 0;
    //发放状态
    public static final int NO_PUBLISH = 0;
    public static final int PUBLISHING = 1;
    public static final int STOPED = 2;
    public static final int CANCELL =3;
    //是否可以领取优惠券状态
    public static final int DRAW_STATUS_CAN = 0;
    public static final int DRAW_STATUS_CANT = 1;
    

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
	private String Name;
    /**
     * 0 : 代金券
     */
	private Integer Type;
    /**
     * 面值
     */
	private BigDecimal Money;
    /**
     * 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品, 4:邮费, 5:品牌
     */
	private Integer RangeType;
    /**
     * 范围。格式：限定商品：{ "productIds":[2,3]}  分类：{ "categoryIds":[2,3]}  限额订单：{ "limitOrders":[2,3]}
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
     * 发行量,平台优惠券
     */
	private Integer PublishCount;
    /**
     * 发放量,平台优惠券
     */
	private Integer GrantCount;
    /**
     * 可用量,平台优惠券
     */
	private Integer AvailableCount;
    /**
     * 优惠券共用性 0:不可共用 1：可共用
     */
	private Integer Coexist;
    /**
     * -1:删除 0:正常
     */
	private Integer Status;
	private Long CreateTime;
	private Long UpdateTime;
    /**
     * 订单限额
     */
	private BigDecimal LimitMoney;
    /**
     * 积分兑换 ： 0：不用于积分兑换  1：用于积分兑换
     */
	private Integer ExchangeJiuCoinSetting;
    /**
     * 兑换消耗的积分

     */
	private Integer ExchangeJiuCoinCost;
    /**
     * 兑换总量限制，-1代表不限
     */
	private Integer ExchangeLimitTotalCount;
    /**
     * 单人限购代金券张数，-1代表不限
     */
	private Integer ExchangeLimitSingleCount;
    /**
     * 已兑换的数量
     */
	private Integer ExchangeCount;
    /**
     * 兑换开始时间
     */
	private Long ExchangeStartTime;
    /**
     * 兑换结束时间
     */
	private Long ExchangeEndTime;
    /**
     * 积分促销设置 0：无，1：定义
     */
	private Integer PromotionJiuCoinSetting;
    /**
     * 兑换消耗的积分(促销积分)
     */
	private Integer PromotionJiuCoin;
    /**
     * 促销开始时间
     */
	private Long PromotionStartTime;
    /**
     * 促销结束时间
     */
	private Long PromotionEndTime;
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
     * 每人限领优惠券个数
     */
	@TableField("limit_draw")
	private Integer limitDraw;
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
    /**
     * 发放状态   -1：全部  1：发放中  2：已停止  3：已作废 
     */
	@TableField("publish_status")
	private Integer publishStatus;
    /**
     * 未使用优惠券总数,未过期前为可用优惠券总数,过期后为过期优惠券总数,不包含作废优惠券和已使用优惠券
     */
	@TableField("valid_total_count")
	private Integer validTotalCount;
    /**
     * 未使用优惠券总额,未过期前为可用优惠券总额,过期后为过期优惠券总额,不包含作废优惠券和已使用优惠券
     */
	@TableField("valid_total_amount")
	private BigDecimal validTotalAmount;
    /**
     * 已使用数量,供应商优惠券
     */
	@TableField("used_count")
	private Integer usedCount;
    /**
     * 已作废数量,供应商优惠券
     */
	@TableField("cancel_count")
	private Integer cancelCount;
    /**
     * 领取状态 0:可以领取 1:不可领取
     */
	@TableField("draw_status")
	private Integer drawStatus;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer Type) {
		this.Type = Type;
	}

	public BigDecimal getMoney() {
		return Money;
	}

	public void setMoney(BigDecimal Money) {
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

	public Integer getPublishCount() {
		return PublishCount;
	}

	public void setPublishCount(Integer PublishCount) {
		this.PublishCount = PublishCount;
	}

	public Integer getGrantCount() {
		return GrantCount;
	}

	public void setGrantCount(Integer GrantCount) {
		this.GrantCount = GrantCount;
	}

	public Integer getAvailableCount() {
		return AvailableCount;
	}

	public void setAvailableCount(Integer AvailableCount) {
		this.AvailableCount = AvailableCount;
	}

	public Integer getCoexist() {
		return Coexist;
	}

	public void setCoexist(Integer Coexist) {
		this.Coexist = Coexist;
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

	public BigDecimal getLimitMoney() {
		return LimitMoney;
	}

	public void setLimitMoney(BigDecimal LimitMoney) {
		this.LimitMoney = LimitMoney;
	}

	public Integer getExchangeJiuCoinSetting() {
		return ExchangeJiuCoinSetting;
	}

	public void setExchangeJiuCoinSetting(Integer ExchangeJiuCoinSetting) {
		this.ExchangeJiuCoinSetting = ExchangeJiuCoinSetting;
	}

	public Integer getExchangeJiuCoinCost() {
		return ExchangeJiuCoinCost;
	}

	public void setExchangeJiuCoinCost(Integer ExchangeJiuCoinCost) {
		this.ExchangeJiuCoinCost = ExchangeJiuCoinCost;
	}

	public Integer getExchangeLimitTotalCount() {
		return ExchangeLimitTotalCount;
	}

	public void setExchangeLimitTotalCount(Integer ExchangeLimitTotalCount) {
		this.ExchangeLimitTotalCount = ExchangeLimitTotalCount;
	}

	public Integer getExchangeLimitSingleCount() {
		return ExchangeLimitSingleCount;
	}

	public void setExchangeLimitSingleCount(Integer ExchangeLimitSingleCount) {
		this.ExchangeLimitSingleCount = ExchangeLimitSingleCount;
	}

	public Integer getExchangeCount() {
		return ExchangeCount;
	}

	public void setExchangeCount(Integer ExchangeCount) {
		this.ExchangeCount = ExchangeCount;
	}

	public Long getExchangeStartTime() {
		return ExchangeStartTime;
	}

	public void setExchangeStartTime(Long ExchangeStartTime) {
		this.ExchangeStartTime = ExchangeStartTime;
	}

	public Long getExchangeEndTime() {
		return ExchangeEndTime;
	}

	public void setExchangeEndTime(Long ExchangeEndTime) {
		this.ExchangeEndTime = ExchangeEndTime;
	}

	public Integer getPromotionJiuCoinSetting() {
		return PromotionJiuCoinSetting;
	}

	public void setPromotionJiuCoinSetting(Integer PromotionJiuCoinSetting) {
		this.PromotionJiuCoinSetting = PromotionJiuCoinSetting;
	}

	public Integer getPromotionJiuCoin() {
		return PromotionJiuCoin;
	}

	public void setPromotionJiuCoin(Integer PromotionJiuCoin) {
		this.PromotionJiuCoin = PromotionJiuCoin;
	}

	public Long getPromotionStartTime() {
		return PromotionStartTime;
	}

	public void setPromotionStartTime(Long PromotionStartTime) {
		this.PromotionStartTime = PromotionStartTime;
	}

	public Long getPromotionEndTime() {
		return PromotionEndTime;
	}

	public void setPromotionEndTime(Long PromotionEndTime) {
		this.PromotionEndTime = PromotionEndTime;
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

	public Integer getLimitDraw() {
		return limitDraw;
	}

	public void setLimitDraw(Integer limitDraw) {
		this.limitDraw = limitDraw;
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

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Integer getValidTotalCount() {
		return validTotalCount;
	}

	public void setValidTotalCount(Integer validTotalCount) {
		this.validTotalCount = validTotalCount;
	}

	public BigDecimal getValidTotalAmount() {
		return validTotalAmount;
	}

	public void setValidTotalAmount(BigDecimal validTotalAmount) {
		this.validTotalAmount = validTotalAmount;
	}

	public Integer getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	public Integer getCancelCount() {
		return cancelCount;
	}

	public void setCancelCount(Integer cancelCount) {
		this.cancelCount = cancelCount;
	}

	public Integer getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(Integer drawStatus) {
		this.drawStatus = drawStatus;
	}

	@Override
	public String toString() {
		return "StoreCouponTemplateNew [Id=" + Id + ", Name=" + Name + ", Type=" + Type + ", Money=" + Money
				+ ", RangeType=" + RangeType + ", RangeContent=" + RangeContent + ", ValidityStartTime="
				+ ValidityStartTime + ", ValidityEndTime=" + ValidityEndTime + ", IsLimit=" + IsLimit
				+ ", PublishCount=" + PublishCount + ", GrantCount=" + GrantCount + ", AvailableCount=" + AvailableCount
				+ ", Coexist=" + Coexist + ", Status=" + Status + ", CreateTime=" + CreateTime + ", UpdateTime="
				+ UpdateTime + ", LimitMoney=" + LimitMoney + ", ExchangeJiuCoinSetting=" + ExchangeJiuCoinSetting
				+ ", ExchangeJiuCoinCost=" + ExchangeJiuCoinCost + ", ExchangeLimitTotalCount="
				+ ExchangeLimitTotalCount + ", ExchangeLimitSingleCount=" + ExchangeLimitSingleCount
				+ ", ExchangeCount=" + ExchangeCount + ", ExchangeStartTime=" + ExchangeStartTime + ", ExchangeEndTime="
				+ ExchangeEndTime + ", PromotionJiuCoinSetting=" + PromotionJiuCoinSetting + ", PromotionJiuCoin="
				+ PromotionJiuCoin + ", PromotionStartTime=" + PromotionStartTime + ", PromotionEndTime="
				+ PromotionEndTime + ", RangeTypeIds=" + RangeTypeIds + ", RangeTypeNames=" + RangeTypeNames
				+ ", supplierId=" + supplierId + ", limitDraw=" + limitDraw + ", drawStartTime=" + drawStartTime
				+ ", drawEndTime=" + drawEndTime + ", publisher=" + publisher + ", publishStatus=" + publishStatus
				+ ", validTotalCount=" + validTotalCount + ", validTotalAmount=" + validTotalAmount + ", usedCount="
				+ usedCount + ", cancelCount=" + cancelCount + ", drawStatus="
				+ drawStatus + "]";
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
