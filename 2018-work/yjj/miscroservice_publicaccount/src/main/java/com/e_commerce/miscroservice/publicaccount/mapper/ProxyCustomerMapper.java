package com.e_commerce.miscroservice.publicaccount.mapper;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/26 21:41
 * @Copyright 玖远网络
 */
@Mapper
public interface ProxyCustomerMapper{

    List<ProxyCustomer> customerList(ProxyCustomerQuery query);




}
