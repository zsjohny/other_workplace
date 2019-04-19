package com.e_commerce.miscroservice.product.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/11 9:31
 * @Copyright 玖远网络
 */
@Data
@Table("yjj_live_product")
public class LiveProduct {

    @Id
    private Long id;

    /**
     * 小程序商品id
     */
    @Column(value = "shop_product_id", commit = "小程序商品id", length = 20, defaultVal = "0")
    private Long shopProductId;

    /**
     * APP平台商品id
     */
    @Column(value = "supplier_product_id", commit = "APP平台商品id", length = 20, defaultVal = "0")
    private Long supplierProductId;

    /**
     * 类型,0平台供应商商品, 1是用户自定义款，2用户自营平台同款,10APP直播商品
     */
    @Column(value = "type", length = 4, isNUll = false, commit = "类型,0平台供应商商品, 1是用户自定义款，2用户自营平台同款,10APP直播商品")
    private Integer type;

    /**
     * 直播价
     */
    @Column(value = "live_price", commit = "直播价", length = 11,precision = 2,defaultVal = "0.00")
    private Double livePrice;

    /**
     * 排序/默认创建时的时间戳
     */
    @Column( value = "sort_no", length = 20, commit = "排序/默认创建时的时间戳", defaultVal = "0" )
    private Long sortNo;

    /**
     * 直播间编号
     */
    @Column(value = "room_num",length = 20,commit = "房间号", defaultVal = "0")
    private Long roomNum;

    /**
     * 主播id
     */
    @Column(value = "anchor_id", commit = "主播id", length = 20, isNUll = false)
    private Long anchorId;

    /**
     * 直播状态:0正常,1取消直播
     */
    @Column(value = "live_status", commit = "直播状态:0正常,1取消直播", length = 4, defaultVal = "0")
    private Integer liveStatus;

    /**
     * 0可用,1删除,
     */
    @Column(value = "del_status", length = 4, defaultVal = "0")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;

}
