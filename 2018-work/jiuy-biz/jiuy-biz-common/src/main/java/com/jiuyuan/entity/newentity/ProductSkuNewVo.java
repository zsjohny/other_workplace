package com.jiuyuan.entity.newentity;

import java.util.List;

/**
 * 商品SKU对象
 * 确认订单时使用
 */
public class ProductSkuNewVo {
	
	/**
	 * 购买数量
	 */
	private int buyCount;
	/**
     * 商品SKU列表
     */
	private ProductSkuNew productSkuNew;
	
	public  ProductSkuNewVo(int buyCount,ProductSkuNew productSkuNew){
		this.productSkuNew = productSkuNew;
		this.buyCount = buyCount;
	}
	
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public ProductSkuNew getProductSkuNew() {
		return productSkuNew;
	}
	public void setProductSkuNew(ProductSkuNew productSkuNew) {
		this.productSkuNew = productSkuNew;
	}
	
	
}
