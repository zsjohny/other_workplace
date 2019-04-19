package com.jiuy.rb.model.coupon; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分享玖币计算规则
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 06:32:19
 */
@Data
@ModelName(name = "分享玖币计算规则", tableName = "yjj_share_coins_rule")
public class ShareCoinsRule extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 分享类型: 1:活动分享,2:商品分享,3:分享优惠劵
	 */  
	@FieldName(name = "分享类型")  
	private Integer type;  
 
	/**
	 * 0:无效,1:有效
	 */  
	@FieldName(name = "0")  
	private Integer status;  
 
	/**
	 * 固定发放的数量
	 */  
	@FieldName(name = "固定发放的数量")  
	private Long count;  
 
	/**
	 * 计算方式:0 固定数量,1按照分享物品的价值比例
	 */  
	@FieldName(name = "计算方式")  
	private Integer countType;  
 
	/**
	 * 兑换比例:如某个商品的价格比例
	 */  
	@FieldName(name = "兑换比例")  
	private BigDecimal proportion;  
 
	/**
	 * 什么时候过期
	 */  
	@FieldName(name = "什么时候过期")  
	private Date deadline;  
 
	/**
	 * 规则说明(html)
	 */  
	@FieldName(name = "规则说明(html)")  
	private byte[] description;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }