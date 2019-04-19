package com.e_commerce.miscroservice.activity.entity.channel;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 渠道商统计表
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/12/20 13:36
 * @Copyright 玖远网络
 */

//"yjj_channel_user_gather"
@Data
@Table("yjj_channel_user_gather")
public class ChannelUserGather{

    @Id
    private Long id;

    /**
     * 渠道商用户id
     */
    @Column(value = "channel_user_id", commit = "渠道商用户id", length = 20)
    private Long channelUserId;

    /**
     * 粉丝总数量
     */
    @Column(value = "fans_count", commit = "粉丝总数", length = 20, defaultVal = "0")
    private Long fansCount;

    /**
     * 下过单的粉丝数
     * <P>
     *    orderFansCount/fansCount=订单转化率
     * </P>
     */
    @Column(value = "order_fans_count", commit = "下过单的粉丝数", length = 20, defaultVal = "0")
    private Long orderFansCount;

    /**
     * 粉丝下单数
     */
    @Column(value = "fans_order_count", commit = "下过单的粉丝数", length = 20, defaultVal = "0")
    private Long fansOrderCount;

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
