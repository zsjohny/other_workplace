package com.yujj.entity.order;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.coupon.RangeType;

public class CouponTemplate implements Serializable{
	
	public CouponTemplate() {
		super();
	}

	public CouponTemplate(Long id, String name, Integer type, Integer rangeType, Long validityStartTime,
			Long validityEndTime, Integer isLimit, Double money, String rangeContent, Integer coexist) {
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
	
	private Integer availableCount;
	
	private Long createTime;
	
	private Long updateTime;
	
	private Integer coexist;
	
	private int exchangeJiuCoinSetting;
	
	private int exchangeJiuCoinCost;
	
	private int exchangeLimitTotalCount;
	
	private int exchangeLimitSingleCount;
	
	private long exchangeStartTime;
	
	private long exchangeEndTime;
	
	private int exchangeCount;
	
	private int promotionJiuCoinSetting;
	
	private int promotionJiuCoin;
	
	private long promotionStartTime;
	
	private long promotionEndTime;
	
	private Double limitMoney;

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

	public Integer getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(Integer availableCount) {
		this.availableCount = availableCount;
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
	
	public Double getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}

	public Object getRangeDetail() {
		if(rangeContent != null) {
			return JSON.parse(rangeContent);
		}
		return null;
	}
	
	public String description() {
		return name + money + "元" + RangeType.getByValue(rangeType).getDescription() + "代金券";
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

	public int getExchangeCount() {
		return exchangeCount;
	}

	public void setExchangeCount(int exchangeCount) {
		this.exchangeCount = exchangeCount;
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

	public int getCurrentJiuCoin() {
		long current = System.currentTimeMillis();
		if (exchangeJiuCoinSetting == 1 && promotionStartTime <= current && current <= promotionEndTime) {
			return promotionJiuCoin;
		}
		return exchangeJiuCoinCost;
	}
	
	public boolean getIsPromotion() {
		long current = System.currentTimeMillis();
		if (exchangeJiuCoinSetting == 1 && promotionStartTime <= current && current <= promotionEndTime) {
			return true;
		}
		return false;
	}

	public int getExchangeStatus() {
		long current = System.currentTimeMillis();
		
		if (exchangeJiuCoinSetting == 1 && exchangeStartTime <= current && current <= exchangeEndTime) {
			return 0;
		} else if(exchangeJiuCoinSetting == 1 && exchangeStartTime > current) {
			return 1;
		}
		return -1;
	}

	public String getExchangeStatusDesc() {
		switch (getExchangeStatus()) {
		case -1:
			return "过期";
		case 0:
			return "正在兑换";
		case 1:
			return "即将兑换";
		}
		
		return "";
	}
	
}
