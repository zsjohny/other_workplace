package com.finace.miscroservice.official_website.rpc;

import com.finace.miscroservice.commons.entity.AccountLog;
import com.finace.miscroservice.commons.entity.CreditGoAccount;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.entity.UserBankCard;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user")
public interface UserRpcService {

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserByUserId", method = RequestMethod.GET)
    public User getUserByUserId(@RequestParam("userId") String userId);

    /**
     * 获取用户银行卡信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserBankCardByUserId", method = RequestMethod.GET)
    public UserBankCard getUserBankCardByUserId(@RequestParam("userId") String userId);

    /**
     * 根据卡号获取银行卡信息
     * @param card
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserBankCardByCard", method = RequestMethod.GET)
    public UserBankCard getUserBankCardByCard(@RequestParam("card") String card);

    /**
     * 新增银行卡信息
     * @param UserBankCard
     */
    @RequestMapping(value = "/sys/user/addUserBankCard", method = RequestMethod.POST)
    public void addUserBankCard(@RequestBody UserBankCard UserBankCard);

    /**
     * 新增资金日志记录
     * @param accountLog
     */
    @RequestMapping(value = "/sys/user/addAccountLog", method = RequestMethod.POST)
    public void addAccountLog(@RequestBody AccountLog accountLog);
    
    /**
     * 获取新增注册人数
     * @param
     */
    @RequestMapping(value = "/sys/user/getCountNewUserNum", method = RequestMethod.POST)
    public int getCountNewUserNum();

    /**
     * 获取用户账户信息
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserAccountByAccountId", method = RequestMethod.POST)
    public CreditGoAccount getUserAccountByAccountId(@RequestParam("accountId") String accountId);

    /**
     * 获取用户账户信息
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserAccountByUserId", method = RequestMethod.POST)
    public CreditGoAccount getUserAccountByUserId(@RequestParam("userId") String userId);

    /**
     * 重置交易密码
     * @param creditGoAccount
     * @param msgCode
     */
    @RequestMapping(value = "/sys/user/resetPass", method = RequestMethod.POST)
    public String resetPass(@RequestBody CreditGoAccount creditGoAccount, @RequestParam("msgCode") String msgCode);



}



