package com.e_commerce.miscroservice.activity.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/27 9:53
 * @Copyright 玖远网络
 */
@Data
//@Table
public class SecondBuyActivity{
    /**
     * id
     */
    @Id
    private Long id;
    /**
     * 商家号ID
     */
    @Column("store_id")
    private Long storeId;
    /**
     * 活动标题
     */
    @Column("activity_title")
    private String activityTitle;
    /**
     * 活动商品ID
     */
    @Column("shop_product_id")
    private Long shopProductId;
    /**
     * 活动商品款号
     */
    @Column("clothes_number")
    private String clothesNumber;
    /**
     * 活动商品名称
     */
    @Column("shop_product_name")
    private String shopProductName;
    /**
     * 活动商品主图
     */
    @Column("shop_product_mainimg")
    private String shopProductMainimg;
    /**
     * 活动商品橱窗图
     */
    @Column("shop_product_showcase_imgs")
    private String shopProductShowcaseImgs;
    /**
     * 活动商品原价格
     */
    @Column("activity_product_price")
    private Double activityProductPrice;
    /**
     * 活动价格
     */
    @Column("activity_price")
    private Double activityPrice;
    /**
     * 活动商品数量
     */
    @Column("activity_product_count")
    private Integer activityProductCount;

    /**
     * 活动有效开始时间
     */
    @Column("activity_start_time")
    private Long activityStartTime;
    /**
     * 活动有效截止时间
     */
    @Column("activity_end_time")
    private Long activityEndTime;
    /**
     * 活动手工结束时间：0表示未手工结束
     */
    @Column("activity_hand_end_time")
    private Long activityHandEndTime;
    /**
     * 参与人数
     */
    @Column("activity_member_count")
    private Integer activityMemberCount;
    /**
     * 删除状态：-1删除、0正常
     */
    @Column("del_state")
    private Integer delState;
    /**
     * 创建时间
     */
    @Column("create_time")
    private Long createTime;
    /**
     * 修改时间
     */
    @Column("update_time")
    private Long updateTime;
    /**
     * 已下单件数
     */
    @Column("ordered_product_count")
    private Integer orderedProductCount;
}
