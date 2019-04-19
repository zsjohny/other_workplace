package com.jiuy.core.meta.coupon;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class StoreCouponTemplateVO extends CouponTemplate{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2921122830359948880L;
	
    private int expiredCount;

    private int deletedCount;

    private int usedCount;
	
	private int availableCount;
    
	private List<JSONObject> jsonObjects;
	
	private String rangeDescription;

	public int getExpiredCount() {
		return expiredCount;
	}

	public void setExpiredCount(int expiredCount) {
		this.expiredCount = expiredCount;
	}

	public int getDeletedCount() {
		return deletedCount;
	}

	public void setDeletedCount(int deletedCount) {
		this.deletedCount = deletedCount;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public List<JSONObject> getJsonObjects() {
		return jsonObjects;
	}

	public void setJsonObjects(List<JSONObject> jsonObjects) {
		this.jsonObjects = jsonObjects;
	}

	public String getRangeDescription() {
		return rangeDescription;
	}

	public void setRangeDescription(String rangeDescription) {
		this.rangeDescription = rangeDescription;
	}
	

}
