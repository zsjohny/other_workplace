package com.e_commerce.miscroservice.operate.rpc.impl;//package com.e_commerce.miscroservice.operate.rpc.impl;
//
//import com.e_commerce.miscroservice.commons.log.Log;
//import com.e_commerce.miscroservice.commons.utils.Response;
//import com.e_commerce.miscroservice.operate.rpc.proxy.ProxyCustomerRpcService;
//import org.springframework.stereotype.Component;
//
///**
// * @author Charlie
// * @version V1.0
// * @date 2018/9/25 9:11
// * @Copyright 玖远网络
// */
//@Component
//public class ProxyCustomerRpcServiceImpl implements ProxyCustomerRpcService{
//    private Log logger = Log.getInstance(ProxyCustomerRpcServiceImpl.class);
//
//
//    @Override
//    public Response audit(Long auditId, String msg, Long operUserId, Integer isPass) {
//        logger.info ("市代理审核,系统没响应进入回掉 auditId:{},msg:{},operUserId:{},isPass:{}",auditId, msg, operUserId, isPass);
//        return Response.success ("网络异常");
//    }
//
//    @Override
//    public Response delete(Long auditId, Long operUserId) {
//        logger.info ("删除一条审核记录,系统没响应进入回掉 auditId:{},operUserId:{}",auditId, operUserId);
//        return Response.success ("网络异常");
//    }
//}
