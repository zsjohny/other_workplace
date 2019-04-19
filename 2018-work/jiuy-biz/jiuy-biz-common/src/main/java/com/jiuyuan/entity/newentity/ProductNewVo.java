package com.jiuyuan.entity.newentity;

import java.util.List;

/**
 * <p>
 * 确认订单时使用
 * <p>
 * 包含字段:
 * <p>
 * {@link BrandNew}
 * <p>
 * {@link ProductNew}
 * <p>
 * {@link UserNew} 供应商表
 * <p>
 * {@link List<ProductSkuNewVo>} ProductSkuNewVo 拥有额外属性: sku购买数
 * <p>
 * buyCount 该商品购买总数(sku数量和)
 */
public class ProductNewVo {
	
	/**
	 * 品牌ID
	 */
	private long brandId;
	/**
	 * 商品品牌
	 */
	private BrandNew brand;
	/**
	 * 订单单价
	 * 说明：会根据当前订单购买数量和阶梯价进行计算商品单价
	 */
	private double orderUnitPrice;
	/**
	 * 购买数量
	 */
	private int buyCount;
	/**
	 * 产品ID
	 */
	private long productId;
    /**
     * 商品对象
     */
	private ProductNew product;
    /**
     * 商品对象
     */
	private UserNew supplier;
	
	/**
     * 商品SKU列表
     */
	private List<ProductSkuNewVo> productSkuNewVoList;
	
	public ProductNewVo(long productId,ProductNew product,long brandId,BrandNew brand,UserNew supplier,List<ProductSkuNewVo> productSkuNewVoList,int buyCount){
		this.brandId = brandId;
		this.brand = brand;
		this.productId = productId;
		this.product = product;
		this.productSkuNewVoList = productSkuNewVoList;
		this.buyCount = buyCount;
		this.supplier = supplier;
		
	}
	/**
	 * 增加购买数量
	 */
	public void addBuyCount(int addBuyCount){
		this.buyCount = this.buyCount + addBuyCount;
	}
	
	public int getBuyCount() {
		return buyCount;
	}
	public long getProductId() {
		return productId;
	}
	public ProductNew getProduct() {
		return product;
	}
	
	public long getBrandId() {
		return brandId;
	}
	
	public BrandNew getBrand() {
		return brand;
	}
	public double getOrderUnitPrice() {
		return orderUnitPrice;
	}
	
	public List<ProductSkuNewVo> getProductSkuNewVoList() {
		return productSkuNewVoList;
	}
	public void setProductSkuNewVoList(List<ProductSkuNewVo> productSkuNewVoList) {
		this.productSkuNewVoList = productSkuNewVoList;
	}
	public UserNew getSupplier() {
		return supplier;
	}
	public void setOrderUnitPrice(double orderUnitPrice) {
		this.orderUnitPrice = orderUnitPrice;
	}
	
	
	
   
}
