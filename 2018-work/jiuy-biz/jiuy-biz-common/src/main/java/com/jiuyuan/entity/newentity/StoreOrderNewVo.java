package com.jiuyuan.entity.newentity;

import com.jiuyuan.common.CouponRbRef;

import java.util.List;

/**
 * <p>
 * 确认订单时使用
 */
public class StoreOrderNewVo {
	
//	/**
//	 * 订单编号
//	 */
//	private Long orderNo;
	
	
	 public static final int buyType_common = 0;//购买方式：0常规购买、1混批购买、2互动购买
	 public static final int buyType_WholesaleLimit = 1;//购买方式：0常规购买、1混批购买、2互动购买
	 public static final int buyType_activity = 2;//购买方式：0常规购买、1混批购买、2互动购买
	
	/**
	 * 供应商
	 */
	private UserNew supplier;
	/**
	 * 品牌
	 */
	private BrandNew brand;
	/**
	 * 使用优惠券ID
	 */
	private CouponRbRef coupon;
	/**
//	 * 收货地址ID 
//	 */
//	private AddressNew address;
	
	/**
     * 总购买件数
     */
	private Integer TotalBuyCount;
	
	/**
     * 商品列表
     */
	private List<ProductNewVo> ProductNewVoList;
	
	/**
     * 订单对象
     */
	private StoreOrderNew order;
	
	/**
	 * 配送方式名称
	 */
	private String deliveryTypeName = "快递";
	
	/**
	 * 是否免邮，0(不免邮)、1(免邮)
	 */
	private int freePostage = 1;
	/**
	 * 是否免邮名称，0(不免邮)、1(免邮)
	 */
	private String freePostageName = "免邮";
	/**
	 * 优惠券抵扣价格（优惠券抵扣）
	 */
	private Double couponOffsetPrice;
	/**
	 * 邮费（该订单邮费）
	 */
	private Double postage;
	/**
	 * 商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费
	 * 订单金额原价总价，不包含邮费
	 */
	private Double totalProductPrice;
	
	/**
	 * 优惠后商品总价格（商品总价格 -优惠券价格） 
	 */
	private Double totalProductPriceAfterCoupon;
	
	
	/**
	 * 订单支付价格（优惠后商品总价格+邮费）
	 */
	private Double payPrice ;
	
	/**
	 * 平台优惠
	 */
	private Double platformTotalPreferential;
	
	/**
	 * 商家店铺总优惠（包含商家优惠和商家改价差额）
	 */
	private Double supplierTotalPreferential;
	
	/**
	 * 商家优惠（不包含改价部分）
	 */
	private Double supplierPreferential;
	
	/**
	 * 商家改价差额
	 */
	private Double supplierChangePrice;
	
	/**
	 * 订单金额优惠后原始待付款价格，不包含邮费
	 */
	private Double originalPrice;
	
//	/**
//	 * 购买方式：0常规购买、1混批购买、2活动购买
//	 */
//	private Integer buyType;
	
	/**
	 * 是否符合混批限制：0不符合、1符合
	 */
	private int matchWholesaleLimit;

	public List<ProductNewVo> getProductNewVoList() {
		return ProductNewVoList;
	}

	public void setProductNewVoList(List<ProductNewVo> productNewVoList) {
		ProductNewVoList = productNewVoList;
	}

	public BrandNew getBrand() {
		return brand;
	}

	public void setBrand(BrandNew brand) {
		this.brand = brand;
	}

	public String getDeliveryTypeName() {
		return deliveryTypeName;
	}

	public void setDeliveryTypeName(String deliveryTypeName) {
		this.deliveryTypeName = deliveryTypeName;
	}

	public int getFreePostage() {
		return freePostage;
	}

	public void setFreePostage(int freePostage) {
		this.freePostage = freePostage;
	}

	public String getFreePostageName() {
		return freePostageName;
	}

	public void setFreePostageName(String freePostageName) {
		this.freePostageName = freePostageName;
	}

	public Double getPostage() {
		return postage;
	}

	public void setPostage(Double postage) {
		this.postage = postage;
	}
	
	public Double getTotalProductPrice() {
		return totalProductPrice;
	}
	public void setTotalProductPrice(Double totalProductPrice) {
		this.totalProductPrice = totalProductPrice;
	}


	public CouponRbRef getCoupon() {
		return coupon;
	}

	public void setCoupon(CouponRbRef coupon) {
		this.coupon = coupon;
	}

	public UserNew getSupplier() {
		return supplier;
	}

	public void setSupplier(UserNew supplier) {
		this.supplier = supplier;
	}

	public Double getTotalProductPriceAfterCoupon() {
		return totalProductPriceAfterCoupon;
	}

	public void setTotalProductPriceAfterCoupon(Double totalProductPriceAfterCoupon) {
		this.totalProductPriceAfterCoupon = totalProductPriceAfterCoupon;
	}

	public Double getCouponOffsetPrice() {
		return couponOffsetPrice;
	}

	public void setCouponOffsetPrice(Double couponOffsetPrice) {
		this.couponOffsetPrice = couponOffsetPrice;
	}

//	public AddressNew getAddress() {
//		return address;
//	}
//
//	public void setAddress(AddressNew address) {
//		this.address = address;
//	}

	public Integer getTotalBuyCount() {
		return TotalBuyCount;
	}

	public void setTotalBuyCount(Integer totalBuyCount) {
		TotalBuyCount = totalBuyCount;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}
	public Double getPayPrice() {
		return  payPrice;
	}

	public StoreOrderNew getOrder() {
		return order;
	}

	public void setOrder(StoreOrderNew order) {
		this.order = order;
	}

	public Double getPlatformTotalPreferential() {
		return platformTotalPreferential;
	}

	public void setPlatformTotalPreferential(Double platformTotalPreferential) {
		this.platformTotalPreferential = platformTotalPreferential;
	}

	public Double getSupplierTotalPreferential() {
		return supplierTotalPreferential;
	}

	public void setSupplierTotalPreferential(Double supplierTotalPreferential) {
		this.supplierTotalPreferential = supplierTotalPreferential;
	}

	public Double getSupplierPreferential() {
		return supplierPreferential;
	}

	public void setSupplierPreferential(Double supplierPreferential) {
		this.supplierPreferential = supplierPreferential;
	}

	public Double getSupplierChangePrice() {
		return supplierChangePrice;
	}

	public void setSupplierChangePrice(Double supplierChangePrice) {
		this.supplierChangePrice = supplierChangePrice;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

//	public Integer getBuyType() {
//		return buyType;
//	}
//
//	public void setBuyType(Integer buyType) {
//		this.buyType = buyType;
//	}

	public int getMatchWholesaleLimit() {
		return matchWholesaleLimit;
	}

	public void setMatchWholesaleLimit(int matchWholesaleLimit) {
		this.matchWholesaleLimit = matchWholesaleLimit;
	}

}