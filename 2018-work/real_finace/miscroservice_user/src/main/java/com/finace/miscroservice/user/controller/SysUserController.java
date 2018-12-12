package com.finace.miscroservice.user.controller;


import com.finace.miscroservice.commons.annotation.Auth;
import com.finace.miscroservice.commons.annotation.InnerRestController;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.entity.*;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.user.entity.po.CreditAccountLog;
import com.finace.miscroservice.user.entity.response.MsgResponse;
import com.finace.miscroservice.user.po.*;
import com.finace.miscroservice.user.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统间调用用户控制类
 */
@InnerRestController
@Auth
@RequestMapping("/sys/user/*")
public class SysUserController extends BaseController {
    private Log logger = Log.getInstance(SysUserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserBankCardService userBankCardService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BorrowerInfoService borrowerInfoService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private OpenAccountService openAccountService;


    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getUserByUserId", method = RequestMethod.GET)
    public User getUserByUserId(@RequestParam("userId") String userId) {
        try {
            //拷贝属性值
            //
            UserPO userPo = this.userService.findUserOneById(userId);
            if (null != userPo) {
                User user = new User();

                BeanUtils.copyProperties(userPo, user);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户手机号码获取用户信息
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "getUserByPhone", method = RequestMethod.GET)
    public User getUserByPhone(@RequestParam("phone") String phone) {
        try {
            UserPO userPo = this.userService.getUserByUserPhone(phone);
            if (null != userPo) {
                User user = new User();
                BeanUtils.copyProperties(userPo, user);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户银行卡信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getUserBankCardByUserId", method = RequestMethod.GET)
    public UserBankCard getUserBankCardByUserId(@RequestParam("userId") String userId) {
        try {
            UserBankCardPO userBankCardPO = this.userBankCardService.getUserEnableCardByUserId(userId);
            if (null != userBankCardPO) {
                UserBankCard userBankCard = new UserBankCard();
                BeanUtils.copyProperties(userBankCardPO, userBankCard);
                return userBankCard;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据卡号获取银行卡信息
     *
     * @param card
     * @return
     */
    @RequestMapping(value = "getUserBankCardByCard", method = RequestMethod.GET)
    public UserBankCard getUserBankCardByCard(@RequestParam("card") String card) {
        try {
            UserBankCardPO userBankCardPO = this.userBankCardService.getBaknCardByCard(card);
            if (null != userBankCardPO) {
                UserBankCard userBankCard = new UserBankCard();
                BeanUtils.copyProperties(userBankCardPO, userBankCard);
                return userBankCard;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据卡号获取用户协议支付银行卡信息
     * @param card
     * @return
     */
    @RequestMapping(value = "getAgreeBaknCardByCard", method = RequestMethod.GET)
    public UserBankCard getAgreeBaknCardByCard(@RequestParam("card") String card) {
        try {
            UserBankCardPO userBankCardPO = this.userBankCardService.getAgreeBaknCardByCard(card);
            if (null != userBankCardPO) {
                UserBankCard userBankCard = new UserBankCard();
                BeanUtils.copyProperties(userBankCardPO, userBankCard);
                return userBankCard;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据用户id获取用户协议支付银行卡信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "getAgreeEnableCardByUserId", method = RequestMethod.GET)
    public UserBankCard getAgreeEnableCardByUserId(@RequestParam("userId") String userId) {
        try {
            UserBankCardPO userBankCardPO = this.userBankCardService.getAgreeEnableCardByUserId(userId);
            if (null != userBankCardPO) {
                UserBankCard userBankCard = new UserBankCard();
                BeanUtils.copyProperties(userBankCardPO, userBankCard);
                return userBankCard;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增银行卡信息
     *
     * @param UserBankCard
     */
    @RequestMapping(value = "addUserBankCard", method = RequestMethod.POST)
    public void addUserBankCard(@RequestBody UserBankCard UserBankCard) {
        UserBankCardPO userBankCardPO = new UserBankCardPO();
        try {
            BeanUtils.copyProperties(UserBankCard, userBankCardPO);
            userBankCardService.addUserBankCard(userBankCardPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增资金日志记录
     *
     * @param accountLog
     */
    @RequestMapping(value = "addAccountLog", method = RequestMethod.POST)
    public void addAccountLog(@RequestBody AccountLog accountLog) {
        AccountLogPO accountLogPO = new AccountLogPO();
        try {
            //拷贝属性值
            BeanUtils.copyProperties(accountLog, accountLogPO);
            accountService.addAccountLog(accountLogPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据邀请人id获取被邀请人列表
     *
     * @param inviter
     * @return
     */
    @RequestMapping(value = "getUserListByInviter", method = RequestMethod.POST)
    public List<User> getUserListByInviter(@RequestParam("inviter") int inviter,
                                           @RequestParam("page") int page) {
        List<User> list = new ArrayList<>();
        try {

            List<UserPO> userPOList = userService.getUserListByInviter(inviter, page);
            for (UserPO userPO : userPOList) {
                User user = new User();
                BeanUtils.copyProperties(userPO, user);
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 根据邀请人id获取被邀请人总数
     *
     * @param inviter
     * @return
     */
    @RequestMapping(value = "getUserCountByInviter", method = RequestMethod.POST)
    public int getUserCountByInviter(@RequestParam("inviter") int inviter) {
        return userService.getUserCountByInviter(inviter);
    }

    /**
     * 根据邀请人id 活动时间 获取被邀请人列表
     *
     * @param inviter
     * @return
     */
    @RequestMapping(value = "getUsersByInviterInTime", method = RequestMethod.POST)
    public List<User> getUsersByInviterInTime(@RequestParam("inviter") int inviter,
                                           @RequestParam("page") int page, @RequestParam("starttime") String starttime,
                                              @RequestParam("endtime") String endtime) {
        List<User> list = new ArrayList<>();
        try {

            List<UserPO> userPOList = userService.getUsersByInviterInTime(inviter, page,starttime,endtime);
            for (UserPO userPO : userPOList) {
                User user = new User();
                BeanUtils.copyProperties(userPO, user);
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 根据邀请人id 活动时间 获取被邀请人总数
     *
     * @param inviter
     * @return
     */
    @RequestMapping(value = "getUserCountByInviterInTime", method = RequestMethod.POST)
    public int getUserCountByInviterInTime(@RequestParam("inviter") int inviter, @RequestParam("starttime") String starttime,
                                           @RequestParam("endtime") String endtime) {
        return userService.getUserCountByInviterInTime(inviter,starttime,endtime);
    }

    /**
     * 获取新增注册人数
     *
     * @param
     * @return
     */
    @RequestMapping(value = "getCountNewUserNum", method = RequestMethod.POST)
    public int getCountNewUserNum() {
        return userService.getCountNewUserNum();
    }

    /**
     * 根据用户id获取用户手机号码
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getUserPhoneByUserId", method = RequestMethod.POST)
    public String getUserPhoneByUserId(@RequestParam("userId") String userId) {
        return userService.getUserPhoneByUserId(userId);
    }

    /**
     * 添加消息
     *
     * @param userId  用户id
     * @param msgCode 消息类型
     * @param topic   标题
     * @param msg     消息
     */
    @RequestMapping(value = "addMsg", method = RequestMethod.POST)
    public void addMsg(@RequestParam("userId") Integer userId, @RequestParam("msgCode") Integer msgCode,
                       @RequestParam("topic") String topic, @RequestParam("msg") String msg) {
        userService.addMsg(userId, msgCode, topic, msg, System.currentTimeMillis());
    }


    /**
     * 获取借款人信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "getBorrowerInfo", method = RequestMethod.POST)
    public BorrowerInfo getBorrowerInfo(@RequestParam("userId") String userId) {
        BorrowerInfo borrowerInfo = new BorrowerInfo();
        try {
            BorrowerInfoPO borrowerInfoPO = borrowerInfoService.getBorrowerInfoByUserId(userId);
            if(borrowerInfoPO != null){
                //拷贝属性值
                BeanUtils.copyProperties(borrowerInfoPO, borrowerInfo);
                return borrowerInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * app首页消息通知
     * @return
     */
    @RequestMapping(value = "getAppIndexMsg", method = RequestMethod.POST)
    public List<Msg> getAppIndexMsg() {
        List<Msg> alist = new ArrayList<>();
        try {
            List<MsgResponse> list = msgService.getAppIndexMsg();
            if( list.size() > 0 && list != null){
                for (MsgResponse msgResponse : list){
                    Msg msg = new Msg();
                    //拷贝属性值
                    BeanUtils.copyProperties(msgResponse, msg);
                    alist.add(msg);
                }
            }
            return alist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取用户账户信息
     * @return
     */
    @RequestMapping(value = "getUserAccountByUserId", method = RequestMethod.POST)
    public CreditGoAccount getUserAccountByUserId(@RequestParam("userId") String userId) {
        try {
            CreditGoAccount creditGoAccount = new CreditGoAccount();
            CreditGoAccountPO creditGoAccountPO = openAccountService.findOpenAccountByUserId(userId);

            BeanUtils.copyProperties(creditGoAccountPO, creditGoAccount);
            return creditGoAccount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户账户信息
     * @return
     */
    @RequestMapping(value = "getUserAccountByAccountId", method = RequestMethod.POST)
    public CreditGoAccount getUserAccountByAccountId(@RequestParam("accountId") String accountId) {
        try {
            CreditGoAccount creditGoAccount = new CreditGoAccount();
            CreditGoAccountPO creditGoAccountPO = openAccountService.findOpenAccountByAccountId(accountId);

            BeanUtils.copyProperties(creditGoAccountPO, creditGoAccount);
            return creditGoAccount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 资金流水日志
     * @return
     */
    @RequestMapping(value = "saveCallBack", method = RequestMethod.POST)
    public void saveCallBack(@RequestBody AccountLogPo accountLogPo){
        CreditAccountLog accountLog = new CreditAccountLog();
        //拷贝属性值
        BeanUtils.copyProperties(accountLogPo, accountLog);
        openAccountService.saveCallBack(accountLog);
    }


    /**
     * 修改账户信息
     * @param
     */
    @RequestMapping(value = "updateCreditAccount", method = RequestMethod.POST)
    public void updateCreditAccount(@RequestBody CreditGoAccount creditGoAccount){
        CreditGoAccountPO creditGoAccountPO = new CreditGoAccountPO();
        BeanUtils.copyProperties(creditGoAccount, creditGoAccountPO);
        openAccountService.updateCreditAccount(creditGoAccountPO);
    }


    /**
     * 重置交易密码
     * @param creditGoAccount
     * @param msgCode
     */
    @RequestMapping(value = "resetPass", method = RequestMethod.POST)
    public String resetPass(@RequestBody CreditGoAccount creditGoAccount, @RequestParam("msgCode") String msgCode){
        CreditGoAccountPO creditGoAccountPO = new CreditGoAccountPO();
        BeanUtils.copyProperties(creditGoAccount, creditGoAccountPO);
        return openAccountService.resetPass(creditGoAccountPO, msgCode);
    }


}
