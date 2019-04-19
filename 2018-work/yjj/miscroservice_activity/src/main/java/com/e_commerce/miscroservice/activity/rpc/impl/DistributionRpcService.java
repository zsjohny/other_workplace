package com.e_commerce.miscroservice.activity.rpc.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/17 10:43
 * @Copyright 玖远网络
 */
@FeignClient(value = "DISTRIBUTION", path = "/distribution/rpc")
@Component
public interface DistributionRpcService {

    /**
     * 根据用户id查询 分销账户表
     * @param userId
     * @return
     */
    @GetMapping("findByUser")
    public ShopMemberAccount findByUser(@RequestParam(value = "userId") Long userId);
    /**
     * 根据用户id更新 分销账户表
     *
     * @param aliveGoldCoin
     * @param allGoldCoin
     * @param signGoldCoin
     * @param historyGoldCoinEarning
     * @param userId
     * @return
     */
    @GetMapping("updateShopMemberAccountByUserId")
    public void updateShopMemberAccountByUserId(@RequestParam( value = "aliveGoldCoin" ) BigDecimal aliveGoldCoin, @RequestParam( value = "allGoldCoin" ) BigDecimal allGoldCoin, @RequestParam( value = "signGoldCoin" ) BigDecimal signGoldCoin, @RequestParam( value = "historyGoldCoinEarning" ) BigDecimal historyGoldCoinEarning, @RequestParam( value = "userId" ) Long userId);
    /**
     * 保存 分销账户表
     * @return
     */
    @GetMapping("saveShopMemberAccount")
    public void saveShopMemberAccount(@RequestParam(value = "userId")Long userId, @RequestParam("coin")BigDecimal coin);
    /**
     * 根据页码 用户id分销账户日志
     * @return
     */
    @GetMapping("findLogList")
    public List<ShopMemberAccountCashOutIn> findLogList(@RequestParam(value = "page") Integer page, @RequestParam(value = "userId") Long userId);
    /**
     * 当月 签到阶段奖励
     * @param userId
     * @return
     */
    @GetMapping("findPeriodicalPrizeMonthLog")
    public  List<ShopMemberAccountCashOutIn> findPeriodicalPrizeMonthLog(@RequestParam(value = "userId") Long userId);


    /**
     * 保存账户流水
     * @param operTime
     * @param inOutType
     * @param userId
     * @param status
     * @param prize
     * @param aliveGoldCoin
     * @param type
     * @param remark
     */
    @RequestMapping(value = "saveAccountLog")
    public void saveAccountLog(@RequestParam(value = "operTime") Long operTime, @RequestParam(value = "inOutType") Integer inOutType, @RequestParam(value = "userId")Long userId,
                               @RequestParam(value = "status")Integer status,
                               @RequestParam(value = "detailStatus")Integer detailStatus,
                               @RequestParam(value = "prize")BigDecimal prize, @RequestParam(value = "aliveGoldCoin")BigDecimal aliveGoldCoin,@RequestParam(value = "type") Integer type, @RequestParam(value = "remark")String remark);

    /**
     * 根据收益类型添加收益
     *
     * @param addInfo addInfo
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/22 11:53
     */
    @RequestMapping(value = "addCashOutInByType")
    Map<String, Object> addCashOutInByType(@RequestBody ShopMemAcctCashOutInQuery addInfo);


    /**
     * 绑定粉丝
     *
     * @param userId 用户id
     * @param fans 粉丝id
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/14 13:38
     */
    @RequestMapping("/binding/fans")
    Response bindingFans(@RequestParam("userId") Long userId, @RequestParam("fans") Long fans);

}
