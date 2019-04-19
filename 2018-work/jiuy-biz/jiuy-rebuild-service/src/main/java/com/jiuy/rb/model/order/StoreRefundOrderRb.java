package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 售后订单表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月28日 下午 04:31:55
 */
@Data
@ModelName(name = "售后订单表", tableName = "store_refund_order")
public class StoreRefundOrderRb extends Model {  
 
	/**
	 * id
	 */  
	@FieldName(name = "id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 售后订单编号
	 */  
	@FieldName(name = "售后订单编号")  
	private Long refundOrderNo;  
 
	/**
	 * 商家号ID
	 */  
	@FieldName(name = "商家号ID")  
	private Long storeId;  
 
	/**
	 * 商家手机号
	 */  
	@FieldName(name = "商家手机号")  
	private String storePhone;  
 
	/**
	 * 商家名称
	 */  
	@FieldName(name = "商家名称")  
	private String storeName;  
 
	/**
	 * 供应商ID
	 */  
	@FieldName(name = "供应商ID")  
	private Long supplierId;  
 
	/**
	 * 订单ID
	 */  
	@FieldName(name = "订单ID")  
	private Long orderNo;  
 
	/**
	 * 品牌ID
	 */  
	@FieldName(name = "品牌ID")  
	private Long brandId;  
 
	/**
	 * 品牌名称
	 */  
	@FieldName(name = "品牌名称")  
	private String brandName;  
 
	/**
	 * 退款类型：1.仅退款  2.退货退款
	 */  
	@FieldName(name = "退款类型")  
	private Integer refundType;  
 
	/**
	 * 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
	 */  
	@FieldName(name = "售后订单状态")  
	private Integer refundStatus;  
 
	/**
	 * 退款申请金额
	 */  
	@FieldName(name = "退款申请金额")  
	private BigDecimal refundCost;  
 
	/**
	 * 退货数量
	 */  
	@FieldName(name = "退货数量")  
	private Integer returnCount;  
 
	/**
	 * 退款原因ID
	 */  
	@FieldName(name = "退款原因ID")  
	private Long refundReasonId;  
 
	/**
	 * 退款原因
	 */  
	@FieldName(name = "退款原因")  
	private String refundReason;  
 
	/**
	 * 退款说明
	 */  
	@FieldName(name = "退款说明")  
	private String refundRemark;  
 
	/**
	 * 退款证明图片，英文逗号分隔的字符串
	 */  
	@FieldName(name = "退款证明图片，英文逗号分隔的字符串")  
	private String refundProofImages;  
 
	/**
	 * 退款通道：0(无)、1(支付宝)、2(微信)
	 */  
	@FieldName(name = "退款通道")  
	private Integer refundWay;  
 
	/**
	 * 退款时间
	 */  
	@FieldName(name = "退款时间")  
	private Long refundTime;  
 
	/**
	 * 申请售后时间
	 */  
	@FieldName(name = "申请售后时间")  
	private Long applyTime;  
 
	/**
	 * 买家发货时间
	 */  
	@FieldName(name = "买家发货时间")  
	private Long customerReturnTime;  
 
	/**
	 * 卖家确认收货时间
	 */  
	@FieldName(name = "卖家确认收货时间")  
	private Long confirmTime;  
 
	/**
	 * 平台介入时间
	 */  
	@FieldName(name = "平台介入时间")  
	private Long platformInterveneTime;  
 
	/**
	 * 平台客服关闭时间
	 */  
	@FieldName(name = "平台客服关闭时间")  
	private Long platformInterveneCloseTime;  
 
	/**
	 * 买家发货快递单号
	 */  
	@FieldName(name = "买家发货快递单号")  
	private String customerExpressNo;  
 
	/**
	 * 买家发货快递公司
	 */  
	@FieldName(name = "买家发货快递公司")  
	private String customerExpressCompany;  
 
	/**
	 * 卖家同意时间
	 */  
	@FieldName(name = "卖家同意时间")  
	private Long storeAllowRefundTime;  
 
	/**
	 * 卖家拒绝时间
	 */  
	@FieldName(name = "卖家拒绝时间")  
	private Long storeRefuseRefundTime;  
 
	/**
	 * 卖家拒绝退款原因
	 */  
	@FieldName(name = "卖家拒绝退款原因")  
	private String storeRefuseReason;  
 
	/**
	 * 平台介入处理意见
	 */  
	@FieldName(name = "平台介入处理意见")  
	private String handlingSuggestion;  
 
	/**
	 * 卖家同意退款备注
	 */  
	@FieldName(name = "卖家同意退款备注")  
	private String storeAgreeRemark;  
 
	/**
	 * 平台关闭理由
	 */  
	@FieldName(name = "平台关闭理由")  
	private String platformCloseReason;  
 
	/**
	 * 平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
	 */  
	@FieldName(name = "平台介入状态")  
	private Integer platformInterveneState;  
 
	/**
	 * 供应商收货人姓名
	 */  
	@FieldName(name = "供应商收货人姓名")  
	private String receiver;  
 
	/**
	 * 供应商收货地址
	 */  
	@FieldName(name = "供应商收货地址")  
	private String supplierReceiveAddress;  
 
	/**
	 * 收货人电话
	 */  
	@FieldName(name = "收货人电话")  
	private String receiverPhone;  
 
	/**
	 * 卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
	 */  
	@FieldName(name = "卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停")  
	private Long supplierAutoTakeDeliveryPauseTime;  
 
	/**
	 * 卖家自动确认收货总暂停时长（毫秒）
	 */  
	@FieldName(name = "卖家自动确认收货总暂停时长（毫秒）")  
	private Long supplierAutoTakeDeliveryPauseTimeLength;  
 
	/**
	 * 买家撤销售后订单时间
	 */  
	@FieldName(name = "买家撤销售后订单时间")  
	private Long customerCancelTime;  
 
	/**
	 * 买家超时未发货时间
	 */  
	@FieldName(name = "买家超时未发货时间")  
	private Long customerOvertimeTimeNoDelivery;  
 
	/**
	 * 物流信息内容
	 */  
	@FieldName(name = "物流信息内容")  
	private String expressInfo;  
 
	/**
	 * 买家发货快递公司名称
	 */  
	@FieldName(name = "买家发货快递公司名称")  
	private String customerExpressCompanyName;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }