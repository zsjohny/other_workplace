package com.e_commerce.miscroservice.distribution.controller;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.distribution.service.DistributionSystemService;
import com.e_commerce.miscroservice.distribution.service.ShopMemberAccountCashOutInService;
import com.e_commerce.miscroservice.distribution.service.ShopMemberAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/17 10:04
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping("/distribution/rpc")
public class SysRpcController {

    @Autowired
    private ShopMemberAccountService shopMemberAccountService;
    @Autowired
    private DistributionSystemService distributionSystem;
    @Autowired
    private ShopMemberAccountCashOutInService shopMemberAccountCashOutInService;

    /**
     * 根据用户id查询 分销账户表
     * @param userId
     * @return
     */
    @GetMapping("findByUser")
    public ShopMemberAccount findByUser(@RequestParam(value = "userId") Long userId){
        ShopMemberAccount shopMemberAccount = shopMemberAccountService.findByUser(userId);
        return shopMemberAccount;
    }
    /**
     * 根据用户id更新 分销账户表
     * @param userId
     * @return
     */
    @GetMapping("updateShopMemberAccountByUserId")
    public void updateShopMemberAccountByUserId(@RequestParam(value = "aliveGoldCoin")BigDecimal aliveGoldCoin, @RequestParam(value = "allGoldCoin")BigDecimal allGoldCoin, @RequestParam(value = "signGoldCoin")BigDecimal signGoldCoin,@RequestParam(value = "userId") Long userId){
                    ShopMemberAccount shopMemberAccount = new ShopMemberAccount();
            shopMemberAccount.setAliveGoldCoin(aliveGoldCoin);
            shopMemberAccount.setAllGoldCoin(allGoldCoin);
            shopMemberAccount.setSignGoldCoin(signGoldCoin);
        shopMemberAccountService.updateShopMemberAccountByUserId(shopMemberAccount,userId);
    }

    /**
     * 保存 分销账户表
     * @return
     */
    @GetMapping("saveShopMemberAccount")
    public void saveShopMemberAccount(@RequestParam(value = "userId")Long userId, @RequestParam("coin") BigDecimal coin){
        shopMemberAccountService.saveShopMemberAccount(userId,coin);
    }
    /**
     * 根据页码 用户id分销账户日志
     * @return
     */
    @GetMapping("findLogList")
    public  List<ShopMemberAccountCashOutIn> findLogList(@RequestParam(value = "page") Integer page,@RequestParam(value = "userId") Long userId){
        List<ShopMemberAccountCashOutIn> shopMemberAccountCashOutIn = shopMemberAccountCashOutInService.findLogList(page,userId);
        return shopMemberAccountCashOutIn;
    }
    /**
     * 当月 签到阶段奖励
     * @param userId
     * @return
     */
    @GetMapping("findPeriodicalPrizeMonthLog")
    public  List<ShopMemberAccountCashOutIn> findPeriodicalPrizeMonthLog(@RequestParam(value = "userId") Long userId){
        List<ShopMemberAccountCashOutIn> list = shopMemberAccountCashOutInService.findPeriodicalPrizeMonthLog(userId);

        return list;
    }

    /**
     * 保存账户流水
     * @return
     */
    @RequestMapping(value = "saveAccountLog")
    public void saveAccountLog(@RequestParam(value = "operTime") Long operTime, @RequestParam(value = "inOutType") Integer inOutType, @RequestParam(value = "userId")Long userId,
                               @RequestParam(value = "status")Integer status,
                               @RequestParam(value = "detailStatus")Integer detailStatus,
                               @RequestParam(value = "prize")BigDecimal prize, @RequestParam(value = "aliveGoldCoin")BigDecimal aliveGoldCoin,@RequestParam(value = "type") Integer type,
                               @RequestParam(value = "remark") String remark){
        ShopMemberAccountCashOutIn shopMemberAccount = new ShopMemberAccountCashOutIn();
        shopMemberAccount.setOperTime(operTime);
        shopMemberAccount.setInOutType(inOutType);
        shopMemberAccount.setUserId(userId);
        shopMemberAccount.setStatus(status);
        shopMemberAccount.setDetailStatus (detailStatus);
        shopMemberAccount.setOperGoldCoin(prize);
        shopMemberAccount.setOriginalGoldCoin(aliveGoldCoin);
        shopMemberAccount.setType(type);
        shopMemberAccount.setRemark(remark);
       shopMemberAccountCashOutInService.saveAccountLog(shopMemberAccount);
    }




    /**
     * 根据收益类型,新增收益流水
     *
     * @param addInfo addInfo
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Charlie
     * @date 2018/11/22 16:51
     */
    @RequestMapping(value = "addCashOutInByType")
    public Map<String, Object> addCashOutInByType(@RequestBody ShopMemAcctCashOutInQuery addInfo){
        return shopMemberAccountService.addCashOutInByType (addInfo);
    }


    /**
     * 绑定粉丝
     *
     * @param userId userId
     * @param fans fans
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/14 13:36
     */
    @RequestMapping("/binding/fans")
    public Response bindingFans(@RequestParam("userId") Long userId, @RequestParam("fans") Long fans){
        return distributionSystem.bindingFans(userId,fans);
    }


}
