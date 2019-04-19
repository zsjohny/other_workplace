package com.jiuy.operator.controller.newController.account;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.HttpUtils;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.enums.AccountTypeEnum;
import com.jiuy.rb.enums.CoinsDetailTypeEnum;
import com.jiuy.rb.model.account.CoinsCashOutQuery;
import com.jiuy.rb.model.account.CoinsQuery;
import com.jiuy.rb.model.account.CoinsVo;
import com.jiuy.rb.service.account.ICoinsAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * 玖币账户controller
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/11 15:35
 * @Copyright 玖远网络
 */
@ResponseBody
@Controller
@RequestMapping("/admin/")
public class CoinsAccountController {


    @Resource(name = "coinsAccountService")
    private ICoinsAccountService coinsAccountService;


    /**
     * 玖币后台管理接口
     *
     * @param query 查询实体
     * @author Aison
     * @date 2018/7/11 15:36
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("coinsList")
    public ResponseResult coinsList(CoinsQuery query) {

        return ResponseResult.instance().success(coinsAccountService.coinsList(query));
    }


    /**
     * 提现列表
     * @param query query
     * @author Aison
     * @date 2018/7/19 9:35
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("cashOutList")
    public ResponseResult cashOutList(CoinsCashOutQuery query) {

        if(Biz.isEmpty(query.getBeginTime())) {
            query.setBeginTime(null);
        }
        if(Biz.isEmpty(query.getEndTime())) {
            query.setEndTime(null);
        }
        if(Biz.isEmpty(query.getNickName())) {
            query.setNickName(null);
        }
        if(Biz.isEmpty(query.getStatus())) {
            query.setStatus(null);
        }
        query.setQueryStore (true);
        return ResponseResult.instance().success(coinsAccountService.cashOutMyPage(query));
    }


    /**
     * 确定提现
     *
     * @param cashOutId cashOutId
     * @author Aison
     * @date 2018/7/19 16:49
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("doCashOut")
    public ResponseResult doCashOut(Long cashOutId, Integer agree, HttpServletRequest request) {

        agree = agree == null || agree == 1 ? 1 : 0;
        UserSession userSession = UserSession.getUserSession();
        String ip =  HttpUtils.getIpAdrress(request);
        userSession.setRequestIp(ip);
        coinsAccountService.dealCashOut(cashOutId,agree==1,userSession);
        return ResponseResult.SUCCESS;
    }




    /**
     *
     *
     * 测试 @RequestMapping("init")
     */

    public ResponseResult init() {

        Integer[] longs = new Integer[] {1,2,3,50,51};

        Random random = new Random();
        for(int i=0;i<100;i++) {

            new Thread(() -> {

                Integer count = random.nextInt(300);
                Integer memberId =  random.nextInt(300);

                CoinsDetailTypeEnum enums = CoinsDetailTypeEnum.getEnum(longs[count%5]);
                CoinsVo coinsVo =  CoinsVo.instance(enums,"",count.longValue(),null,null,"分享商品:"+count,"",UserSession.getUserSession(),memberId.longValue(),AccountTypeEnum.WXA_USER).create(true);
                try{
                    Thread.sleep(300);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                coinsVo.setInitCount(500L);
                coinsAccountService.acceptCoins(coinsVo);
            }).start();
        }

        return ResponseResult.SUCCESS;

    }
}
