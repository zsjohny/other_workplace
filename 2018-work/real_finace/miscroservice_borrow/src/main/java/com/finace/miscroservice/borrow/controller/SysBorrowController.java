package com.finace.miscroservice.borrow.controller;


import com.finace.miscroservice.borrow.entity.response.DataCollectionResponse;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.BorrowService;
import com.finace.miscroservice.borrow.service.BorrowTenderService;
import com.finace.miscroservice.borrow.service.FinanceBidService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouBinCardQueryService;
import com.finace.miscroservice.commons.annotation.Auth;
import com.finace.miscroservice.commons.annotation.InnerRestController;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.entity.*;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统间调用,标的控制类
 */
@InnerRestController
@RequestMapping("/sys/borrow/*")
@Auth
public class SysBorrowController extends BaseController {

    private Log logger = Log.getInstance(SysBorrowController.class);
    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private FinanceBidService financeBidService;

    @Autowired
    private BorrowTenderService borrowTenderService;

    @Autowired
    private FuiouBinCardQueryService fuiouBinCardQueryService;

    @Autowired
    private UserRpcService userRpcService;


    /**
     * 根据用户id获取用户富有投资总数
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getFinanceMoneyInfo", method = RequestMethod.POST)
    public FinanceMoney getFinanceMoneyInfo(@RequestParam("userId") String userId) {
        return financeBidService.getFinanceMoneyInfo(userId);
    }

    /**
     * 查询已完成收款的投标累计本金和累计收益
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getBorrowTenderMoneyInfo", method = RequestMethod.POST)
    public BorrowTender getBorrowTenderMoneyInfo(@RequestParam("userId") String userId) {
        return borrowTenderService.getUserTotalPast(userId);
    }

    /**
     * 用户待回款笔数
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getUserFidCount", method = RequestMethod.POST)
    public Integer getUserFidCount(@RequestParam("userId") String userId) {
        return financeBidService.getUserFidCount(userId);
    }

    /**
     * 获取用户投资记录
     *
     * @param userId
     * @param type
     * @param stype
     * @return
     */
    @RequestMapping(value = "getUserInvestRecords", method = RequestMethod.POST)
    public List<InvestRecords> getUserInvestRecords(@RequestParam("userId") String userId,
                                                    @RequestParam("type") String type,
                                                    @RequestParam("stype") String stype,
                                                    @RequestParam("page") int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("type", type);
        map.put("stype", stype);
        return financeBidService.getUserInvestRecords(map, page);
    }

    /**
     * 获取用户回款日历信息
     *
     * @param userId
     * @param tmonth
     * @param tday
     * @return
     */
    @RequestMapping(value = "getReturnCalendar", method = RequestMethod.POST)
    public Map<String, Object> getReturnCalendar(@RequestParam("userId") String userId,
                                                 @RequestParam("tmonth") String tmonth,
                                                 @RequestParam("tday") String tday) {
        return financeBidService.getReturnCalendar(userId, tday, tmonth);
    }

    /**
     * 根据id获取标信息
     *
     * @param borrowId
     * @return
     */
    @RequestMapping(value = "getBorrowById", method = RequestMethod.POST)
    public Borrow getBorrowById(@RequestParam("borrowId") int borrowId) {
        BorrowPO borrowPO = borrowService.getBorrowById(borrowId);
        if (null != borrowPO) {
            try {
                Borrow borrow = new Borrow();
                BeanUtils.copyProperties(borrowPO, borrow);
                return borrow;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取用户第一次投资金额
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getUserFirstBuyAmt", method = RequestMethod.POST)
    public Double getUserFirstBuyAmt(@RequestParam("userId") int userId) {
        return financeBidService.getUserFirstBuyAmt(userId);
    }

    /**a
     * 查看用户总共投资
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getAllFinaceByUserId", method = RequestMethod.POST)
    public Double getAllFinaceByUserId(@RequestParam("userId") int userId) {
        return financeBidService.getAllFinaceByUserId(userId);
    }

    /**
     * 数据明细
     * @return
     */
    @RequestMapping(value = "getDataCollection", method = RequestMethod.POST)
    public DataCollectionResponse getDataCollection() {
        return borrowService.dataCollectionChange();
    }


    /**
     * 获取银行卡限额
     * @return
     */
    @RequestMapping(value = "getUserBankCardByUserId", method = RequestMethod.GET)
    public Map<String, String> getUserBankCardByUserId(@RequestParam("userId") String userId) {
        UserBankCard userBankCard = userRpcService.getUserBankCardByUserId(userId);
        if (userBankCard != null && StringUtil.isNotEmpty(userBankCard.getBankCard())) {
            Map<String, String> map = new HashMap<>();
            map.put("shortCard", userBankCard.getBankCard().substring(userBankCard.getBankCard().length() - 4, userBankCard.getBankCard().length()));
            map.put("bankCardName", userBankCard.getBankName());
            map.put("bankCard", userBankCard.getBankCard());
            logger.info("用户{}银行卡信息,银行卡名称={},银行卡卡号={}", userId, userBankCard.getBankName(), userBankCard.getBankCard());
            if (null != userBankCard.getInscd()) {
                FuiouBinCardQueryService.AmtResultLimit amtResultLimit = fuiouBinCardQueryService.queryLimitByCard(userBankCard.getInscd());
                if (null != amtResultLimit) {
                    logger.info("用户{}, 银行卡卡号={},ymoney={},dmoney={}", userId, userBankCard.getBankCard(), amtResultLimit.getAmtlimittime(), amtResultLimit.getAmtlimitday());
                    map.put("ymoney", String.valueOf(NumberUtil.round(Double.valueOf(amtResultLimit.getAmtlimittime()) / 1000000, 0)));  //单笔限额
                    map.put("dmoney", String.valueOf(NumberUtil.round(Double.valueOf(amtResultLimit.getAmtlimitday()) / 1000000, 0)));  //每天限额
                }
            }
            return map;
        }
        return null;
    }
    /**
     * 根据用户id和时间查询该时间段内投资金额
     * @param userId
     * @param starttime
     * @param endtime
     * @return
     */
    @RequestMapping(value = "getUserAmtMoneyInTime", method = RequestMethod.POST)
    public Double getUserAmtMoneyInTime(@RequestParam("userId")Integer userId,@RequestParam("starttime")String starttime,@RequestParam("endtime")String endtime){
       return borrowService.getUserAmtMoneyInTime(userId,starttime,endtime);
    }


}
