package com.xiaoluo.dao.impl;

import com.xiaoluo.dao.SSHDao;
import com.xiaoluo.entity.Customer;
import com.xiaoluo.repository.SpringCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
// 新的一个事务开启，原来的事务会保持
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SSHDaoImpl implements SSHDao {
	@Autowired
	private SpringCrudRepository springCrudRepository;

	public void createCustomerByCusual(Customer customer) {

		springCrudRepository.save(customer);
	}

	@Override
	public List<Customer> queryCustomer() {

		return (List<Customer>) springCrudRepository.findAll();

	}

}
