package com.finace.miscroservice.commons.entity;

import com.finace.miscroservice.commons.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class AccountLogPo {

    private Integer id;

    private Timestamp createTime;

    private Timestamp modifyTime;

    private Integer deleted;

    private Integer userId;

    private String accountId;

    private String txCode;

    private Double availBal;

    private Double txMoney;

    private String content;

    private String seqNo;

    private String txDate;

    private String txTime;

    private Integer isSuccess;

}
