package com.jiuyuan.entity.product;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.jiuyuan.constant.order.OrderConstants;

public class ProductWindow implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -11726053741823358L;

	private long id;
	private String clothesNum;
	private String name;
	private String promotionImage;
	private long brandId;
	private String brandIdentity;
	private int marketPrice;
	private int marketPriceMin;
	private int marketPriceMax;
	private long price;
	private String summaryImages;
	private int remainCount;
	
    private double promotionCash;    
    private long promotionStartTime;    
    private long promotionEndTime;
	
    /*app 1.7*/
    private double cash;    
    private int jiuCoin;  
	
	public String getPromotionImage() {
		if (promotionImage == null || StringUtils.equals("", promotionImage)) {
			return getDisplayImg();
		}
		return promotionImage;
	}
	public void setPromotionImage(String promotionImage) {
		this.promotionImage = promotionImage;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getClothesNum() {
		return clothesNum;
	}
	public void setClothesNum(String clothesNum) {
		this.clothesNum = clothesNum;
	}
	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	public String getBrandIdentity() {
		return brandIdentity;
	}
	public void setBrandIdentity(String brandIdentity) {
		this.brandIdentity = brandIdentity;
	}
	public int getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(int marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getMarketPriceMin() {
		return marketPriceMin;
	}
	public void setMarketPriceMin(int marketPriceMin) {
		this.marketPriceMin = marketPriceMin;
	}
	public int getMarketPriceMax() {
		return marketPriceMax;
	}
	public void setMarketPriceMax(int marketPriceMax) {
		this.marketPriceMax = marketPriceMax;
	}
	public void setSummaryImages(String summaryImages) {
		this.summaryImages = summaryImages;
	}
	public long getPayAmountInCents() {
		return price * OrderConstants.PAY_CENTS_PER_UNIT;
	}
	public Object getDefaultImg() {
		if (promotionImage != null && !StringUtils.equals("", promotionImage)) {
			return promotionImage;
		}
		JSONArray jsonArray = new JSONArray();
		if(StringUtils.isNotBlank(this.summaryImages)) {
			jsonArray = JSONArray.parseArray(this.summaryImages);
		}
		if(jsonArray.size() > 0) {
			return jsonArray.get(0);
		}
		return null;
	}
	
	public String getDisplayImg() {
		JSONArray jsonArray = new JSONArray();
		if(StringUtils.isNotBlank(this.summaryImages)) {
			jsonArray = JSONArray.parseArray(this.summaryImages);
		}
		if(jsonArray.size() > 0) {
			return jsonArray.get(0).toString();
		}
		return "";
	}
	
	public int getRemainCount() {
		return remainCount;
	}
	public void setRemainCount(int remainCount) {
		this.remainCount = remainCount;
	}
	public String getSummaryImages() {
		return summaryImages;
	}
	public double getCash() {
		return cash;
	}
	public void setCash(double cash) {
		this.cash = cash;
	}
	public int getJiuCoin() {
		return jiuCoin;
	}
	
	public void setJiuCoin(int jiuCoin) {
		this.jiuCoin = jiuCoin;
	}
	
	
	public double getPromotionCash() {
		return promotionCash;
	}
	public void setPromotionCash(double promotionCash) {
		this.promotionCash = promotionCash;
	}
	public long getPromotionStartTime() {
		return promotionStartTime;
	}
	public void setPromotionStartTime(long promotionStartTime) {
		this.promotionStartTime = promotionStartTime;
	}
	public long getPromotionEndTime() {
		return promotionEndTime;
	}
	public void setPromotionEndTime(long promotionEndTime) {
		this.promotionEndTime = promotionEndTime;
	}
	@Override
	public String toString() {
		return "ProductWindow [id=" + id + ", clothesNum=" + clothesNum + ", name=" + name + ", promotionImage="
				+ promotionImage + ", brandId=" + brandId + ", brandIdentity=" + brandIdentity + ", marketPrice="
				+ marketPrice + ", marketPriceMin=" + marketPriceMin + ", marketPriceMax=" + marketPriceMax + ", price="
				+ price + ", summaryImages=" + summaryImages + ", remainCount=" + remainCount + ", cash=" + cash
				+ ", jiuCoin=" + jiuCoin + "]";
	}
}
