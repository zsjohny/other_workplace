package com.e_commerce.miscroservice.order.rpc.proxy;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.order.rpc.impl.ProxyPayRpcServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 调用微信支付
 */
@FeignClient(value = "USER")
public interface ProxyUserRpcService {


    @RequestMapping("user/rpc/memberRpcController/buyMember")
    String buyMember(
            @RequestParam(value = "canal") String canal,
            @RequestParam (value = "city") String city,
            @RequestParam (value = "district") String district,
            @RequestParam (value = "memberLevel") Integer memberLevel,
            @RequestParam (value = "name") String name,
            @RequestParam (value = "orderNo") String orderNo,
            @RequestParam (value = "totalMoney") Double totalMoney,
            @RequestParam (value = "phone") String phone,
            @RequestParam (value = "province") String province,
            @RequestParam (value = "type") Integer type);


}
