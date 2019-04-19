package com.jiuyuan.constant.coupon;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.coupon.RangeType;

public class StoreCouponTemplate implements Serializable{
	
	public StoreCouponTemplate() {
		super();
	}

	public StoreCouponTemplate(Long id, String name, Integer type, Integer rangeType, Long validityStartTime,
			Long validityEndTime, Integer isLimit, Double money, String rangeContent, Integer coexist, Double limitMoney) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.rangeType = rangeType;
		this.validityStartTime = validityStartTime;
		this.validityEndTime = validityEndTime;
		this.isLimit = isLimit;
		this.money = money;
		this.rangeContent = rangeContent;
		this.coexist = coexist;
		this.limitMoney = limitMoney;
	}
	
	public StoreCouponTemplate(Long id, String name, Integer type, Double money, Integer rangeType, String rangeContent,
			Long validityStartTime, Long validityEndTime, Integer isLimit,
			Integer coexist, Double limitMoney,
			int exchangeJiuCoinSetting, int exchangeJiuCoinCost, int exchangeLimitTotalCount,
			int exchangeLimitSingleCount, long exchangeStartTime, long exchangeEndTime, int promotionJiuCoinSetting,
			int promotionJiuCoin, long promotionStartTime, long promotionEndTime) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.money = money;
		this.rangeType = rangeType;
		this.rangeContent = rangeContent;
		this.validityStartTime = validityStartTime;
		this.validityEndTime = validityEndTime;
		this.isLimit = isLimit;
		this.coexist = coexist;
		this.limitMoney = limitMoney;
		this.exchangeJiuCoinSetting = exchangeJiuCoinSetting;
		this.exchangeJiuCoinCost = exchangeJiuCoinCost;
		this.exchangeLimitTotalCount = exchangeLimitTotalCount;
		this.exchangeLimitSingleCount = exchangeLimitSingleCount;
		this.exchangeStartTime = exchangeStartTime;
		this.exchangeEndTime = exchangeEndTime;
		this.promotionJiuCoinSetting = promotionJiuCoinSetting;
		this.promotionJiuCoin = promotionJiuCoin;
		this.promotionStartTime = promotionStartTime;
		this.promotionEndTime = promotionEndTime;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 8181147853985198258L;

	private Long id;
	
	private String name;
	
	private Integer type;
	
	private Double money;
	
	private Integer rangeType;
	
	private String rangeContent;
	
	private Long validityStartTime;
	
	private Long validityEndTime;
	
	private Integer isLimit;
	
	private Integer publishCount;
	
	private Integer grantCount;
	
	private Long createTime;
	
	private Long updateTime;
	
	private Integer coexist;
	
	private Integer status;
	
	private Double limitMoney;
	
	private int exchangeJiuCoinSetting;
	
	private int exchangeJiuCoinCost;
	
	private int exchangeLimitTotalCount;
	
	private int availableCount;
	
	private int exchangeLimitSingleCount;
	
	private long exchangeStartTime;
	
	private long exchangeEndTime;
	
	private int promotionJiuCoinSetting;
	
	private int promotionJiuCoin;
	
	private long promotionStartTime;
	
	private long promotionEndTime;
	
	private String rangeTypeIds;
	
	private String rangeTypeNames;
	
	public String getRangeTypeNames() {
		return rangeTypeNames;
	}

	public void setRangeTypeNames(String rangeTypeNames) {
		this.rangeTypeNames = rangeTypeNames;
	}

	public String getRangeTypeIds() {
		return rangeTypeIds;
	}

	public void setRangeTypeIds(String rangeTypeIds) {
		this.rangeTypeIds = rangeTypeIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getRangeType() {
		return rangeType;
	}

	public void setRangeType(Integer rangeType) {
		this.rangeType = rangeType;
	}

	public String getRangeContent() {
		return rangeContent;
	}

	public void setRangeContent(String rangeContent) {
		this.rangeContent = rangeContent;
	}

	public Long getValidityStartTime() {
		return validityStartTime;
	}

	public void setValidityStartTime(Long validityStartTime) {
		this.validityStartTime = validityStartTime;
	}

	public Long getValidityEndTime() {
		return validityEndTime;
	}

	public void setValidityEndTime(Long validityEndTime) {
		this.validityEndTime = validityEndTime;
	}

	public Integer getIsLimit() {
		return isLimit;
	}

	public void setIsLimit(Integer isLimit) {
		this.isLimit = isLimit;
	}

	public Integer getPublishCount() {
		return publishCount;
	}

	public void setPublishCount(Integer publishCount) {
		this.publishCount = publishCount;
	}

	public Integer getGrantCount() {
		return grantCount;
	}

	public void setGrantCount(Integer grantCount) {
		this.grantCount = grantCount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getCoexist() {
		return coexist;
	}

	public void setCoexist(Integer coexist) {
		this.coexist = coexist;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Object getRangeDetail() {
		if(rangeContent != null) {
			return JSON.parse(rangeContent);
		}
		return null;
	}
	
	public String getRangTypeName() {
		RangeType rt = RangeType.getByValue(rangeType);
		if (rt != null) {
			return rt.getDescription();
		}
		return "";
	}

	public Double getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}

	public String getTemplateDescription(){
//		周年庆5元通用代金券（ID:12）
		return name + money + "元" + RangeType.getByValue(rangeType).getDescription() + "优惠券(ID:" + id + ")";
	}

	public int getExchangeJiuCoinSetting() {
		return exchangeJiuCoinSetting;
	}

	public void setExchangeJiuCoinSetting(int exchangeJiuCoinSetting) {
		this.exchangeJiuCoinSetting = exchangeJiuCoinSetting;
	}

	public int getExchangeJiuCoinCost() {
		return exchangeJiuCoinCost;
	}

	public void setExchangeJiuCoinCost(int exchangeJiuCoinCost) {
		this.exchangeJiuCoinCost = exchangeJiuCoinCost;
	}

	public int getExchangeLimitTotalCount() {
		return exchangeLimitTotalCount;
	}

	public void setExchangeLimitTotalCount(int exchangeLimitTotalCount) {
		this.exchangeLimitTotalCount = exchangeLimitTotalCount;
	}

	public int getExchangeLimitSingleCount() {
		return exchangeLimitSingleCount;
	}

	public void setExchangeLimitSingleCount(int exchangeLimitSingleCount) {
		this.exchangeLimitSingleCount = exchangeLimitSingleCount;
	}

	public long getExchangeStartTime() {
		return exchangeStartTime;
	}

	public void setExchangeStartTime(long exchangeStartTime) {
		this.exchangeStartTime = exchangeStartTime;
	}

	public long getExchangeEndTime() {
		return exchangeEndTime;
	}

	public void setExchangeEndTime(long exchangeEndTime) {
		this.exchangeEndTime = exchangeEndTime;
	}

	public int getPromotionJiuCoinSetting() {
		return promotionJiuCoinSetting;
	}

	public void setPromotionJiuCoinSetting(int promotionJiuCoinSetting) {
		this.promotionJiuCoinSetting = promotionJiuCoinSetting;
	}

	public int getPromotionJiuCoin() {
		return promotionJiuCoin;
	}

	public void setPromotionJiuCoin(int promotionJiuCoin) {
		this.promotionJiuCoin = promotionJiuCoin;
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

	
	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	@Override
	public String toString() {
		return "StoreCouponTemplate [id=" + id + ", name=" + name + ", type=" + type + ", money=" + money
				+ ", rangeType=" + rangeType + ", rangeContent=" + rangeContent + ", validityStartTime="
				+ validityStartTime + ", validityEndTime=" + validityEndTime + ", isLimit=" + isLimit
				+ ", publishCount=" + publishCount + ", grantCount=" + grantCount + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", coexist=" + coexist + ", status=" + status + ", limitMoney="
				+ limitMoney + ", exchangeJiuCoinSetting=" + exchangeJiuCoinSetting + ", exchangeJiuCoinCost="
				+ exchangeJiuCoinCost + ", exchangeLimitTotalCount=" + exchangeLimitTotalCount
				+ ", exchangeLimitSingleCount=" + exchangeLimitSingleCount + ", exchangeStartTime=" + exchangeStartTime
				+ ", exchangeEndTime=" + exchangeEndTime + ", promotionJiuCoinSetting=" + promotionJiuCoinSetting
				+ ", promotionJiuCoin=" + promotionJiuCoin + ", promotionStartTime=" + promotionStartTime
				+ ", promotionEndTime=" + promotionEndTime + "]";
	}

}
