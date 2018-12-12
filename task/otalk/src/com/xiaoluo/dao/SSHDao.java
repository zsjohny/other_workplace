package com.xiaoluo.dao;

import com.xiaoluo.entity.Customer;

import java.util.List;

public interface SSHDao {
	void createCustomerByCusual(Customer customer);

	List<Customer> queryCustomer();
}
