package com.jiuy.product.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月10日 下午 04:45:09
 */
@Data
@ModelName(name = "", tableName = "store_coupontemplate")
public class StoreCouponTemplate extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	private String name;  
 
	/**
	 * 0 : 代金券
	 */  
	@FieldName(name = "0 ")  
	private Integer type;  
 
	/**
	 * 面值
	 */  
	@FieldName(name = "面值")  
	private BigDecimal money;  
 
	/**
	 * 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品, 4:邮费, 5:品牌
	 */  
	@FieldName(name = "范围类型 0")  
	private Integer rangeType;  
 
	/**
	 * 范围。格式：限定商品：{ "productIds":[2,3]}  分类：{ "categoryIds":[2,3]}  限额订单：{ "limitOrders":[2,3]}
	 */  
	@FieldName(name = "范围。格式：限定商品：{ productIds")
	private String rangeContent;  
 
	/**
	 * 有效开始时间
	 */  
	@FieldName(name = "有效开始时间")  
	private Long validityStartTime;  
 
	/**
	 * 有效结束时间 0:无限制
	 */  
	@FieldName(name = "有效结束时间 0")  
	private Long validityEndTime;  
 
	/**
	 * 优惠限制 0:无 1:有
	 */  
	@FieldName(name = "优惠限制 0")  
	private Integer isLimit;  
 
	/**
	 * 发行量,平台优惠券
	 */  
	@FieldName(name = "发行量,平台优惠券")  
	private Integer publishCount;  
 
	/**
	 * 发放量,平台优惠券
	 */  
	@FieldName(name = "发放量,平台优惠券")  
	private Integer grantCount;  
 
	/**
	 * 可用量,平台优惠券
	 */  
	@FieldName(name = "可用量,平台优惠券")  
	private Integer availableCount;  
 
	/**
	 * 优惠券共用性 0:不可共用 1：可共用
	 */  
	@FieldName(name = "优惠券共用性 0")  
	private Integer coexist;  
 
	/**
	 * -1:删除 0:正常
	 */  
	@FieldName(name = "-1")  
	private Integer status;  
 
	private Long createTime;  
 
	private Long updateTime;  
 
	/**
	 * 订单限额
	 */  
	@FieldName(name = "订单限额")  
	private BigDecimal limitMoney;  
 
	/**
	 * 积分兑换 ： 0：不用于积分兑换  1：用于积分兑换
	 */  
	@FieldName(name = "积分兑换 ")  
	private Integer exchangeJiuCoinSetting;  
 
	/**
	 * 兑换消耗的积分

	 */  
	@FieldName(name = "兑换消耗的积分")  
	private Integer exchangeJiuCoinCost;  
 
	/**
	 * 兑换总量限制，-1代表不限
	 */  
	@FieldName(name = "兑换总量限制，-1代表不限")  
	private Integer exchangeLimitTotalCount;  
 
	/**
	 * 单人限购代金券张数，-1代表不限
	 */  
	@FieldName(name = "单人限购代金券张数，-1代表不限")  
	private Integer exchangeLimitSingleCount;  
 
	/**
	 * 已兑换的数量
	 */  
	@FieldName(name = "已兑换的数量")  
	private Integer exchangeCount;  
 
	/**
	 * 兑换开始时间
	 */  
	@FieldName(name = "兑换开始时间")  
	private Long exchangeStartTime;  
 
	/**
	 * 兑换结束时间
	 */  
	@FieldName(name = "兑换结束时间")  
	private Long exchangeEndTime;  
 
	/**
	 * 积分促销设置 0：无，1：定义
	 */  
	@FieldName(name = "积分促销设置 0")  
	private Integer promotionJiuCoinSetting;  
 
	/**
	 * 兑换消耗的积分(促销积分)
	 */  
	@FieldName(name = "兑换消耗的积分(促销积分)")  
	private Integer promotionJiuCoin;  
 
	/**
	 * 促销开始时间
	 */  
	@FieldName(name = "促销开始时间")  
	private Long promotionStartTime;  
 
	/**
	 * 促销结束时间
	 */  
	@FieldName(name = "促销结束时间")  
	private Long promotionEndTime;  
 
	/**
	 * 范围类型ID集合.格式:,id,id,id,
	 */  
	@FieldName(name = "范围类型ID集合.格式")  
	private String rangeTypeIds;  
 
	/**
	 * 范围类型名称集合.格式:,name,name,
	 */  
	@FieldName(name = "范围类型名称集合.格式")  
	private String rangeTypeNames;  
 
	/**
	 * 发放优惠券的供应商ID
	 */  
	@FieldName(name = "发放优惠券的供应商ID")  
	private Long supplierId;  
 
	/**
	 * 每人限领优惠券个数
	 */  
	@FieldName(name = "每人限领优惠券个数")  
	private Integer limitDraw;  
 
	/**
	 * 领取开始时间
	 */  
	@FieldName(name = "领取开始时间")  
	private Long drawStartTime;  
 
	/**
	 * 领取结束时间
	 */  
	@FieldName(name = "领取结束时间")  
	private Long drawEndTime;  
 
	/**
	 * 发行方
	 */  
	@FieldName(name = "发行方")  
	private String publisher;  
 
	/**
	 * 发放状态   -1：全部 0：未发放 1：发放中  2：已停止  3：已作废 
	 */  
	@FieldName(name = "发放状态   -1")  
	private Integer publishStatus;  
 
	/**
	 * 未使用优惠券总数,未过期前为可用优惠券总数,过期后为过期优惠券总数,不包含作废优惠券和已使用优惠券
	 */  
	@FieldName(name = "未使用优惠券总数,未过期前为可用优惠券总数,过期后为过期优惠券总数,不包含作废优惠券和已使用优惠券")  
	private Integer validTotalCount;  
 
	/**
	 * 未使用优惠券总额,未过期前为可用优惠券总额,过期后为过期优惠券总额,不包含作废优惠券和已使用优惠券
	 */  
	@FieldName(name = "未使用优惠券总额,未过期前为可用优惠券总额,过期后为过期优惠券总额,不包含作废优惠券和已使用优惠券")  
	private BigDecimal validTotalAmount;  
 
	/**
	 * 已使用数量,供应商优惠券
	 */  
	@FieldName(name = "已使用数量,供应商优惠券")  
	private Integer usedCount;  
 
	/**
	 * 已作废数量,供应商优惠券
	 */  
	@FieldName(name = "已作废数量,供应商优惠券")  
	private Integer cancelCount;  
 
	/**
	 * 领取状态 0:可以领取 1:不可领取
	 */  
	@FieldName(name = "领取状态 0")  
	private Integer drawStatus;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }