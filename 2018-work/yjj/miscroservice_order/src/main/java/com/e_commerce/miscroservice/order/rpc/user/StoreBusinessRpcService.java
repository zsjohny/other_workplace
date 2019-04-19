package com.e_commerce.miscroservice.order.rpc.user;

import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 15:25
 * @Copyright 玖远网络
 */
@FeignClient(value = "USER", path = "user/rpc/storeBusinessRpcController")
public interface StoreBusinessRpcService{


    /**
     * 用户是否可以开通店中店
     *
     * @param storeId storeId
     * @return boolean
     * @author Charlie
     * @date 2018/12/11 15:59
     */
    @RequestMapping( "isCanOpenInShop" )
    boolean isCanOpenInShop(@RequestParam("storeId") Long storeId);


    /**
     * 根据店中店用户小程序用户id.查询APP账户id
     *
     * @param inShopMemberId inShopMemberId
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/12 15:24
     */
    @RequestMapping("findStoreIdByInShopMemberId")
    StoreBusinessVo findStoreIdByInShopMemberId(@RequestParam("inShopMemberId") Long inShopMemberId);
}
