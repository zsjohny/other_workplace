package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
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
 * @since 2017-11-09
 */
@TableName("store_OrderProduct")
public class StoreOrderProduct extends Model<StoreOrderProduct> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 用户id
     */
	private Long StoreId;
    /**
     * 对应Product表的id
     */
	private Long ProductId;
    /**
     * 新订单表OrderNo
     */
	private Long OrderNo;
    /**
     * 订单金额总价，不包含邮费
     */
	private Double TotalMoney;
    /**
     * 商品单价，不包含邮费
     */
	private Double Money;
    /**
     * 订购数量
     */
	private Integer BuyCount;
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
	private Long WarehouseId;
    /**
     * 市场价
     */
	private Double TotalMarketPrice;
	private Double MarketPrice;
    /**
     * 提成金额
     */
	private Double Commission;
    /**
     * 总提成金额
     */
	private Double TotalCommission;
    /**
     * 商品图片
     */
	private String Image;
    /**
     * 品牌logo
     */
	private String BrandLogo;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
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

	public Long getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(Long OrderNo) {
		this.OrderNo = OrderNo;
	}

	public Double getTotalMoney() {
		return TotalMoney;
	}

	public void setTotalMoney(Double TotalMoney) {
		this.TotalMoney = TotalMoney;
	}

	public Double getMoney() {
		return Money;
	}

	public void setMoney(Double Money) {
		this.Money = Money;
	}

	public Integer getBuyCount() {
		return BuyCount;
	}

	public void setBuyCount(Integer BuyCount) {
		this.BuyCount = BuyCount;
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

	public Long getWarehouseId() {
		return WarehouseId;
	}

	public void setWarehouseId(Long WarehouseId) {
		this.WarehouseId = WarehouseId;
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

	public Double getCommission() {
		return Commission;
	}

	public void setCommission(Double Commission) {
		this.Commission = Commission;
	}

	public Double getTotalCommission() {
		return TotalCommission;
	}

	public void setTotalCommission(Double TotalCommission) {
		this.TotalCommission = TotalCommission;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String Image) {
		this.Image = Image;
	}

	public String getBrandLogo() {
		return BrandLogo;
	}

	public void setBrandLogo(String BrandLogo) {
		this.BrandLogo = BrandLogo;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
