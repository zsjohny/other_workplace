package com.jiuy.rb.model.account; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 支付配置
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月23日 下午 06:10:03
 */
@Data
@ModelName(name = "支付配置", tableName = "yjj_wxa_pay_config")
public class WxaPayConfig extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 小程序id
	 */  
	@FieldName(name = "小程序id")  
	private String appId;  
 
	/**
	 * 微信支付商户id
	 */  
	@FieldName(name = "微信支付商户id")  
	private String mchId;  
 
	/**
	 * 密文
	 */  
	@FieldName(name = "密文")  
	private String paternerKey;  
 
	/**
	 * 证书地址
	 */  
	@FieldName(name = "证书地址")  
	private String certPath;  
 
	/**
	 * 订单前缀
	 */  
	@FieldName(name = "订单前缀")  
	private String orderPrefix;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }