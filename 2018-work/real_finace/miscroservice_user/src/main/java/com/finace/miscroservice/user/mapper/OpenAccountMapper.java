package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.entity.po.Account;
import com.finace.miscroservice.user.po.CreditGoAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OpenAccountMapper {


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
    void openAccount(@Param("txDate")String txDate,@Param("txTime")String txTime,@Param("seqNo")String seqNo,
                     @Param("userId") String userId, @Param("idType") String idType, @Param("idNo") String idNo, @Param("name") String name,
                     @Param("gender") String gender, @Param("mobile") String mobile, @Param("acctUse") String acctUse, @Param("smsFlag") String smsFlag,
                     @Param("identity") String identity, @Param("coinstName") String coinstName);

    /**
     * 查询开户信息
     * @param userId
     * @return
     */
    CreditGoAccountPO findOpenAccountByUserId(@Param("userId") String userId);

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
    void upOpenAccount(@Param("txDate")String txDate,@Param("txTime")String txTime,@Param("seqNo")String seqNo,
                       @Param("userId") String userId, @Param("idType") String idType, @Param("idNo") String idNo, @Param("name") String name,
                       @Param("gender") String gender, @Param("mobile") String mobile, @Param("acctUse") String acctUse, @Param("smsFlag") String smsFlag,
                       @Param("identity") String identity, @Param("coinstName") String coinstName);

    /**
     * 修改银行卡信息
     * @param accountId
     * @param cardNo
     * @param seqNo
     * @param txTime
     * @param txDate
     */
    void upCardNoByAccountId(@Param("accountId")String accountId, @Param("cardNo")String cardNo,
                             @Param("seqNo")String seqNo, @Param("txTime")String txTime,@Param("txDate") String txDate);

    /**
     * 根据accountId查找开户信息
     * @param accountId
     * @return
     */
    CreditGoAccountPO findOpenAccountByAccountId(@Param("accountId")String accountId);

    /**
     * 修改设置密码状态
     * @param accountId
     */
    void upSetPass(@Param("accountId")String accountId);

    /**
     * 修改设置银行卡状态
     * @param accountId
     * @param cardNo
     */
    void upCardNo(@Param("accountId")String accountId, @Param("cardNo")String cardNo);

    /**
     * 删除银行卡状态
     * @param reAccountId
     */
    void delCardNo(@Param("reAccountId")String reAccountId);

    /**
     * 修改金额
     * @param reAccountId
     * @param availBal
     * @param currBal
     * @param iceMoney
     */
    void upMoney(@Param("reAccountId")String reAccountId, @Param("availBal")Double availBal, @Param("currBal")Double currBal, @Param("iceMoney")Double iceMoney);

    /**
     * 修改 需扣款金额
     * @param accountId
     * @param txAmount
     */
    void upOpenAccountCutPayment(@Param("accountId")String accountId, @Param("txAmount")String txAmount);

    /**
     * 缴费授权
     * @param accountId
     */
    void upPaymentAuth(@Param("accountId")String accountId);

    /**
     * 还款授权
     * @param accountId
     */
    void upRepayAuth(@Param("accountId")String accountId);


    /**
     *修改账户信息
     * @param creditGoAccountPO
     */
    void updateCreditAccount(CreditGoAccountPO creditGoAccountPO);
    /**
     * 根据用户身份证查询用户开户身份证
     * @param idNo
     */
    List<String> findAccountByIdNo(@Param("idNo")String idNo);
}
