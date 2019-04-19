package com.e_commerce.miscroservice.activity.entity.channel;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/20 13:41
 * @Copyright 玖远网络
 */
//"yjj_channel_user_fans"
@Data
@Table("yjj_channel_user_fans")
public class ChannelUserFans{

    @Id
    private Long id;

    /**
     * 小程序用户表id
     */
    @Column(value = "shop_member_id", commit = "小程序用户表id", length = 20)
    private Long shopMemberId;

    /**
     * 渠道商用户id
     */
    @Column(value = "channel_user_id", commit = "渠道商用户id", length = 20)
    private Long channelUserId;


    /**
     * 小程序用户下单数量
     */
    @Column(value = "shop_member_order_count", commit = "小程序用户下单数量", length = 11, defaultVal = "0")
    private Integer shopMemberOrderCount;


    /**
     * 是否删除 0正常,1删除
     */
    @Column(value = "del_status", length = 4, commit = "是否删除 0正常,1删除", defaultVal = "0")
    private Integer delStatus;

    @Column(value = "create_time", commit = "创建时间",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
    private Timestamp createTime;

    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "更新时间")
    private Timestamp updateTime;

}
