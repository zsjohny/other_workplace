package com.e_commerce.miscroservice.activity.entity.channel;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 渠道商
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/12/20 11:18
 * @Copyright 玖远网络
 */
//"yjj_channel_user"
@Data
@Table("yjj_channel_user")
public class ChannelUser{

    @Id
    private Long id;

    /**
     * 所属店铺
     */
    @Column(value = "belong_store_id", length = 20, commit = "所属门店", defaultVal = "121")
    private Long belongStoreId;

    /**
     * 用户姓名
     */
    @Column(value = "user_name", length = 50, commit = "姓名")
    private String userName;

    /**
     * 手机号
     */
    @Column(value = "phone", length = 50, commit = "电话")
    private String phone;

    /**
     * 合作状态:0终止合作,1合作中(default:1)
     */
    @Column(value = "partner_status", length = 4, commit = "0终止合作,1合作中(default:1)", defaultVal = "1")
    private Integer partnerStatus;

    /**
     * 进店二维码
     */
    @Column(value = "in_shop_qr_img", commit = "进店二维码", length = 1000)
    private String inShopQrImg;

    /**
     * 是否删除 0正常,1删除(default:0)
     */
    @Column(value = "del_status", length = 4, commit = "是否删除 0正常,1删除", defaultVal = "0")
    private Integer delStatus;

    @Column(value = "create_time", commit = "创建时间",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
    private Timestamp createTime;

    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "更新时间")
    private Timestamp updateTime;


}
