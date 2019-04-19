package com.e_commerce.miscroservice.order.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;


import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/17 11:03
 */
@Data
public class StoreOrder {
    /**
     * 订单编号，前台展示
     */
    private Long orderNo;

    /**
     * 订单随机号,用于微信支付，退款时,生成预生成订单,保证订单能够支付
     */
    private String ordernoAttachmentStr;

    /**
     * 用户id
     */
    private Long storeId;


    /**
     * 订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭
     */
    private Integer orderStatus;

    /**
     * 订单金额原价总价，不包含邮费（包含平台优惠和商家店铺优惠）
     */
    private BigDecimal totalMoney;

    /**
     * 订单金额折后总价,不包含邮费,（不包含平台优惠和店铺优惠）,如果有改价,那么该订单金额为改价后金额
     */
    private BigDecimal totalPay;

    /**
     * 平台优惠
     */
    private BigDecimal platformTotalPreferential;

    /**
     * 商家店铺实际优惠,因为改价差额可以为负，所以商家店铺实际优惠可以为负（包含商家优惠和商家改价差额）
     */
    private BigDecimal supplierTotalPreferential;

    /**
     * 商家优惠（不包含改价部分）
     */
    private BigDecimal supplierPreferential;

    /**
     * 商家改价差额,original_price - TotalPay,该改价差额可以为负
     */
    private BigDecimal supplierChangePrice;

    /**
     * 订单金额优惠后原始待付款价格，不包含邮费
     */
    private BigDecimal originalPrice;

    /**
     * 邮费总价
     */
    private BigDecimal totalExpressMoney;

    /**
     * 邮寄信息
     */
    private String expressInfo;

    /**
     * 使用的玖币
     */
    private Integer coinUsed;

    /**
     * 用户订单备注
     */
    private String remark;

    /**
     * 生成订单平台
     */
    private String platform;

    /**
     * 生成订单平台版本号
     */
    private String platformVersion;

    /**
     * 客户端ip
     */
    private String ip;

    /**
     * 第三方的支付订单号
     */
    private String paymentNo;

    /**
     * 使用的第三方支付方式，参考常量PaymentType
     */
    private Integer paymentType;

    /**
     * 母订单id 0:其他  -1:已拆分订单, OrderNo:没有子订单
     */
    private Long parentId;

    /**
     * 0:其他, 需合并订单：自身改为-1并把相应的被合并的订单此字段设为合好的新订单OrderNo, 不需要合并订单：值与自身OrderNo相等
     */
    private Long mergedId;

    /**
     * 仓库id
     */
    private Long lOWarehouseId;

    /**
     * 状态:-1删除，0正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 市场价
     */
    private BigDecimal totalMarketPrice;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 推送时间
     */
    private Long pushTime;

    /**
     * 订单过期时间
     */
    private Long expiredTime;

    /**
     * 付款时间
     */
    private Long payTime;

    /**
     * 发货时间
     */
    private Long sendTime;

    /**
     * 收益
     */
    private BigDecimal commission;

    /**
     * 可提现金额
     */
    private BigDecimal availableCommission;

    /**
     * 提成比例
     */
    private BigDecimal commissionPercent;

    /**
     * 品牌批发订单编号
     */
    private Long brandOrder;

    /**
     * 总购买件数
     */
    private Integer totalBuyCount;

    /**
     * 新功能涉及到旧的订单，所以出现，是否已经提现，0：旧订单，1：未提现，2已提现
     */
    private Integer hasWithdrawed;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 所有上级ids,例如(,1,3,5,6,)
     */
    private String superIds;

    /**
     * 确认收获日期，格式例：20170909
     */
    private Integer confirmSignedDate;

    /**
     * 确认收获时间，时间戳
     */
    private Long confirmSignedTime;

    /**
     * 订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105、平台客服关闭订单
     */
    private Integer orderCloseType;

    /**
     * 总的退款金额
     */
    private BigDecimal totalRefundCost;

    /**
     * 订单金额折后有效总价，去除退款金额，不包含邮费
     */
    private BigDecimal totalValidPay;

    /**
     * 是否是售后进行中：0(否)、1(是)
     */
    private Integer refundUnderway;

    /**
     * 售后开始时间时间戳,使用方法,当该订单发起第一笔售后或者平台介入就记录时间戳,当该订单售后完全结束且平台介入结束时，该字段变为0
     */
    private Long refundStartTime;

    /**
     * 收件人姓名
     */
    private String expressName;

    /**
     * 收件人号码
     */
    private String expressPhone;

    /**
     * 收件人地址
     */
    private String expressAddress;

    /**
     * 买家自动确认收货总暂停时长(毫秒)
     */
    private Long autoTakeDeliveryPauseTimeLength;

    /**
     * 订单供应商备注
     */
    private String orderSupplierRemark;

    /**
     * 订单关闭时间
     */
    private Long orderCloseTime;

    /**
     * 是否平台挂起 0：否  1：是
     */
    private Integer hangUp;

    /**
     * 是否锁定在支付订单：0:否，不锁定支付订单 1：是，锁定支付订单 2：3.5版本以下的APP版本不支持改价，不包含3.5
     */
    private Integer lockingOrder;

    /**
     * 限购活动商品id
     */
    private Long restrictionActivityProductId;

    /**
     * 支付成功后是否发了优惠券:0 否 1 是
     */
    private Integer sendCoupon;

    /**
     * 0 普通, 1,进货金专属(平台代发货)
     */
    private Integer type;

    /**
     * 平台代发货关联的小程序订单
     */
    private Long shopMemberOrderId;
}
