package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Employee;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.org.employee.get response.
 *
 * @author auto
 * @since 2.0
 */
public class OrgEmployeeGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 单个员工详细信息
	 */
	@ApiField("employee")
	private Employee employee;

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Employee getEmployee( ) {
		return this.employee;
	}

}
