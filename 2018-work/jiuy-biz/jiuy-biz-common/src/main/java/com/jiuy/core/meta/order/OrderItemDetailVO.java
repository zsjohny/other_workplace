package com.jiuy.core.meta.order;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class OrderItemDetailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3911209222745226941L;

	private String previewImg;
	
	private long skuNo;
	
	private String productName;
	
	private String clothesNum;
	
	private String color;
	
	private String size;
	
	private int jiuCoin;
	
	private String brandName;
	
	private double totalMoney;
	
	private int buyCount;

	private double totalPay;
	
	private Long productId;
	
	private String code;	//新增统计识别码

	public String getPreviewImg() {
		if(StringUtils.equals("", previewImg) || StringUtils.equals(null, previewImg)) {
			previewImg = "[]";
		}
		
		JSONArray jsonArray = JSON.parseArray(previewImg);
		if(jsonArray.size() < 1) {
			return "";
		}
		
		return jsonArray.getString(0);
	}

	public void setPreviewImg(String previewImg) {
		this.previewImg = previewImg;
	}

	public long getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(long skuNo) {
		this.skuNo = skuNo;
	}

	public String getClothesNum() {
		return clothesNum;
	}

	public void setClothesNum(String clothesNum) {
		this.clothesNum = clothesNum;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getJiuCoin() {
		return jiuCoin;
	}

	public void setJiuCoin(int jiuCoin) {
		this.jiuCoin = jiuCoin;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}
	
	public double getSinglePay() {
		return totalPay / buyCount;
	}

//	总优惠金额
	public double getDiscount() {
		return totalMoney - totalPay;
	}
	
	public double getProductPrice() {
		return totalMoney / buyCount;
	}
	
	public double getSingleDiscount() {
		return (totalMoney - totalPay) / buyCount;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
}
