package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户订单细目表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-11
 */
@TableName("store_OrderItem")
public class StoreOrderItemNew extends Model<StoreOrderItemNew> {

    private static final long serialVersionUID = 1L;
    
    /**
     * 退款状态
     */
//    public static final int NO_REFUND = 0; //0(无退款)
//    public static final int REFUNDING = 1; //1(退款进行中)
//    public static final int PART_REFUNDED = 2; //2(部分退款完成)
//    public static final int ALL_REFUNDED = 3; //3(全部退款完成)

    /**
     * id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 新订单表OrderNo
     */
	private Long OrderNo;
    /**
     * 用户id
     */
	private Long StoreId;
    /**
     * 对应Product表的id
     */
	private Long ProductId;
    /**
     * 对应ProductSKU的id
     */
	private Long SkuId;
    /**
     * 订单金额总价，不包含邮费
     */
	private Double TotalMoney;
    /**
     * 邮费总价
     */
	private Double TotalExpressMoney;
    /**
     * 订单细目单价，不包含邮费
     */
	private Double Money;
    /**
     * 邮费单价
     */
	private Double ExpressMoney;
    /**
     * 总玖币
     */
	private Integer TotalUnavalCoinUsed;
    /**
     * 玖币
     */
	private Integer UnavalCoinUsed;
    /**
     * 订购数量
     */
	private Integer BuyCount;
    /**
     * sku快照，json
     */
	private String SkuSnapshot;
    /**
     * 状态:-1删除，0正常
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
     * 关联的品牌id
     */
	private Long BrandId;
    /**
     * 仓库id
     */
	private Long LOWarehouseId;
    /**
     * 实付价
     */
	private Double TotalPay;
    /**
     * 市场价
     */
	private Double TotalMarketPrice;
	private Double MarketPrice;
    /**
     * 总提现金额
     */
	private Double TotalAvailableCommission;
    /**
     * sku 货架位置
     */
	private String Position;
	 /**
     * 供应商ID
     */
	private Long supplierId;


	/**
     * 购买会员套餐类型 0:无,1会员套餐,2至尊套餐
     */
	@TableField("member_package_type")
	private Integer memberPackageType;
	
//	 /**
//     * 退款状态：0(无退款)、1(退款进行中)、2(部分退款完成)、3(全部退款完成)
//     */
//	@TableField("refund_status")
//	private Integer refund_status;
	
//	 /**
//     * 退款完成金额
//     */
//	@TableField("refund_finish_cost")
//	private Integer refundFinishCost;
//	
//	 /**
//     * 退款完成件数
//     */
//	@TableField("refund_finish_count")
//	private Integer refundFinishCount;
//	public Integer getRefund_status() {
//		return refund_status;
//	}
//
//	public void setRefund_status(Integer refund_status) {
//		this.refund_status = refund_status;
//	}
//
//	public Integer getRefundFinishCost() {
//		return refundFinishCost;
//	}
//
//	public void setRefundFinishCost(Integer refundFinishCost) {
//		this.refundFinishCost = refundFinishCost;
//	}
//	public Integer getRefundFinishCount() {
//		return refundFinishCount;
//	}
//
//	public void setRefundFinishCount(Integer refundFinishCount) {
//		this.refundFinishCount = refundFinishCount;
//	}

	public Integer getMemberPackageType() {
		return memberPackageType;
	}

	public void setMemberPackageType(Integer memberPackageType) {
		this.memberPackageType = memberPackageType;
	}

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

	public Long getStoreId() {
		return StoreId;
	}

	public void setStoreId(Long StoreId) {
		this.StoreId = StoreId;
	}

	public Long getProductId() {
		return ProductId;
	}

	public void setProductId(Long ProductId) {
		this.ProductId = ProductId;
	}

	public Long getSkuId() {
		return SkuId;
	}

	public void setSkuId(Long SkuId) {
		this.SkuId = SkuId;
	}

	public Double getTotalMoney() {
		return TotalMoney;
	}

	public void setTotalMoney(Double TotalMoney) {
		this.TotalMoney = TotalMoney;
	}

	public Double getTotalExpressMoney() {
		return TotalExpressMoney;
	}

	public void setTotalExpressMoney(Double TotalExpressMoney) {
		this.TotalExpressMoney = TotalExpressMoney;
	}

	public Double getMoney() {
		return Money;
	}

	public void setMoney(Double Money) {
		this.Money = Money;
	}

	public Double getExpressMoney() {
		return ExpressMoney;
	}

	public void setExpressMoney(Double ExpressMoney) {
		this.ExpressMoney = ExpressMoney;
	}

	public Integer getTotalUnavalCoinUsed() {
		return TotalUnavalCoinUsed;
	}

	public void setTotalUnavalCoinUsed(Integer TotalUnavalCoinUsed) {
		this.TotalUnavalCoinUsed = TotalUnavalCoinUsed;
	}

	public Integer getUnavalCoinUsed() {
		return UnavalCoinUsed;
	}

	public void setUnavalCoinUsed(Integer UnavalCoinUsed) {
		this.UnavalCoinUsed = UnavalCoinUsed;
	}

	public Integer getBuyCount() {
		return BuyCount;
	}

	public void setBuyCount(Integer BuyCount) {
		this.BuyCount = BuyCount;
	}

	public String getSkuSnapshot() {
		return SkuSnapshot;
	}

	public void setSkuSnapshot(String SkuSnapshot) {
		this.SkuSnapshot = SkuSnapshot;
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

	public Long getBrandId() {
		return BrandId;
	}

	public void setBrandId(Long BrandId) {
		this.BrandId = BrandId;
	}

	public Long getLOWarehouseId() {
		return LOWarehouseId;
	}

	public void setLOWarehouseId(Long LOWarehouseId) {
		this.LOWarehouseId = LOWarehouseId;
	}

	public Double getTotalPay() {
		return TotalPay;
	}

	public void setTotalPay(Double TotalPay) {
		this.TotalPay = TotalPay;
	}

	public Double getTotalMarketPrice() {
		return TotalMarketPrice;
	}

	public void setTotalMarketPrice(Double TotalMarketPrice) {
		this.TotalMarketPrice = TotalMarketPrice;
	}

	public Double getMarketPrice() {
		return MarketPrice;
	}

	public void setMarketPrice(Double MarketPrice) {
		this.MarketPrice = MarketPrice;
	}

	public Double getTotalAvailableCommission() {
		return TotalAvailableCommission;
	}

	public void setTotalAvailableCommission(Double TotalAvailableCommission) {
		this.TotalAvailableCommission = TotalAvailableCommission;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String Position) {
		this.Position = Position;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
