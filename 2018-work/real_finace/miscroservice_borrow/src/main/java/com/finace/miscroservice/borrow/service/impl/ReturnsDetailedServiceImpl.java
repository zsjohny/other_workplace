package com.finace.miscroservice.borrow.service.impl;

import com.finace.miscroservice.borrow.dao.ReturnsDetailedDao;
import com.finace.miscroservice.borrow.entity.response.HMoneyResponse;
import com.finace.miscroservice.borrow.entity.response.ReturnsDetailedResponse;
import com.finace.miscroservice.borrow.service.ReturnsDetailedService;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReturnsDetailedServiceImpl implements ReturnsDetailedService {
    Log logger = Log.getInstance(ReturnsDetailedServiceImpl.class);
    @Autowired
    private ReturnsDetailedDao returnsDetailedDao;

    public static Double DEFAULT_MONEY=0.0; //初始化 金额 0.0

    @Override
    public Response findReturnsDetailed(Integer userId) {
        logger.info("--->查询用户userId={}的收益明细<---",userId);
        //根据用户id查询用户
        User user = returnsDetailedDao.findUserByUserId(userId);
        Double hAccount = DEFAULT_MONEY; //汇付本金
        Double hInterest = DEFAULT_MONEY;//汇付利息
//        Double hRepaymentAccount = DEFAULT_MONEY;//汇付本息

        //若为 汇付用户
        if (StringUtils.isNotEmpty(user.getTrustUsrCustId())){
            //汇付用户
            HMoneyResponse moneyResponse = returnsDetailedDao.findHuifu(userId);
                //本金
                hAccount = moneyResponse.getHAccount();
                //利息
                hInterest = moneyResponse.getHInterest();
        }
        ReturnsDetailedResponse response2 = returnsDetailedDao.findWaitBack(userId);
        //待回款本金(含红包)
        Double waitBackPrincipal = response2.getWaitBackPrincipal();
        //待回款利息
        Double waitBackInterest = response2.getWaitBackInterest();
//        总待回款总额（本息）
        Double amtWaitBackMoney = NumberUtil.adds(2,waitBackInterest,waitBackPrincipal);


        // 本金 -利息收益 -加息券收益 -红包收益 -其他
        ReturnsDetailedResponse response = returnsDetailedDao.findAmtBack(userId);

        //设置 待回款本金（含红包） 待回款利息 总待回款总额（本息）
        response.setWaitBackPrincipal(waitBackPrincipal);
        response.setWaitBackInterest(waitBackInterest);
        response.setAmtWaitBackMoney(amtWaitBackMoney);

        //利息收益 = 汇付利息收益+ 富有利息收益
        Double backInterest = NumberUtil.adds(2,response.getBackInterest(),hInterest);
        response.setBackInterest(backInterest);
        //加利息收益
        Double backInterestPro = response.getBackInterestPro();
        //红包收益
        Double backRedPacket = response.getBackRedPacket();
        //其他收益
        Double others = DEFAULT_MONEY;
        response.setOthers(others);
            //累计投资总额
        Double insertmentFuyou= returnsDetailedDao.findAmtInvestmentByUserId(userId);

        //累计已收益
        Double amtBack = NumberUtil.adds(2,backInterest,backInterestPro,backRedPacket,others);
        response.setAmtBack(amtBack);
        //利息百分比
        Double interestPercent = NumberUtil.divide(2,backInterest,amtBack);
        response.setInterestPercent(interestPercent);
        //红包百分比
        Double redPacketPercent = NumberUtil.divide(2,backRedPacket,amtBack);
        response.setRedPacketPercent(redPacketPercent);
        //加利息收益百分比
        Double interestProPercent = NumberUtil.divide(2,backInterestPro,amtBack);
        response.setInterestProPercent(interestProPercent);
        //其他收益百分比
        Double othersPercent = DEFAULT_MONEY;
        response.setOthersPercent(othersPercent);

//        Double amtReturnedMoney = returnsDetailedDao.findAmtReturnedMoney(userId);
        //累计已回款总额（本息）
        // 总金额= 累计已回款本金本金+利息
        Double amtMoney = NumberUtil.adds(2,response.getAmtPrincipal(),amtBack);
        //        累计投资总额（本金）= 汇付本金 + 富有本金
        Double amtPrincipals = NumberUtil.adds(2,insertmentFuyou,hAccount);
        response.setAmtPrincipal(amtPrincipals);
//        Double amtReturnedMoney = NumberUtil.subtract(2,amtMoney,amtWaitBackMoney);
        response.setAmtReturnedMoney(amtMoney);
        return Response.success(response);

    }
}
