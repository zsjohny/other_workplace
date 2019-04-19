package com.e_commerce.miscroservice.distribution.rpc.user;

import com.e_commerce.miscroservice.commons.entity.user.StoreWxaVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:53
 * @Copyright 玖远网络
 */
@FeignClient(value = "USER", path = "user/rpc/wxaPayConfig")
public interface WxaPayConfigRpcService{

    @GetMapping("findByStoreId")
    StoreWxaVo findByStoreId(@RequestParam("storeId") Long storeId);
}
