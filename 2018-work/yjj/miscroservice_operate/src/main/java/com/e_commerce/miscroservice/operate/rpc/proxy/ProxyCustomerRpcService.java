package com.e_commerce.miscroservice.operate.rpc.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerQuery;
import com.e_commerce.miscroservice.commons.entity.proxy.MyProxyQueryVo;
import com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 9:11
 * @Copyright 玖远网络
 */
@FeignClient(value = "PUBLICACCOUNT", path = "public/rpc/proxyCustomer"/*, fallback = ProxyCustomerRpcService.class*/)
public interface ProxyCustomerRpcService{


    /**
     * 市代理审核
     *
     * @param auditId 审核id
     * @param msg 审核意见
     * @param operUserId 操作人员id
     * @param isPass 1:通过,0:不通过
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author Charlie
     * @date 2018/9/25 9:44
     */
    @RequestMapping("audit")
    String audit(@RequestParam("auditId") Long auditId,
                 @RequestParam("msg") String msg,
                 @RequestParam("operUserId") Long operUserId,
                 @RequestParam("isPass") Integer isPass);


    /**
     * 删除一条审核记录
     *
     * @param auditId 审核id
     * @param operUserId 操作人员id
     * @author Charlie
     * @date 2018/9/25 10:57
     */
    @RequestMapping("delete")
    String delete(@RequestParam("auditId") Long auditId, @RequestParam("operUserId") Long operUserId);


    /**
     * 用户所有的下级代理/客户数量
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/9/27 17:24
     */
    @RequestMapping( "myProxyCustomer" )
    String myProxyCustomer(@RequestBody MyProxyQueryVo vo);

    @RequestMapping("auditList")
    List<ProxyCustomerAuditQuery> auditList(@RequestBody ProxyCustomerAuditQuery query);


    @RequestMapping("unbind")
    void unBindById(@RequestParam("refereeId") Long refereeId, @RequestParam("operUserId") Long operUserId);

    @RequestMapping("customerList")
    public String customerList(@RequestBody ProxyCustomerQuery query);


}
