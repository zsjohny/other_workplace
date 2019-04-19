package com.jiuyuan.entity.account;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午3:44:28
 * 
 */
public class UserMember implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -961358074425377666L;

	private Long id;
	
	private Long userId;
	
	private Long createTime;
	
	private Long updateTime;
	
	private Integer distributionPartners;
	
	private Long parentDistribution;
	
	private Integer distributionStatus;
	
	private String belongStoreName;
	
	private Long belongStoreId;
	
	private String inviteRelation;
	
	public UserMember() {
		super();
	}

	public UserMember(Long userId, Long createTime, Long updateTime, Integer distributionPartners,
			Long parentDistribution, Integer distributionStatus, String belongStoreName, Long belongStoreId, String inviteRelation) {
		super();
		this.userId = userId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.distributionPartners = distributionPartners;
		this.parentDistribution = parentDistribution;
		this.distributionStatus = distributionStatus;
		this.belongStoreName = belongStoreName;
		this.belongStoreId = belongStoreId;
		this.inviteRelation = inviteRelation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Integer getDistributionPartners() {
		return distributionPartners;
	}

	public void setDistributionPartners(Integer distributionPartners) {
		this.distributionPartners = distributionPartners;
	}

	public Long getParentDistribution() {
		return parentDistribution;
	}

	public void setParentDistribution(Long parentDistribution) {
		this.parentDistribution = parentDistribution;
	}

	public Integer getDistributionStatus() {
		return distributionStatus;
	}

	public void setDistributionStatus(Integer distributionStatus) {
		this.distributionStatus = distributionStatus;
	}

	public String getBelongStoreName() {
		return belongStoreName;
	}

	public void setBelongStoreName(String belongStoreName) {
		this.belongStoreName = belongStoreName;
	}

	public Long getBelongStoreId() {
		return belongStoreId;
	}

	public void setBelongStoreId(Long belongStoreId) {
		this.belongStoreId = belongStoreId;
	}

	public String getInviteRelation() {
		return inviteRelation;
	}

	public void setInviteRelation(String inviteRelation) {
		this.inviteRelation = inviteRelation;
	}

	@Override
	public String toString() {
		return "UserMember [id=" + id + ", userId=" + userId + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", distributionPartners=" + distributionPartners + ", parentDistribution="
				+ parentDistribution + ", distributionStatus=" + distributionStatus + ", belongStoreName="
				+ belongStoreName + ", belongStoreId=" + belongStoreId + ", inviteRelation=" + inviteRelation + "]";
	}
	
}
