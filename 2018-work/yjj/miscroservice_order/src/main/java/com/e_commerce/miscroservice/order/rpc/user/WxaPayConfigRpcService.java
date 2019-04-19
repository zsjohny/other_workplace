package com.e_commerce.miscroservice.order.rpc.user;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/12 10:43
 * @Copyright 玖远网络
 */
@FeignClient(value = "USER", path = "user/rpc/wxaPayConfig")
public interface WxaPayConfigRpcService{

    /**
     * 查询用户微信信息
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.user.entity.StoreWxa
     * @author Charlie
     * @date 2018/12/6 15:43
     */
    @RequestMapping("findByStoreId")
    StoreWxaVo findByStoreId(@RequestParam("storeId") Long storeId);
}
