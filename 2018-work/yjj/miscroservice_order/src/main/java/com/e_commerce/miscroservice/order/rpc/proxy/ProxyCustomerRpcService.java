package com.e_commerce.miscroservice.order.rpc.proxy;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import com.e_commerce.miscroservice.order.rpc.impl.ProxyPayRpcServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述
 * @author hyq
 * @date 2018/10/10 20:30
 */
@FeignClient(value = "publicaccount")
public interface ProxyCustomerRpcService {

    @RequestMapping(value = "public/proxyCustomer/judeCustomerRole")
    public int judeCustomerRole(@RequestParam(value = "userId")Long userId);

    @RequestMapping( "public/yOpen/publicAccountUser/findByUserId" )
    PublicAccountUser findByUserId(@RequestParam(value = "userId")long userId);

}
