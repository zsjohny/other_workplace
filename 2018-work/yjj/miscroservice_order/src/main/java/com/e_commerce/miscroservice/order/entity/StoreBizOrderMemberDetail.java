package com.e_commerce.miscroservice.order.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 10:29
 * @Copyright 玖远网络
 */
@Data
@Table("store_biz_order_member_detail")
public class StoreBizOrderMemberDetail{

    @Id
    private Long id;

    @Column(value = "order_no", commit = "订单编号", isNUll = false, length = 50)
    private String orderNo;

    @Column(value = "order_id", commit = "订单id", isNUll = false, length = 20)
    private Long orderId;

    /**
     * 会员商品名称
     */
    @Column(value = "product_name", commit = "商品名", length = 200, isNUll = false)
    private String productName;

    /**
     * 会员类型
     */
    @Column(value = "member_type", commit = "会员类型", length = 4, isNUll = false)
    private Integer memberType;

    /**
     * 会员过期--时间单位
     */
//    @Column(value = "time_unit", commit = "会员过期,时间单位", isNUll = false, length = 4)
    private Integer timeUnit;

    /**
     * 会员过期--时间长度
     */
//    @Column(value = "time_value", commit = "会员过期,时间长度", isNUll = false, length = 4)
    private Integer timeValue;

    /**
     * 购买渠道1app购买,2线下,3公众号购买,4店中店购买
     */
    @Column(value = "canal", commit = "购买渠道", isNUll = false, length = 4)
    private Integer canal;

    /**
     * 订单成功后执行的业务 0:无,1:立即开通店中店
     */
    @Column(value = "task_after_success", commit = "订单成功后执行的业务 0:无,1:立即开通店中店", length = 4, defaultVal = "0")
    private Integer taskAfterSuccess;

    /**
     * 0可用,1删除,2失效
     */
    @Column(value = "del_status", length = 4, defaultVal = "0")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;
}
