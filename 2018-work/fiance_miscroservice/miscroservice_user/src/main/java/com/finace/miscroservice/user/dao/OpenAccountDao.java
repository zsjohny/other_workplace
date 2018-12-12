package com.finace.miscroservice.user.dao;

import com.finace.miscroservice.user.entity.po.Account;
import com.finace.miscroservice.user.po.CreditGoAccountPO;

import java.util.List;

public interface OpenAccountDao {

    /**
     * 开户信息添加
     * @param txDate
     * @param txTime
     * @param seqNo
     * @param userId
     * @param idType
     * @param idNo
     * @param name
     * @param gender
     * @param mobile
     * @param acctUse
     * @param smsFlag
     * @param identity
     * @param coinstName
     */
    void openAccount(String txDate,String txTime,String seqNo,String userId, String idType, String idNo, String name, String gender, String mobile, String acctUse, String smsFlag, String identity,  String coinstName);
    /**
     * 查询开户信息
     * @param userId
     * @return
     */
    CreditGoAccountPO findOpenAccountByUserId(String userId);
    /**
     * 修改开户信息
     * @param txDate
     * @param txTime
     * @param seqNo
     * @param userId
     * @param idType
     * @param idNo
     * @param name
     * @param gender
     * @param mobile
     * @param acctUse
     * @param smsFlag
     * @param identity
     * @param coinstName
     */
    void upOpenAccount(String txDate,String txTime,String seqNo,String userId, String idType, String idNo, String name, String gender, String mobile, String acctUse, String smsFlag, String identity,  String coinstName);
    /**
     * 修改银行卡信息
     * @param accountId
     * @param cardNo
     * @param seqNo
     * @param txTime
     * @param txDate
     */
    void upCardNoByAccountId(String accountId, String cardNo, String seqNo, String txTime, String txDate);
    /**
     * 根据accountId查找开户信息
     * @param accountId
     * @return
     */
    CreditGoAccountPO findOpenAccountByAccountId(String accountId);
    /**
     * 修改设置密码状态
     * @param accountId
     */
    void upSetPass(String accountId);
    /**
     * 修改设置银行卡状态
     * @param accountId
     * @param cardNo
     */
    void upCardNo(String accountId, String cardNo);
    /**
     * 删除银行卡状态
     * @param reAccountId
     */
    void delCardNo(String reAccountId);
    /**
     * 修改金额
     * @param reAccountId
     * @param availBal
     * @param currBal
     * @param iceMoney
     */
    void upMoney(String reAccountId, Double availBal, Double currBal, Double iceMoney);

    /**
     * 修改 需扣款金额
     * @param accountId
     * @param txAmount
     */
    void upOpenAccountCutPayment(String accountId, String txAmount);

    /**
     * 缴费授权
     * @param accountId
     */
    void upPaymentAuth(String accountId);

    /**
     * 还款授权
     * @param accountId
     */
    void upRepayAuth(String accountId);

    /**
     *修改账户信息
     * @param creditGoAccountPO
     */
    void updateCreditAccount(CreditGoAccountPO creditGoAccountPO);

    /**
     * 根据用户身份证 查询开户身份证
     * @param idNo
     * @return
     */
    List<String> findAccountByIdNo(String idNo);
}
