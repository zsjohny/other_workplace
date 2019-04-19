package com.jiuy.rb.util;

import lombok.Data;

import java.util.List;

/**
 * 发送给谁
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/2 15:29
 * @Copyright 玖远网络
 */
@Data
public class CouponWho {

    /**
     * 指定用户id
     */
    private String userIds;
    /**
     * 注册时间开始,
     */
    private String registerTimeBegin;
    /**
     * 注册时间范围结束
     */
    private String registerTimeEnd;
    /**
     * 购买金额范围开始
     */
    private String buyTotalMoneyMin;
    /**
     * 购买金额范围结束
     */
    private String buyTotalMoneyMax;
    /**
     * 累计购买件数 范围开始
     */
    private String buyTotalCountMin;
    /**
     * 累计购买件数范围结束
     */
    private String buyTotalCountMax ;
    /**
     * 多久未登陆了
     */
    private String notLogin;
    /**
     * 供应商id
     */
    private String supplierIds;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户dis
     */
    private List<String> userIdList ;

    /**
     * 供应商list
     */
    private List<String> supplierIdList;

    /**
     * 字符串的storeIds 前端传递过来的
     */
    private String storeIds;
    /**
     * 门店宝ids 用于筛选小程序用户
     */
    private List<String> storeIdsList;

}
