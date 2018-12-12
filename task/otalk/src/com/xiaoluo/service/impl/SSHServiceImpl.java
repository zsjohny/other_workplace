package com.xiaoluo.service.impl;

import com.xiaoluo.dao.SSHDao;
import com.xiaoluo.entity.Customer;
import com.xiaoluo.service.SSHService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class SSHServiceImpl implements SSHService {
	@Resource
	private SSHDao sshDao;

	@Override
	public void createCustomerByCusual(Customer customer) {
		sshDao.createCustomerByCusual(customer);

	}

	@Override
	public List<Customer> queryCustomer() {
		return sshDao.queryCustomer();
	}

}
