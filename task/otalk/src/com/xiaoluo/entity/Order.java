package com.xiaoluo.entity;

import javax.persistence.*;

@Table(name = "orders")
@Entity
public class Order {
	private Integer orderId;
	private String orderName;
	private int orderAge;

	private Customer customer;

	// 制定外键的的生成方式--
	@JoinColumn(name = "customer_id")
	@ManyToOne
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "order_Id")
	@GeneratedValue
	@Id
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	// 限制长度，和增加表的名字
	@Column(name = "order_name", length = 50)
	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	@Column(name = "order_age")
	public int getOrderAge() {
		return orderAge;
	}

	public void setOrderAge(int orderAge) {
		this.orderAge = orderAge;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderName=" + orderName + ", orderAge=" + orderAge + ", customer="
				+ customer + "]";
	}

}
