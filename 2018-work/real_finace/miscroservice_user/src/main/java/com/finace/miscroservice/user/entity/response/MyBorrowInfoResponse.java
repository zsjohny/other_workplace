package com.finace.miscroservice.user.entity.response;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MyBorrowInfoResponse implements Serializable {
    /**
     * 标的id
     */
    private int id;
    /**
     * borrow.user_id
     * 用户名称
     */
    private int userId;
    /**
     * borrow.name
     * 标题
     */
    private String name;
    /**
     * borrow.status
     * 状态
     */
    private int status;
    /**
     * borrow.use
     * 用途
     */
    private String use;
    /**
     * borrow.account
     * 借贷总金额
     */
    private String account;
    /**
     * borrow.account_yes
     *
     */
    private String accountYes;
    /**
     * 利息
     */
    private String interest;
    /**
     * borrow.tender_times
     *
     */
    private String tenderTimes;
    /**
     * borrow.apr
     * 总年利率
     */
    private double apr;
    /**
     * borrow.valid_time
     * 有效时间
     */
    private String validTime;
    /**
     * borrow.addtime
     *
     */
    private String addtime;
    /**
     * borrow.time_limit_day
     *
     */
    private int timeLimitDay;

    /**
     * borrow.end_time
     * 还款时间
     */
    private String endTime;
    /**
     * borrow.success_time
     * 放款时间
     */
    private String successTime;



    private int count3;  //是否标已经满

}
