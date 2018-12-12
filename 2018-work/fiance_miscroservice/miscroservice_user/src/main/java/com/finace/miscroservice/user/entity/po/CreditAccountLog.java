package com.finace.miscroservice.user.entity.po;

import com.finace.miscroservice.commons.annotation.Column;
import com.finace.miscroservice.commons.annotation.Id;
import com.finace.miscroservice.commons.annotation.Table;
import com.finace.miscroservice.commons.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Table("credit_account_log")
public class CreditAccountLog {

    @Id
    private Integer id;

    @Column(dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE, commit = "创建时间")
    private Timestamp createTime;

    @Column(dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "修改时间")
    private Timestamp modifyTime;

    @Column(commit = "是否删除  0 默认不删除 1删除", defaultVal = "0")
    private Integer deleted;

    @Column(commit = "用户id")
    private Integer userId;

    @Column(commit = "accountId")
    private String accountId;

    @Column(commit = "txCode")
    private String txCode;

    @Column(commit = "账面余额",precision = 2)
    private Double availBal;

    @Column(commit = "操作金额",precision = 2)
    private Double txMoney;

    @Column(commit = "具体内容")
    private String content;

    @Column(commit = "交易流水号")
    private String seqNo;

    @Column(commit = "交易日期")
    private String txDate;

    @Column(commit = "交易时间")
    private String txTime;

    @Column(commit = "是否成功 0 成功 1 失败 2 操作中")
    private Integer isSuccess;


}
