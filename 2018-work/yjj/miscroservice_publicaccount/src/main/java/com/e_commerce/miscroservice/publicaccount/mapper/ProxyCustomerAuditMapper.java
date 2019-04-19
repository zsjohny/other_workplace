package com.e_commerce.miscroservice.publicaccount.mapper;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerAudit;
import com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/8 6:59
 * @Copyright 玖远网络
 */
@Mapper
public interface ProxyCustomerAuditMapper{


    /**
     * 代理商申请列表
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery>
     * @author Charlie
     * @date 2018/10/8 6:56
     */
    List<ProxyCustomerAuditQuery> auditList(ProxyCustomerAuditQuery query);




}
