package com.e_commerce.miscroservice.distribution.rpc.user;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 账户RPC
 * Create by hyf on 2018/12/13
 */

@FeignClient(value = "USER",path = "user/rpc/storeBusinessAccountRpcController")
public interface StoreBusinessAccountRpcService {

    /**
     * 更新用户账户
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    @RequestMapping( "/accountUpdate" )
    public Integer updateStoreBusinessAccount(@RequestBody YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 查询用户账户
     * @param userId
     * @return
     */
    @RequestMapping( "/accountSelect" )
    public YjjStoreBusinessAccount selectStoreBusinessAccount(@RequestParam("userId") Long userId);
    /**
     * 查询用户账户 日志
     * @param userId
     * @return
     */
    @RequestMapping( "/logSelect" )
    public SimplePage selectStoreBusinessAccountLog(@RequestParam("userId") Long userId, @RequestParam("pageNumber") Integer pageNumber);
}
