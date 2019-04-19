package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.MembersListResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.members.list request
 *
 * @author auto
 * @since 1.0
 */
public class MembersListRequest implements QianmiRequest<MembersListResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 审核状态，0：待审核，1：审核通过，-1：审核不通过，默认值1
	 */
	private String auditStatus;

	/** 
	 * 返回的字段列表
	 */
	private String fields;

	/** 
	 * 会员等级编号
	 */
	private String levelId;

	/** 
	 * 锁定状态，0：锁定，1：正常，默认值1
	 */
	private String lockStatus;

	/** 
	 * 会员类型，1：个人会员，4：分销商，不传则查询个人会员和分销商
	 */
	private String memberType;

	/** 
	 * 页码，取大于等于0的整数，默认值0
	 */
	private Integer pageNo;

	/** 
	 * 每页条数，取大于0的整数，默认20，最大值20
	 */
	private Integer pageSize;

	/** 
	 * 用户状态，1：正常，2：删除，默认值1
	 */
	private String status;

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getLevelId() {
		return this.levelId;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	public String getLockStatus() {
		return this.lockStatus;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMemberType() {
		return this.memberType;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageNo() {
		return this.pageNo;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return this.status;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.members.list";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("audit_status", this.auditStatus);
		txtParams.put("fields", this.fields);
		txtParams.put("level_id", this.levelId);
		txtParams.put("lock_status", this.lockStatus);
		txtParams.put("member_type", this.memberType);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("status", this.status);
		if(udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new QianmiHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<MembersListResponse> getResponseClass() {
		return MembersListResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}