package com.xiaoluo.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "customers")
@Entity
public class Customer {
	private Integer customerId;
	private String customerName;
	private int customerAge;
	// 多对一
	private Set<Order> order = new HashSet<Order>();

	// 使用了repository查询，关闭懒加载异常
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	public Set<Order> getOrder() {
		return order;
	}

	public void setOrder(Set<Order> order) {
		this.order = order;
	}

	@Column(name = "customer_id")
	@GeneratedValue
	@Id
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Column(name = "customer_name")
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Column(name = "customer_age")
	public int getCustomerAge() {
		return customerAge;
	}

	public void setCustomerAge(int customerAge) {
		this.customerAge = customerAge;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerName=" + customerName + ", customerAge=" + customerAge
				+ ", order=" + order + "]";
	}

}
