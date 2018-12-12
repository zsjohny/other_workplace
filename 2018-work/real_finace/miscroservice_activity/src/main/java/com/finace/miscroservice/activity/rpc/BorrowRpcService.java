package com.finace.miscroservice.activity.rpc;

import com.finace.miscroservice.commons.entity.Borrow;
import com.finace.miscroservice.commons.entity.BorrowTender;
import com.finace.miscroservice.commons.entity.FinanceMoney;
import com.finace.miscroservice.commons.entity.InvestRecords;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "BORROW")
public interface BorrowRpcService {

    /**
     * 根据id获取标信息
     * @param borrowId
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getBorrowById", method = RequestMethod.POST)
    public Borrow getBorrowById(@RequestParam("borrowId") int borrowId);


    /**
     * 获取用户第一次投资金额
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getUserFirstBuyAmt", method = RequestMethod.POST)
    public Double getUserFirstBuyAmt(@RequestParam("userId") int userId);

    /**
     * 根据用户id和时间查询该时间段内投资金额
     * @param userId
     * @param starttime
     * @param endtime
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getUserAmtMoneyInTime", method = RequestMethod.POST)
    public Double getUserAmtMoneyInTime(@RequestParam("userId")Integer userId,@RequestParam("starttime")String starttime,@RequestParam("endtime")String endtime);

}



