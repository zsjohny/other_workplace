package com.e_commerce.miscroservice.store.rpc;

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

    /**
     * 更新账户待结算到已结算
     *
     * @param shopMemberOrderNo shopMemberOrderNo
     * @return boolean
     * @author Charlie
     * @date 2019/2/19 14:21
     */
    @RequestMapping( "/sendGoodsAfter15DaysWaitMoneyIn" )
    boolean sendGoodsAfter15DaysWaitMoneyIn(@RequestParam( "shopMemberOrderNo" ) String shopMemberOrderNo,
                                            @RequestParam( "storeId" ) Long storeId,
                                            @RequestParam( "storeOrderNo" )Long storeOrderNo);
}
