package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 异常表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月19日 上午 09:31:40
 */
@Data
@ModelName(name = "异常表", tableName = "yjj_sales_volume_plain_exception")
public class SalesVolumePlainExceptionRb extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 策略主键
	 */  
	@FieldName(name = "策略主键")  
	private Long planId;  
 
	/**
	 * 刷量明细表id
	 */  
	@FieldName(name = "刷量明细表id")  
	private Long detailId;  
 
	/**
	 * 预计要刷的数量
	 */  
	@FieldName(name = "预计要刷的数量")  
	private Long exceptCount;  
 
	/**
	 * 数据状态:1 执行成功,0 失败
	 */  
	@FieldName(name = "数据状态")  
	private Integer dataStatus;  
 
	/**
	 * 执行回执
	 */  
	@FieldName(name = "执行回执")  
	private String executeResult;  
 
	/**
	 * 默认执行几次
	 */  
	@FieldName(name = "默认执行几次")  
	private Integer executeCount;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Date updateTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }