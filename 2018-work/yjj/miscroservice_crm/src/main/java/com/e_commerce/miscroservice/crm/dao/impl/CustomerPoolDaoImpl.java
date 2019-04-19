package com.e_commerce.miscroservice.crm.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.crm.dao.CustomerPoolDao;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolAddRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolsFindRequest;
import com.e_commerce.miscroservice.crm.entity.response.CustomerPoolResponse;
import com.e_commerce.miscroservice.crm.mapper.CustomerPoolMapper;
import com.e_commerce.miscroservice.crm.po.CustomerPoolPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/13 21:52
 * @Copyright 玖远网络
 */
@Repository
public class CustomerPoolDaoImpl implements CustomerPoolDao {
    @Resource
    private CustomerPoolMapper customerPoolMapper;

    @Override
    public List<CustomerPoolResponse> findAllCustomerPool(CustomerPoolsFindRequest request) {
        List<CustomerPoolResponse> list = customerPoolMapper.findAllCustomerPool(request);
        return list;
    }

    @Override
    public int addCustomer(CustomerPoolAddRequest request) {
        return MybatisOperaterUtil.getInstance().save(request);
    }

    @Override
    public CustomerPoolPO findCustomByPhone(String phone) {
        CustomerPoolPO customerPoolPO = MybatisOperaterUtil.getInstance().findOne(new CustomerPoolPO(), new MybatisSqlWhereBuild(CustomerPoolPO.class).eq(CustomerPoolPO::getDelState, 0).eq(CustomerPoolPO::getPhone, phone));
        return customerPoolPO;
    }

    @Override
    public void addCustomerList(List<CustomerPoolAddRequest> list) {
        customerPoolMapper.addCustomerList(list);
    }

    @Override
    public List<CustomerPoolPO> findCustomerListByPhone(List<CustomerPoolAddRequest> list) {
        return customerPoolMapper.findCustomerListByPhone(list);
    }

    @Override
    public List<CustomerPoolPO> findCustomerListByIds(String[] ids, String type) {
        return customerPoolMapper.findCustomerListByIds(ids, type);
    }

    @Override
    public void updateCustomers(String[] ids, Long userId, String type) {
        customerPoolMapper.updateCustomers(ids, userId, type);
    }
}
