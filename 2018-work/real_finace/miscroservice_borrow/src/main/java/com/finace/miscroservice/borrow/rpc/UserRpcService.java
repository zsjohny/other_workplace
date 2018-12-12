package com.finace.miscroservice.borrow.rpc;

import com.finace.miscroservice.commons.entity.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
     * 根据用户id获取用户手机号码
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserPhoneByUserId", method = RequestMethod.POST)
    public String getUserPhoneByUserId(@RequestParam("userId") String userId);

    /**
     * 添加到消息中心
     * @param userId 用户id
     * @param msgCode 消息code  根据MsgCodeMenu取
     * @param topic 标题
     * @param msg 内容  -- text  /  url
     */
    @RequestMapping(value = "/sys/user/addMsg", method = RequestMethod.POST)
    void addMsg(@RequestParam("userId")Integer userId,@RequestParam("msgCode")Integer msgCode,
                @RequestParam("topic")String topic,@RequestParam("msg")String msg);

    /**
     * 根据用户手机号码获取用户信息
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserByPhone", method = RequestMethod.GET)
    public User getUserByPhone(@RequestParam("phone") String phone);


    /**
     * 获取借款人信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/user/getBorrowerInfo", method = RequestMethod.POST)
    public BorrowerInfo getBorrowerInfo(@RequestParam("userId") String userId);

    /**
     * app首页消息通知
     * @return
     */
    @RequestMapping(value = "/sys/user/getAppIndexMsg", method = RequestMethod.POST)
    public List<Msg> getAppIndexMsg();

    /**
     * 获取用户账户信息
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserAccountByUserId", method = RequestMethod.POST)
    public CreditGoAccount getUserAccountByUserId(@RequestParam("userId") String userId);

    /**
     * 获取用户账户信息
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserAccountByAccountId", method = RequestMethod.POST)
    public CreditGoAccount getUserAccountByAccountId(@RequestParam("accountId") String accountId);

    /**
     *
     * @param
     */
    @RequestMapping(value = "/sys/user/saveCallBack", method = RequestMethod.POST)
    public void saveCallBack(@RequestBody AccountLogPo accountLogPo);


    /**
     * 修改账户信息
     * @param
     */
    @RequestMapping(value = "/sys/user/updateCreditAccount", method = RequestMethod.POST)
    public void updateCreditAccount(@RequestBody CreditGoAccount creditGoAccount);

    /**
     * 根据用户id获取用户协议支付银行卡信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/user/getAgreeEnableCardByUserId", method = RequestMethod.GET)
    public UserBankCard getAgreeEnableCardByUserId(@RequestParam("userId") String userId);

    /**
     * 根据卡号获取用户协议支付银行卡信息
     * @param card
     * @return
     */
    @RequestMapping(value = "/sys/user/getAgreeBaknCardByCard", method = RequestMethod.GET)
    public UserBankCard getAgreeBaknCardByCard(@RequestParam("card") String card);

}



