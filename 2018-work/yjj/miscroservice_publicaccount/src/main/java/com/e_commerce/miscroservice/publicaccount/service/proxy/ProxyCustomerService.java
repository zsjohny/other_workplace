package com.e_commerce.miscroservice.publicaccount.service.proxy;


import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerAudit;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerQuery;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 代理商
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:24
 * @Copyright 玖远网络
 */
public interface ProxyCustomerService{


    /**
     * 注册代理商
     *
     * @param authCode 手机验证码
     * @param response
     * @param openToken
     * @param openId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/9/20 13:51
     */
    ProxyCustomerAudit doRegister(ProxyCustomerAudit audit, String authCode, HttpServletResponse response, String openToken, String openId);

    ProxyCustomerAudit getAuditStatus(long userId);


    /**
     * 审核注册代理
     *
     * @param auditId auditId
     * @param msg 审核意见
     * @param operUserId 审核人
     * @param isPass 是否通过
     * @author Charlie
     * @date 2018/9/22 13:20
     */
    void doAudit(Long auditId, String msg, Long operUserId, Boolean isPass);


    /**
     * 删除一条审核记录
     *
     * @param auditId 审核id
     * @param operUserId 操作人员id
     * @author Charlie
     * @date 2018/9/25 10:57
     */
    Response delete(Long auditId, Long operUserId);


    ProxyCustomer selectByUserId(Long proxyCustomerId);

    /**
     * 描述 1 市代理  0 客户 2 县代理
     * @param proxyCustomerId
     * @author hyq
     * @date 2018/10/10 10:39
     * @return int
     */
    int judeCustomerRole(Long proxyCustomerId);

    String posterQrcode(Integer type, Long userId, String webUrl);

    /**
     * 申请代理
     *
     * @param param param
     * @return com.e_commerce.miscroservice.user.po.proxy.ProxyCustomer
     * @author Charlie
     * @date 2018/9/20 14:33
     */
    ProxyCustomerAudit addProxyCustomerAudit(ProxyCustomerAudit param);



    /**
     * 代理商申请列表
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery>
     * @author Charlie
     * @date 2018/10/8 6:56
     */
    List<ProxyCustomerAuditQuery> auditList(ProxyCustomerAuditQuery query);

    List<ProxyCustomerQuery> customerList(ProxyCustomerQuery query);


    /**
     * 找最近的一个审核未通过或者失败的申请
     *
     * @param user user
     * @return boolean
     * @author Charlie
     * @date 2018/10/22 19:21
     */
    Map<String, Object> recentlyUnCheckOrFailedAudit(PublicAccountUser user);
}
