package com.jiuy.core.meta.member;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
@TableName(value="jiuy_store_audit_refuse")
public class RefuseReason implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4934038889893174364L;
	
	@TableId
	private long id;
	
	@TableField("refuseReason")
	private String refuseReason;
	
	@TableField("CreateTime")
	private Long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
