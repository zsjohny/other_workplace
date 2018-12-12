package com.finace.miscroservice.user.entity.po;

import com.finace.miscroservice.commons.annotation.Column;
import com.finace.miscroservice.commons.annotation.Id;
import com.finace.miscroservice.commons.annotation.Table;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

import static com.finace.miscroservice.commons.handler.DbHandler.DateGeneStrategy.CREATE;
import static com.finace.miscroservice.commons.handler.DbHandler.DateGeneStrategy.UPDATE;

@Table("credit_account")
@Data
public class Account{
    @Id
    private Integer id;

    @Column(commit = "证件类型",isNUll = false)
    private String idType;

    @Column(commit = "用户id",isNUll = false)
    private Integer userId;

    @Column(commit = "证件号",isNUll = false)
    private String idNo;

    @Column(commit = "姓名",isNUll = false)
    private String name;

    @Column(commit = "性别M男 F女",isNUll = false)
    private String gender;

    @Column(commit = "手机号",isNUll = false)
    private String mobile;

    @Column(commit = "银行卡号")
    private String cardNo;

    @Column(commit = "用途00000-普通账户 " +
            "10000-红包账户（只能有一个 " +
            "01000-手续费账户（只能有一个 " +
            "00100-担保账户",isNUll = false)
    private String acctUse;

     @Column(commit = "是否开通短信 0：不需要 1或空：需要")
    private String smsFlag;

     @Column(commit = "身份属性 1：出借角色 2：借款角色 3：代偿角色",isNUll = false)
    private String identity;

    @Column(commit = "平台名称 该平台名会在开户协议中使用，平台（存管委托人）的名称",isNUll = false)
    private String coinstName;

    @Column(commit = "是否已设置密码 默认 0 未设置 1已设置",defaultVal = "0")
    private Integer isSetPass;

     @Column(commit = "是否已设置 银行卡 默认0 未设置 1已设置",defaultVal = "0")
    private Integer isSetBankCard;

    @Column(commit = "存管平台分配的账号")
    private String accountId;
//
//    @Column(commit = "可用余额" ,defaultVal = "0")
//    private Double availBal;
//
//    @Column(commit = "账面余额" ,defaultVal = "0")
//    private Double currBal;
//
//    @Column(commit = "冻结金额" ,defaultVal = "0")
//    private Double iceMoney;
    @Column(commit = "需要扣款金额" ,defaultVal = "0",precision = 2)
    private Double cutPayment;

    @Column(commit = "交易币种 默认：156 人民币" ,defaultVal = "156")
    private String currency;

    @Column(commit = "创建时间",dateGeneStrategy =CREATE )
    private Timestamp createTime;

    @Column(commit = "修改时间",dateGeneStrategy =UPDATE )
    private Timestamp modifyTime;

    @Column(commit = "是否删除 default=0 删除 1 不删除",defaultVal = "0")
    private Integer deleted;


    @Column(commit = "交易流水号")
    private String seqNo;

    @Column(commit = "交易时间")
    private String txTime;

    @Column(commit = "交易日期")
    private String txDate;

    @Column(commit = "人民银行分配的12位联行号 routeCode=2，必输")
    private String cardBankCnaps;

    @Column(commit = "签约最高金额",precision = 2)
    private Double maxAmt;

    @Column(commit = "签约状态")
    private String txState;
    @Column(commit = "签约日期")
    private String txnDate;
    @Column(commit = "签约时间")
    private String txnTime;
    @Column(commit = "签约到期日")
    private String deadline;
    @Column(commit = "签约取消日期")
    private String cancelDate;
    @Column(commit = "签约取消时间")
    private String cancelTime;

    @Column(commit = "缴费授权",defaultVal = "0")
    private Integer payment;

    @Column(commit = "还款授权",defaultVal = "0")
    private Integer repay;




}
