package com.jiuy.rb.model.user; 

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
 * @date 2018年07月24日 上午 10:49:55
 */
@Data
@ModelName(name = "支付配置", tableName = "yjj_link_us")
public class LinkUs extends Model {  
 
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
	 * openId 在我们小程序下的id
	 */  
	@FieldName(name = "openId 在我们小程序下的id")  
	private String openId;  
 
	/**
	 * memberId
	 */  
	@FieldName(name = "memberId")  
	private Long memberId;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }