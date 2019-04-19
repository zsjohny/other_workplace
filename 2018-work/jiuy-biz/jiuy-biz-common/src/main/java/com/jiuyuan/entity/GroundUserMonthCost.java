package com.jiuyuan.entity;
/**
 * 
 * 地推提现自动打款:地推人员ID，上月总收入
 *
 */
public class GroundUserMonthCost {
	private Long groundUserId;
	private Double costCount;
	public Long getGroundUserId() {
		return groundUserId;
	}
	public void setGroundUserId(Long groundUserId) {
		this.groundUserId = groundUserId;
	}
	public Double getCostCount() {
		return costCount;
	}
	public void setCostCount(Double costCount) {
		this.costCount = costCount;
	}
}
