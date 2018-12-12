package com.xiaoluo.service;

import com.xiaoluo.entity.Customer;

import java.util.List;

public interface SSHService {
	void createCustomerByCusual(Customer customer);

	List<Customer> queryCustomer();
}
