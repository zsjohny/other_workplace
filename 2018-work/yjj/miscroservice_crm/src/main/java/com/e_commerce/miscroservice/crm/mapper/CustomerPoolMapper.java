package com.e_commerce.miscroservice.crm.mapper;

import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolAddRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolsFindRequest;
import com.e_commerce.miscroservice.crm.entity.response.CustomerPoolResponse;
import com.e_commerce.miscroservice.crm.po.CustomerPoolPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by hyf on 2018/9/14
 */
@Mapper
public interface CustomerPoolMapper {
    /**
     * 查询所有的公海池-客户管理
     * @param request
     * @return
     */
    List<CustomerPoolResponse> findAllCustomerPool(@Param("request") CustomerPoolsFindRequest request);

    /**
     * 添加 公海池 添加用户
     * @param request
     * @return
     */
    int addCustomer(@Param("request")CustomerPoolAddRequest request);

    /**
     * list 添加公海池用户
     * @param list
     */
    void addCustomerList(@Param("list") List<CustomerPoolAddRequest> list);

    /**
     * 批量查询
     * @param list
     * @return
     */
    List<CustomerPoolPO> findCustomerListByPhone(@Param("list")List<CustomerPoolAddRequest> list);

    /**
     * 根据id集合批量查询
     * @param ids
     * @return
     */
    List<CustomerPoolPO> findCustomerListByIds(@Param("array")String[] ids,@Param("type") String type);

    /**
     * 根据用户id 批量 更新 所属id
     * @param  ids
     * @param userId
     */
    void updateCustomers(@Param("array")String[] ids, @Param("userId")Long userId,@Param("type")String type);

}
