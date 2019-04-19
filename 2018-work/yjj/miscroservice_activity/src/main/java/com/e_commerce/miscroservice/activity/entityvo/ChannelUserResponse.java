package com.e_commerce.miscroservice.activity.entityvo;

import com.e_commerce.miscroservice.activity.entity.channel.ChannelUserFans;
import lombok.Data;

@Data
public class ChannelUserResponse extends ChannelUserFans {

    private String userNickname;
    private String wxPhone;
    private Integer sex;

    /**
     * 订单转化率
     */
    private String orderConversionRate;


    private Long allUser;//总用户数量
    private Long allOrderCount;//渠道商的总订单数量
    private Long newUser;//渠道商下今日新增用户数量
    private Long newOrderCount;//渠道商下今日新增订单数量

}
