package com.jiuy.core.meta.operationLog;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class OperationLog extends BaseMeta<Long> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7786050058700909215L;

	private long id;
	
	private String workNo;
	
	private long authorityId;
	
	private long functionName;
	
	private long content;
	
	private long createTime;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(long authorityId) {
		this.authorityId = authorityId;
	}

	public long getFunctionName() {
		return functionName;
	}

	public void setFunctionName(long functionName) {
		this.functionName = functionName;
	}

	public long getContent() {
		return content;
	}

	public void setContent(long content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	@Override
	public Long getCacheId() {
		return null;
	}

}
