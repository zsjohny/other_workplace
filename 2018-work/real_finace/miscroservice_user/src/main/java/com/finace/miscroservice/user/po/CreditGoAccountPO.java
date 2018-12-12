package com.finace.miscroservice.user.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreditGoAccountPO implements Serializable {
    private Integer id;

    private String idType;

    private Integer userId;

    private String idNo;

    private String name;

    private String gender;

    private String mobile;

    private String cardNo;


    private String acctUse;

    private String smsFlag;

    private String identity;

    private String coinstName;

    private Integer isSetPass;

    private Integer isSetBankCard;

    private String accountId;

    private Double availBal;  //可用余额

    private String currency;

    private String seqNo;

    private String txTime;

    private String txDate;

    private String cardBankCnaps;  //联行号
    private Double cutPayment; //需要扣款金额
    private Integer payment; //缴费授权
    private Integer repay; //签约授权
}
