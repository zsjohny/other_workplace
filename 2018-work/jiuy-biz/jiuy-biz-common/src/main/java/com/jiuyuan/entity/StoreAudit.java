package com.jiuyuan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午4:01:32
 * 
 */
@TableName(value="jiuy_store_audit")
public class StoreAudit implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8574631764403116659L;
	
	//STATUS未完成
	public static final int UNFINISHED = -1;

	private long id;
	
	@TableField("StoreId")
	private long storeId;
	
	@TableField("AuditId")
	private long auditId;
	
	@TableField("AuditPerson")
	private String auditPerson;
	
	@TableField("AuditTime")
	private long auditTime;

	private int status;
	
	@TableField("CreateTime")
	private long createTime;
	
	@TableField("UpdateTime")
	private long updateTime;
	
	@TableField("refuseReason")
	private String refuseReason;

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public long getAuditId() {
		return auditId;
	}

	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}

	public String getAuditPerson() {
		return auditPerson;
	}

	public void setAuditPerson(String auditPerson) {
		this.auditPerson = auditPerson;
	}

	public long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(long auditTime) {
		this.auditTime = auditTime;
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





	
}
