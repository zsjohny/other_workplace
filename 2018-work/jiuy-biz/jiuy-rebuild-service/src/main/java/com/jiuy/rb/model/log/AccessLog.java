package com.jiuy.rb.model.log; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 接口访问记录
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月03日 下午 04:02:30
 */
@Data
@ModelName(name = "接口访问记录", tableName = "yjj_access_log")
public class AccessLog extends Model {  
 
	/**
	 * id
	 */  
	@FieldName(name = "id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * ip地址
	 */  
	@FieldName(name = "ip地址")  
	private String ip;  
 
	/**
	 * 访问链接地址
	 */  
	@FieldName(name = "访问链接地址")  
	private String uri;  
 
	/**
	 * 访问用户id
	 */  
	@FieldName(name = "访问用户id")  
	private Long userId;  
 
	/**
	 * 系统:1 app 2 小程序
	 */  
	@FieldName(name = "系统")  
	private Integer type;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }