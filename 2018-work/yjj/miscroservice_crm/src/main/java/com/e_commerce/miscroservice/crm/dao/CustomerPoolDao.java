package com.e_commerce.miscroservice.crm.dao;

import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolAddRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolsFindRequest;
import com.e_commerce.miscroservice.crm.entity.response.CustomerPoolResponse;
import com.e_commerce.miscroservice.crm.po.CustomerPoolPO;

import java.util.List;
import java.util.Map;

/**
 * Create by hyf on 2018/9/13
 */
public interface CustomerPoolDao {
    /**
     * 查询所有的公海池-客户管理
     * @param request
     * @return
     */
    List<CustomerPoolResponse> findAllCustomerPool(CustomerPoolsFindRequest request);

    /**
     * 添加公海池用户
     * @param request
     * @return
     */
    int addCustomer(CustomerPoolAddRequest request);

    /**
     * 根据手机号查找 公海池用户
     * @param phone
     * @return
     */
    CustomerPoolPO findCustomByPhone(String phone);

    /**
     * list 添加公海池用户
     * @param list
     */
    void addCustomerList(List<CustomerPoolAddRequest> list);

    /**
     * 批量查询
     * @param list
     */
    List<CustomerPoolPO> findCustomerListByPhone(List<CustomerPoolAddRequest> list);

    /**
     * 根据 id 集合 批量查询
     * @param ids
     * @return
     */
    List<CustomerPoolPO> findCustomerListByIds(String[] ids,String type);

    /**
     * 根据用户id 批量 更新 所属id
     * @param ids
     * @param userId
     */
    void updateCustomers(String[] ids, Long userId,String type);
}
