package com.e_commerce.miscroservice.crm.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolAddRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolGetRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolsFindRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * Create by hyf on 2018/9/13
 */
public interface CustomerPoolService {
    /**
     * 查询所有的 公海池
     *
     * @param request
     * @return
     */
    Response findAllCustomerPool(CustomerPoolsFindRequest request);

    /**
     * 添加 公海用户
     *
     * @param request
     * @return
     */
    Response addCustomer(CustomerPoolAddRequest request);

    /**
     * 通过excel添加客户
     *
     * @param file
     * @return
     */
    Response addCustomerExcels(MultipartFile file, Long userId, Integer addStatus);

    /**
     * 领取公海池用户
     *
     * @return
     * @author hyf
     * @date 2018/9/18 11:21
     */
    Response upCustomers(CustomerPoolGetRequest request);

    Response test();
}
