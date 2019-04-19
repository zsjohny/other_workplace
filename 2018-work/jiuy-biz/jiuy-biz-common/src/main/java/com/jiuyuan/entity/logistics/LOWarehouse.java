package com.jiuyuan.entity.logistics;

import java.io.Serializable;
/**
 * CREATE TABLE `yjj_LOWarehouse` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL COMMENT '仓库名字',
  `DeliveryLocation` bigint(20) NOT NULL COMMENT '发货地',
  `Description` text,
  `IsFree` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否免邮 0:否 1:是',
  `FreeCount` int(11) DEFAULT '0' COMMENT '几件免邮，在IsFree为1的情况下有效\n',
  `Status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:正常， -1：删除',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间 ',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `RemainCountSyncSet` tinyint(4) NOT NULL DEFAULT '0' COMMENT '库存同步设置 0:人工 1:同步ERP\n',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COMMENT='物流仓库';
 * @author Administrator
 *
 */
public class LOWarehouse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1795218336842741465L;

	private long id;
	
	private String name;
	
	private int deliveryLocation;
	
	private String description;
	
	private int isFree;
	
	private int freeCount;
	
	private int status;
	
	private long createTime;
	
	private long updateTime;
	
	private int remainCountSyncSet;
	
	private int type;
	
	private String cityName;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	private String provinceName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDeliveryLocation() {
		return deliveryLocation;
	}

	public void setDeliveryLocation(int deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIsFree() {
		return isFree;
	}

	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}

	public int getFreeCount() {
		return freeCount;
	}

	public void setFreeCount(int freeCount) {
		this.freeCount = freeCount;
	}

	public int getRemainCountSyncSet() {
		return remainCountSyncSet;
	}

	public void setRemainCountSyncSet(int remainCountSyncSet) {
		this.remainCountSyncSet = remainCountSyncSet;
	}


}
