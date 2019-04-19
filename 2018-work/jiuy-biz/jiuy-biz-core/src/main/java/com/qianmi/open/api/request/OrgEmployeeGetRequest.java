package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.OrgEmployeeGetResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.org.employee.get request
 *
 * @author auto
 * @since 1.0
 */
public class OrgEmployeeGetRequest implements QianmiRequest<OrgEmployeeGetResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 员工编号
	 */
	private String empId;

	/** 
	 * 需要返回的员工信息字段，可返回emp_id,emp_nick,name,mobile,role_name,status,org_name,org_id,created,sales_num,member_num,sales,members
	 */
	private String fields;

	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpId() {
		return this.empId;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.org.employee.get";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("emp_id", this.empId);
		txtParams.put("fields", this.fields);
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

	public Class<OrgEmployeeGetResponse> getResponseClass() {
		return OrgEmployeeGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(empId, "empId");
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}