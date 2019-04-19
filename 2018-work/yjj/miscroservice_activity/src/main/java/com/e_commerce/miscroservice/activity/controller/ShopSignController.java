package com.e_commerce.miscroservice.activity.controller;

import com.e_commerce.miscroservice.activity.service.ShopSignService;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 签到
 */
@RestController
@RequestMapping("/shop/sign")
public class ShopSignController {

    @Autowired
    private ShopSignService shopSignService;


    /**
     * 签到
     *
     * @param userId 用户id
     *               <p>
     *               {
     *               "msg": "签到成功",
     *               "code": 200,
     *               "data": 0.01
     *               }
     * @return
     */
    @RequestMapping("/do/sign")
    private Response doSign(Long userId,@RequestParam(name = "conDay",required = false,defaultValue = "0") Integer conDay) {
        return shopSignService.doSign(userId,conDay);
    }


    /**
     * 当月签到日期
     *
     * @param userId 用户
     *               <p>
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": {
     *               "totalNumber": 1,  总 签到天数
     *               "monthNumber": 1,  当月签到天数
     *               "signCoin": 15.05,  签到获得金币
     *               "days": [ 签到日期数组
     *               "16"
     *               ],
     *               "continueNumber": 1, 持续签到天数
     *               "coin": 16.06 金币数
     *               }
     *               }
     * @return
     */
    @RequestMapping("/show/days")
    private Response showDays(Long userId) {
        return shopSignService.showDays(userId);
    }

    /**
     * 金币获得记录
     *
     * @param page   页码
     * @param userId 用户id
     *               <p>
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": [
     *               {
     *               "id": 12,
     *               "inOutType": 1,
     *               "userId": 100,
     *               "status": 2,
     *               "operCash": 0.00,
     *               "balanceCash": 0.00,
     *               "operGoldCoin": 0.05,
     *               "operTime": 1539678903513,
     *               "originalGoldCoin": 16.01,
     *               "originalCash": 0.00,
     *               "orderEarningsSnapshoot": 0.00,
     *               "earningsRatio": 0.00,
     *               "currencyRatio": 0.00,
     *               "type": 21,
     *               "remark": "1",
     *               "createTime": 1539678902000,
     *               "updateTime": 1539678902000
     *               },
     *               {
     *               "id": 11,
     *               "inOutType": 1,
     *               "userId": 100,
     *               "status": 2,
     *               "operCash": 0.00,
     *               "balanceCash": 0.00,
     *               "operGoldCoin": 0.01,
     *               "operTime": 1539678575783,
     *               "originalGoldCoin": 16.00,
     *               "originalCash": 0.00,
     *               "orderEarningsSnapshoot": 0.00,
     *               "earningsRatio": 0.00,
     *               "currencyRatio": 0.00,
     *               "type": 20,
     *               "remark": "连续签到1天，送0.01金币",
     *               "createTime": 1539678575000,
     *               "updateTime": 1539678575000
     *               }
     *               ]
     *               }
     * @return
     */
    @RequestMapping("/gold/coin/log")
    public Response getGoldCoinLog(Integer page, Long userId) {
        return shopSignService.getGoldCoinLog(page, userId);
    }


    /**
     * 获取签到阶段奖励
     *
     * @param userId 用户
     * @param num    阶段奖励的日期 day
     *               <p>
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": 0.05
     *               }
     * @return
     */
    @RequestMapping("/periodical/prize")
    public Response getPeriodicalPrize(Long userId, Integer num) {
        return shopSignService.getPeriodicalPrize(userId, num);
    }
}
