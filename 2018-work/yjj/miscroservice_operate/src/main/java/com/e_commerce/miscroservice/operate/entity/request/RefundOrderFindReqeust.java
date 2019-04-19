package com.e_commerce.miscroservice.operate.entity.request;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/30 16:50
 * @Copyright 玖远网络
 */
@Data
public class RefundOrderFindReqeust extends BaseEntity {

    /**
     * 售后订单号
     */
    private String refundOrderNo;
    /**
     *  数量 起
     */
    private Integer countStr;
    /**
     *  数量 止
     */
    private Integer countEnd;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     *  姓名
     */
    private String name;
    /**
     *  申请时间 起
     */
    private String applyTimeStr;
    /**
     *  申请时间 止
     */
    private String applyTimeEnd;
    /**
     *  商品名称
     */
    private String shopName;
    /**
     *  订单号
     */
    private String orderNo;
    /**
     *  退款金额 起
     */
    private Double refundMoneyStr;
    /**
     *  退款金额止
     */
    private Double refundMoneyEnd;

    
}
