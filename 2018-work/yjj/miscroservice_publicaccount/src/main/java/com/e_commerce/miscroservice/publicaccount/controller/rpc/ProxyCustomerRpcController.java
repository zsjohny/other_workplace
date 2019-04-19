package com.e_commerce.miscroservice.publicaccount.controller.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerQuery;
import com.e_commerce.miscroservice.commons.entity.proxy.MyProxyQueryVo;
import com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyCustomerService;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyRefereeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 代理商
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:25
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "public/rpc/proxyCustomer" )
public class ProxyCustomerRpcController{
    private Log logger = Log.getInstance(ProxyCustomerRpcController.class);

    private final ProxyCustomerService proxyCustomerService;

    private final ProxyRefereeService proxyRefereeService;

    @Autowired
    public ProxyCustomerRpcController(ProxyCustomerService proxyGoodsService, ProxyRefereeService proxyRefereeService) {
        this.proxyCustomerService = proxyGoodsService;
        this.proxyRefereeService = proxyRefereeService;
    }



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
    public String audit(Long auditId, String msg, Long operUserId, Integer isPass) {
        Response response;
        try {
            proxyCustomerService.doAudit (auditId, msg, operUserId, isPass == 1);
            response =  Response.success ();
        } catch (ErrorHelper e) {
            response = ResponseHelper.errorHandler (e);
        }
        return JSONObject.toJSONString (response);
    }


    @RequestMapping( "delete" )
    public String delete(Long auditId, Long operUserId) {
        return JSONObject.toJSONString (proxyCustomerService.delete (auditId, operUserId));
    }

    /**
     * 解绑代理的客户/下级代理
     *
     * @param refereeId   关系id
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author Charlie
     * @date 2018/9/22 16:39
     */
    @RequestMapping( "unbind" )
    public String unbind(Long refereeId, Long operUserId) {
        int rec = proxyRefereeService.unBindById (refereeId, operUserId);
        logger.info ("解绑代理的客户/下级代理 refereeId={},operUserId={},rec={}", rec, operUserId, rec);
        return JSONObject.toJSONString (Response.success ());
    }


    /**
     * 用户所有的下级代理/客户数量
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/9/27 17:24
     */
    @RequestMapping( "myProxyCustomer" )
    public String myProxyCustomer(@RequestBody MyProxyQueryVo vo) {
        return JSONObject.toJSONString (Response.success (proxyRefereeService.myProxyCustomer (vo)));
    }


    /**
     * 代理商申请列表
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery>
     * @author Charlie
     * @date 2018/10/8 6:56
     */
    @RequestMapping("auditList")
    public List<ProxyCustomerAuditQuery> auditList(@RequestBody ProxyCustomerAuditQuery query){
        List<ProxyCustomerAuditQuery> proxyCustomerAuditQueries = proxyCustomerService.auditList(query);
        return proxyCustomerAuditQueries;
    }

    /**
     * 描述 代理商管理
     * @param query
     * @author hyq
     * @date 2018/10/15 15:55
     * @return java.lang.String
     */
    @RequestMapping("customerList")
    public String customerList(@RequestBody ProxyCustomerQuery query){
        return JSON.toJSONString(Response.success(new PageInfo<>(proxyCustomerService.customerList(query))));
    }



}
