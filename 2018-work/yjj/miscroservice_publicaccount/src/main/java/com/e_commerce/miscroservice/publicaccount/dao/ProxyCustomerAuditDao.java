package com.e_commerce.miscroservice.publicaccount.dao;


import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerAudit;
import com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery;
import com.e_commerce.miscroservice.publicaccount.entity.enums.ProxyCustomerAuditStatus;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:03
 * @Copyright 玖远网络
 */
public interface ProxyCustomerAuditDao{

    /**
     * 创建一个新的对象用于insert
     *
     * @param param param
     * @return com.e_commerce.miscroservice.operate.po.proxy.ProxyCustomerAudit
     * @author Charlie
     * @date 2018/9/20 18:12
     */
    static ProxyCustomerAudit build(ProxyCustomerAudit param) {
        ProxyCustomerAudit nowAudit = new ProxyCustomerAudit ();
        nowAudit.setAuditStatus (ProxyCustomerAuditStatus.PROCESSING.getCode ());
        nowAudit.setPhone (param.getPhone ());
        nowAudit.setRecommonUserId (param.getRecommonUserId ());
        nowAudit.setRefereeUserId (param.getRefereeUserId ());
        nowAudit.setProvince (param.getProvince ());
        nowAudit.setCity (param.getCity ());
        nowAudit.setCounty (param.getCounty ());
        nowAudit.setAddressDetail (param.getAddressDetail ());
        nowAudit.setType (param.getType ());
        nowAudit.setPhone (param.getPhone ());
        nowAudit.setName (param.getName ());
        nowAudit.setIdCardNo (param.getIdCardNo ());
        return nowAudit;
    }

    /**
     * 审核
     *
     * @param status 审核状态
     * @param auditId auditId
     * @param msg 审核意见
     * @param operUserId 审核人
     * @return int
     * @author Charlie
     * @date 2018/9/22 12:50
     */
    int audit(ProxyCustomerAuditStatus status, Long auditId, String msg, Long operUserId);

    ProxyCustomerAudit selectById(Long auditId);

    /**
     * 代理商申请列表
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery>
     * @author Charlie
     * @date 2018/10/8 6:56
     */
    List<ProxyCustomerAuditQuery> auditList(ProxyCustomerAuditQuery query);


    /**
     * 找最近的一个审核未通过或者失败的申请
     *
     * @param userId userId
     * @return boolean
     * @author Charlie
     * @date 2018/10/22 19:21
     */
    ProxyCustomerAudit recentlyUnCheckOrFailedAudit(Long userId);

}
