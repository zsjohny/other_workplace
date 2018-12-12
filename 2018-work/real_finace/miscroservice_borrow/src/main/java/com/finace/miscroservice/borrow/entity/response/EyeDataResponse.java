package com.finace.miscroservice.borrow.entity.response;


import lombok.Data;
import lombok.ToString;

/**
 * 天眼返回数据
 */
@Data
@ToString
public class EyeDataResponse {

    private String mobile;   //用户注册手机号
    private String username; //用户注册用户名
    private String reg_time; //注册时间戳
    private String order_id; //订单号
    private Integer status;  //1已投资 2已起息 3流标 4已还款

    private Double amount;   //投资金额
    private String bid_id;   //标的ID
    private String bid_name; //标的名称
    private Double rate;     //标的利率%数额部分
    private Integer pay_way; // 还款方式 1 等额本息;2 月还息到期还本;3 到期还本息;4 等额本金;6 利息复投;7 等本等息;8 固定付息日;9 按月付息按季等额本金;10 按日付息到期还本;11 按季付息到期还本;12 满标付息到期还本

    private Integer period;    //投资期限数额
    private Integer period_type;  //1月  2天
    private Long trade_time; //投资时间时间戳（秒）
    private Long start_time; //起息时间时间戳（秒），状态起息时必填

    private String reward;   //奖励数额
    private String reward_type; //1百分比/% 2金额/元
    private String cost;     //利息管理费 %数额部分
    private String is_bill;  //是否计费标投资 1是 0否

    private String couponAmt;
    private String couponRate;
    private String userId;


}
