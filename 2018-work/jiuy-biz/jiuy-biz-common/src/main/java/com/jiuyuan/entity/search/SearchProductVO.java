/**
 * 
 */
package com.jiuyuan.entity.search;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author DongZhong
 *
 */
public class SearchProductVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1159677832029651691L;

	private Float score;
	private int type;
	
	
	// 搜索结果内容
	private long id;
	private String name;
	private String image;
	private double cash;
	private double currentCash;
    private Boolean isPromotion;
	private double promotionCash;
	private int jiuCoin;
	private double marketPrice;
	private String subscriptLogo;
	
	
	// filter对象
	private long brandId; 
	    
	
	@JsonIgnore
	private long createTime;	
	@JsonIgnore
	private int weight;
	@JsonIgnore
	private long saleTotalCount;
	@JsonIgnore
	private int visitCount;
	
	// 搜索匹配对象
	private String fTitle;

	private String fColor;

	private String fSize;

	private String fSeason;

	private String fTag;

	private String fBrandName;
	@JsonIgnore
	private String fBrandCnName;
	@JsonIgnore
	private float fBrandWeight;

	private String fCategoryName;
	@JsonIgnore
	private float fCategoryWeight;
	
	private String deductDesc;  
	
	private double deductPercent;  
		
	public String getfBrandCnName() {
		return fBrandCnName == null ? "" : fBrandCnName;
	}

	public void setfBrandCnName(String fBrandCnName) {
		this.fBrandCnName = fBrandCnName;
	}

	public float getfBrandWeight() {
		if (fBrandWeight < 1.0f) fBrandWeight = 1.0f;
		return fBrandWeight;
	}

	public void setfBrandWeight(int fBrandWeight) {
		this.fBrandWeight = fBrandWeight;
	}

	public float getfCategoryWeight() {
		if (fCategoryWeight < 1.0f) fCategoryWeight = 1.0f;
		return fCategoryWeight;
	}

	public void setfCategoryWeight(int fCategoryWeight) {
		this.fCategoryWeight = fCategoryWeight;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public String getfCategoryName() {
		return fCategoryName == null ? "" : fCategoryName;
	}

	public void setfCategoryName(String fCategoryName) {
		this.fCategoryName = fCategoryName;
	}
	
	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public int getJiuCoin() {
		return jiuCoin;
	}

	public void setJiuCoin(int jiuCoin) {
		this.jiuCoin = jiuCoin;
	}

	public String toResultString() {
		return "SearchProductVO [score=" + score + "id=" + id + ", name=" + name + ", image=" + image + ", cash=" + cash + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public long getSaleTotalCount() {
		return saleTotalCount;
	}
	public void setSaleTotalCount(long saleTotalCount) {
		this.saleTotalCount = saleTotalCount;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getfTitle() {
		return fTitle;
	}
	public void setfTitle(String fTitle) {
		this.fTitle = fTitle;
	}
	public String getfColor() {
		return fColor;
	}
	public void setfColor(String fColor) {
		this.fColor = fColor;
	}
	public String getfSize() {
		return fSize;
	}
	public void setfSize(String fSize) {
		this.fSize = fSize;
	}
	public String getfSeason() {
		return fSeason == null ? "" : fSeason;
	}
	public void setfSeason(String fSeason) {
		this.fSeason = fSeason;
	}
	public String getfTag() {
		return fTag;
	}
	public void setfTag(String fTag) {
		this.fTag = fTag;
	}
	public String getfBrandName() {
		return (fBrandName == null ? "" : fBrandName) + (fBrandCnName == null ? "" : fBrandCnName);
	}
	public void setfBrandName(String fBrandName) {
		this.fBrandName = fBrandName;
	}
	public double getCash() {
		return cash;
	}
	public void setCash(double cash) {
		this.cash = cash;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String toTestString() {
		return "SearchProductVO [id="+ id + ", score=" + score + ", cash=" + cash + ", createTime=" + createTime + ", weight="
				+ weight + ", saleTotalCount=" + saleTotalCount + ", visitCount=" + visitCount + ", fTitle=" + fTitle
				+ "]";
	}

	public String getSubscriptLogo() {
		return subscriptLogo+"";
	}

	public void setSubscriptLogo(String subscriptLogo) {
		this.subscriptLogo = subscriptLogo;
	}

	public Boolean getIsPromotion() {
		return isPromotion;
	}

	public void setIsPromotion(Boolean isPromotion) {
		this.isPromotion = isPromotion;
	}

	public double getPromotionCash() {
		return promotionCash;
	}

	public void setPromotionCash(double promotionCash) {
		this.promotionCash = promotionCash;
	}

	public double getCurrentCash() {
		return currentCash;
	}

	public void setCurrentCash(double currentCash) {
		this.currentCash = currentCash;
	}

	public String getDeductDesc() {
		return deductDesc;
	}

	public void setDeductDesc(String deductDesc) {
		this.deductDesc = deductDesc;
	}

	public double getDeductPercent() {
		return deductPercent;
	}

	public void setDeductPercent(double deductPercent) {
		this.deductPercent = deductPercent;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}


	
	
}
