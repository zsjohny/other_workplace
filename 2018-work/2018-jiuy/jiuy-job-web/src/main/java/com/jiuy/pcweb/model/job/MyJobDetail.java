package com.jiuy.pcweb.model.job; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月01日 上午 11:53:16
 */
@Data
@ModelName(name = "", tableName = "t_job_detail")
public class MyJobDetail extends Model {  
 
	/**
	 * 主键:
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 任务id:
	 */  
	@FieldName(name = "任务id")  
	private Long jobId;  
 
	/**
	 * 评论
	 */  
	@FieldName(name = "评论")  
	private String comment;  
 
	/**
	 * 图片地址:
	 */  
	@FieldName(name = "图片地址")  
	private String url;  
 
	/**
	 * 操作用户id:
	 */  
	@FieldName(name = "操作用户id")  
	private Long operatorUserId;  
 
	/**
	 * 创建时间:
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }