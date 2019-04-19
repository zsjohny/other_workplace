package com.e_commerce.miscroservice.store.entity.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import lombok.Data;
 @Data
 @Table("store_order")
 public class Order {
  /**
   * 订单编号，前台展示
   */
@Id
private Long orderno;
  /**
   * 订单随机号,用于微信支付，退款时,生成预生成订单,保证订单能够支付
   */
private String ordernoAttachmentStr;
  /**
   * 用户id
   */
private Long storeid;
  /**
   * 0: 普通订单 1:售后订单 2:当面付订单
   */
private Integer ordertype;
  /**
   * 订单付款状态：参照参照OrderStatus
   * 订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭、5所有（已废弃）、20待审核（已废弃）、30已审核（废弃）、40审核不通过（已废弃）、60已签收（已废弃）、80退款中（已废弃）、90退款成功(已废弃)
   */
private Integer orderstatus;
private String expressinfo;
private Integer coinused;
private String remark;
private String platform;
private String platformversion;
private String ip;
private String paymentno;
private Integer paymenttype;
private Long parentid;
private Long mergedid;
private Long lowarehouseid;
private Integer status;
private Long createtime;
private Long updatetime;
private String cancelreason;
private Long pushtime;
private Long expiredtime;
private Long paytime;
private Long sendtime;
private Long brandorder;
private Integer totalbuycount;
private Integer haswithdrawed;
private Long supplierid;
private Long grounduserid;
private String superids;
private Integer confirmsigneddate;
private Long confirmsignedtime;
  /**
   * 订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105、平台客服关闭订单
   */
private Integer orderCloseType;
private Integer refundUnderway;
private Long refundStartTime;
private String expressName;
private String expressPhone;
private String expressAddress;
private Long autoTakeDeliveryPauseTimeLength;
private String orderSupplierRemark;
private Long orderCloseTime;
private Integer hangup;
private Integer lockingOrder;
private Long restrictionActivityProductId;
private Integer classify;
private Integer sendCoupon;
}