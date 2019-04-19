package com.jiuy.core.meta.product;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("jiuy_boutique_product")
public class ProductBoutique implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8693560455371229276L;

	private Long id;
	
	private Long storeId;
	
	private Long productId;

	private String name;
	
	private String brandName;
	
	private String clothesNumber;
	
	private Integer stock;
	
	private Integer vip;

	private Double xprice;

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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getClothesNumber() {
		return clothesNumber;
	}

	public void setClothesNumber(String clothesNumber) {
		this.clothesNumber = clothesNumber;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Double getXprice() {
		return xprice;
	}

	public void setXprice(Double xprice) {
		this.xprice = xprice;
	}

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	@Override
	public String toString() {
		return "ProductBoutique [id=" + id + ", storeId=" + storeId + ", productId=" + productId + ", name=" + name
				+ ", brandName=" + brandName + ", clothesNumber=" + clothesNumber + ", stock=" + stock + ", vip=" + vip
				+ ", xprice=" + xprice + "]";
	}
	
}