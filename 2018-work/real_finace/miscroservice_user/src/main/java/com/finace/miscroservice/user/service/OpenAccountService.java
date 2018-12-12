package com.finace.miscroservice.user.service;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.user.entity.po.CreditAccountLog;
import com.finace.miscroservice.user.po.CreditGoAccountPO;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface OpenAccountService {
    /**
     * 开户页面
     * @param idNo 身份证
     * @param name 姓名
     * @param acctUse
     */
    String openAccount(String userId, String idNo, String name, String mobile, String acctUse, String identity);

    /**
     * 根据用户id查找 开户信息
     * @param userId
     * @return
     */
    CreditGoAccountPO findOpenAccountByUserId(String userId);

    /**
     * 设置密码
     * @param account
     * @return
     */
    String setPass(CreditGoAccountPO account);

    /**
     * 根据accountId修改 银行卡信息等
     * @param accountId
     * @param cardNo
     * @param seqNo
     * @param txTime
     * @param txDate
     */
    void upCardNoByAccountId(String accountId, String cardNo, String seqNo, String txTime, String txDate);

    /**
     * 根据 accountId 查询开户信息
     * @param accountId
     * @return
     */
    CreditGoAccountPO findOpenAccountByAccountId(String accountId);

    /**
     * 业务回调
     */
    void openAccountNotify(String bgData, HttpServletResponse response);

    /**
     * 根据银行卡信息 查询电子账户
     * @param userId
     * @param idNo
     * @param idType
     * @return
     */
    Map findAccountIdByIdNo(String userId, String idNo, String idType);

    /**
     * 重置密码
     * @param account
     * @param msgCode
     * @return
     */
    String resetPass(CreditGoAccountPO account, String msgCode);

    /**
     * 绑定银行卡号
     * @param account
     * @param ip
     * @return
     */
    String bindCard(CreditGoAccountPO account, String ip);

    /**
     * 解绑银行卡号
     * @param account
     * @return
     */
    Map unbindCard(CreditGoAccountPO account);
    /**
     * 充值
     * @param account
     * @return
     */
    String directRecharge(CreditGoAccountPO account, Double txAmount);

    /**
     * 查询绑卡关系
     * @param accountId
     * @param state
     * @return
     */
    Map cardBindDetailsQuery(String accountId, String state);

    /**
     * 电子账户查询
     * @param accountId
     * @return
     */
    Map balanceQuery(String accountId);

    /**
     * 发送短信
     * @param srvTxCode
     * @param phone
     * @param smsType
     * @param account
     */
    void smsCodeApply(Integer srvTxCode, String phone, String smsType, CreditGoAccountPO account);

    /**
     * 提现
     * @param account
     * @param txAmount
     * @param routeCode
     * @return
     */
    String creditWithdrow(CreditGoAccountPO account, Double txAmount, String routeCode);

    /**
     * 修改手机号
     * @param phone
     * @param smsCode
     * @param account
     * @return
     */
    Response updatePhone(String phone, String smsCode, CreditGoAccountPO account);

    /**
     * 缴费授权
     * @param account
     * @return
     */
    String paymentAuthPage(CreditGoAccountPO account);

    /**
     * 还款授权
     * @param account
     * @return
     */
    String repayAuthPage(CreditGoAccountPO account);

    /**
     * 提现前 查询
     * @param userId
     * @param account
     * @return
     */
    JSONObject withdrawPre(String userId, CreditGoAccountPO account);
    /**
     * P2P产品缴费授权解约
     * @return
     */
    Map paymentAuthCancel(CreditGoAccountPO account);

    /**
     * 保存credit_account_log 日志
     * @param accountLog
     */
    void saveCallBack(CreditAccountLog accountLog);

    /**
     * 单笔资金类业务交易查询
     * @param seqNo
     * @param txTime
     */
    void upOpenAccountLogBySeqNo(String seqNo,String txTime);

    /**
     *修改账户信息
     * @param creditGoAccountPO
     */
    void updateCreditAccount(CreditGoAccountPO creditGoAccountPO);

    /**
     * 重新 查询cardNo
     * @param accountId
     * @param i
     * @return
     */
    String reFindCardNo(String accountId, int i);

    /**
     * 开户前 调用 验证身份证
     * @param idNo
     * @param userId
     * @return
     */
    Response findOpenAccountByIdNo(String idNo, String userId);
}
