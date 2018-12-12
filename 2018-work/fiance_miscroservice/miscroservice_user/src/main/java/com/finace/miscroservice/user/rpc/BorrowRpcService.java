package com.finace.miscroservice.user.rpc;

import com.finace.miscroservice.commons.entity.BorrowTender;
import com.finace.miscroservice.commons.entity.FinanceMoney;
import com.finace.miscroservice.commons.entity.InvestRecords;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FeignClient(value = "BORROW")
public interface BorrowRpcService {

    /**
     * 根据用户id获取用户富有投资总数
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getFinanceMoneyInfo", method = RequestMethod.POST)
    public FinanceMoney getFinanceMoneyInfo(@RequestParam("userId") String userId);

    /**
     * 查询已完成收款的投标累计本金和累计收益
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getBorrowTenderMoneyInfo", method = RequestMethod.POST)
    public BorrowTender getBorrowTenderMoneyInfo(@RequestParam("userId") String userId);

    /**
     * 用户待回款笔数
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getUserFidCount", method = RequestMethod.POST)
    public int getUserFidCount(@RequestParam("userId") String userId);

    /**
     * 获取用户投资记录
     * @param userId
     * @param type
     * @param stype
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getUserInvestRecords", method = RequestMethod.POST)
    public List<InvestRecords> getUserInvestRecords(@RequestParam("userId") String userId,
                                                    @RequestParam("type") String type,
                                                    @RequestParam("stype") String stype,
                                                    @RequestParam("page") int page);

    /**
     * 获取用户回款日历信息
     * @param userId
     * @param tmonth
     * @param tday
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getReturnCalendar", method = RequestMethod.POST)
    public Map<String, Object> getReturnCalendar(@RequestParam("userId") String userId,
                                                 @RequestParam("tmonth") String tmonth,
                                                 @RequestParam("tday") String tday);

    /**
     * 获取用户第一次投资金额
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getUserFirstBuyAmt", method = RequestMethod.POST)
    public Double getUserFirstBuyAmt(@RequestParam("userId") int userId);

    /**
     * 获取银行卡限额
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getUserBankCardByUserId", method = RequestMethod.GET)
    public Map<String, String> getUserBankCardByUserId(@RequestParam("userId") String userId);
}



