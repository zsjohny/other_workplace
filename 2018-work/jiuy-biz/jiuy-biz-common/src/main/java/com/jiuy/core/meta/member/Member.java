package com.jiuy.core.meta.member;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.BaseMeta;

public class Member extends BaseMeta<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2315139384970553628L;
	
	@JsonIgnore
	private long id;
	
	private long userId;
	
	private long createTime;
	
	private long updateTime;
	
	private int distributionPartners;	//邀请人数
	
	private long parentDistribution;	//上级分销
	
	private int distributionStatus;		//分销状态
	
	private String belongStoreName;		//所属门店名称
	
	private long belongStoreId;			//所属门店id

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getUserId() {
		return userId;
	}



	public void setUserId(long userId) {
		this.userId = userId;
	}



	public long getCreateTime() {
		return createTime;
	}



	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}



	public long getUpdateTime() {
		return updateTime;
	}



	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}



	public int getDistributionPartners() {
		return distributionPartners;
	}



	public void setDistributionPartners(int distributionPartners) {
		this.distributionPartners = distributionPartners;
	}



	public long getParentDistribution() {
		return parentDistribution;
	}



	public void setParentDistribution(long parentDistribution) {
		this.parentDistribution = parentDistribution;
	}



	public int getDistributionStatus() {
		return distributionStatus;
	}



	public void setDistributionStatus(int distributionStatus) {
		this.distributionStatus = distributionStatus;
	}



	public String getBelongStoreName() {
		return belongStoreName;
	}



	public void setBelongStoreName(String belongStoreName) {
		this.belongStoreName = belongStoreName;
	}



	public long getBelongStoreId() {
		return belongStoreId;
	}



	public void setBelongStoreId(long belongStoreId) {
		this.belongStoreId = belongStoreId;
	}



	@Override
	public Long getCacheId() {
		return null;
	}
	
}
