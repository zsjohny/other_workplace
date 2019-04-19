package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.OrgEmployeesListResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.org.employees.list request
 *
 * @author auto
 * @since 1.0
 */
public class OrgEmployeesListRequest implements QianmiRequest<OrgEmployeesListResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 查询员工创建的结束时间，格式：yyyy-MM-dd HH:mm:ss
	 */
	private String endCreated;

	/** 
	 * 需要返回的员工信息字段，可返回emp_id,emp_nick,name,mobile,role_name,status,org_name,org_id,created,sales_num,member_num
	 */
	private String fields;

	/** 
	 * 是否销售员 1-是 0-否
	 */
	private String isSaleUser;

	/** 
	 * 部门编号
	 */
	private String orgId;

	/** 
	 * 页码，取值范围：大于等于0的整数，默认0
	 */
	private Integer pageNo;

	/** 
	 * 每页条数，取值范围：大于0的整数，最大100，默认50
	 */
	private Integer pageSize;

	/** 
	 * 岗位编号
	 */
	private String roleId;

	/** 
	 * 查询员工的创建开始时间，格式：yyyy-MM-dd HH:mm:ss
	 */
	private String startCreated;

	/** 
	 * 锁定状态， 0-锁定，1-正常
	 */
	private String status;

	public void setEndCreated(String endCreated) {
		this.endCreated = endCreated;
	}
	public String getEndCreated() {
		return this.endCreated;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setIsSaleUser(String isSaleUser) {
		this.isSaleUser = isSaleUser;
	}
	public String getIsSaleUser() {
		return this.isSaleUser;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgId() {
		return this.orgId;
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

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleId() {
		return this.roleId;
	}

	public void setStartCreated(String startCreated) {
		this.startCreated = startCreated;
	}
	public String getStartCreated() {
		return this.startCreated;
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
		return "qianmi.cloudshop.org.employees.list";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("end_created", this.endCreated);
		txtParams.put("fields", this.fields);
		txtParams.put("is_sale_user", this.isSaleUser);
		txtParams.put("org_id", this.orgId);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("role_id", this.roleId);
		txtParams.put("start_created", this.startCreated);
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

	public Class<OrgEmployeesListResponse> getResponseClass() {
		return OrgEmployeesListResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}