package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 用户订单细目表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月12日 下午 06:21:21
 */
@Data
@ModelName(name = "用户订单细目表", tableName = "store_orderitem")
public class StoreOrderItemRb extends Model {  
 
	/**
	 * id
	 */  
	@FieldName(name = "id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 新订单表OrderNo
	 */  
	@FieldName(name = "新订单表OrderNo")  
	private Long orderNo;  
 
	/**
	 * 用户id
	 */  
	@FieldName(name = "用户id")  
	private Long storeId;  
 
	/**
	 * 对应Product表的id
	 */  
	@FieldName(name = "对应Product表的id")  
	private Long productId;  
 
	/**
	 * 对应ProductSKU的id
	 */  
	@FieldName(name = "对应ProductSKU的id")  
	private Long skuId;  
 
	/**
	 * 订单金额总价，不包含邮费
	 */  
	@FieldName(name = "订单金额总价，不包含邮费")  
	private BigDecimal totalMoney;  
 
	/**
	 * 邮费总价
	 */  
	@FieldName(name = "邮费总价")  
	private BigDecimal totalExpressMoney;  
 
	/**
	 * 订单细目单价，不包含邮费
	 */  
	@FieldName(name = "订单细目单价，不包含邮费")  
	private BigDecimal money;  
 
	/**
	 * 邮费单价
	 */  
	@FieldName(name = "邮费单价")  
	private BigDecimal expressMoney;  
 
	/**
	 * 总玖币
	 */  
	@FieldName(name = "总玖币")  
	private Integer totalUnavalCoinUsed;  
 
	/**
	 * 玖币
	 */  
	@FieldName(name = "玖币")  
	private Integer unavalCoinUsed;  
 
	/**
	 * 订购数量
	 */  
	@FieldName(name = "订购数量")  
	private Integer buyCount;  
 
	/**
	 * sku快照，json
	 */  
	@FieldName(name = "sku快照，json")  
	private String skuSnapshot;  
 
	/**
	 * 状态:-1删除，0正常
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updateTime;  
 
	/**
	 * 关联的品牌id
	 */  
	@FieldName(name = "关联的品牌id")  
	private Long brandId;  
 
	/**
	 * 仓库id
	 */  
	@FieldName(name = "仓库id")  
	private Long lOWarehouseId;  
 
	/**
	 * 实付价
	 */  
	@FieldName(name = "实付价")  
	private BigDecimal totalPay;  
 
	/**
	 * 市场价
	 */  
	@FieldName(name = "市场价")  
	private BigDecimal totalMarketPrice;  
 
	private BigDecimal marketPrice;  
 
	/**
	 * 总提现金额
	 */  
	@FieldName(name = "总提现金额")  
	private BigDecimal totalAvailableCommission;  
 
	/**
	 * sku 货架位置
	 */  
	@FieldName(name = "sku 货架位置")  
	private String position;  
 
	/**
	 * 供应商ID
	 */  
	@FieldName(name = "供应商ID")  
	private Long supplierId;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }