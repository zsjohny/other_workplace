package com.e_commerce.miscroservice.activity.entityvo;

import com.e_commerce.miscroservice.activity.entity.channel.ChannelUser;
import lombok.Data;

/**
 * 渠道商用户
 */
@Data
public class ChannelResponse extends ChannelUser {
    private Long userCount;
    private Long orderCount;

    /**
     * 粉丝下单数
     */
    private Long fansOrderCount;
    /**
     * 粉丝总数量
     */
    private Long fansCount;

    /**
     * 订单转化率
     */
    private String orderConversionRate;


    private Long allUser;//总用户数量
    private Long allOrderCount;//所有渠道商的总订单数量
    private Long newUser;//所有渠道商下今日新增用户数量
    private Long newOrderCount;//所有渠道商下今日新增订单数量
    private Long actualCount;//下过单的用户数量

}
