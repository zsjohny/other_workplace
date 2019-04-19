package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Employee;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.org.employees.list response.
 *
 * @author auto
 * @since 2.0
 */
public class OrgEmployeesListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 员工列表
	 */
	@ApiListField("employees")
	@ApiField("employee")
	private List<Employee> employees;

	/** 
	 * 返回的员工记录条数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	public List<Employee> getEmployees( ) {
		return this.employees;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
