package com.jiuy.timer.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 定时任务表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月15日 下午 02:36:41
 */
@Data
@ModelName(name = "定时任务表", tableName = "qrtz_jobs_accept")
public class QrtzJobsAccept extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 任务名称
	 */  
	@FieldName(name = "任务名称")  
	private String jobName;  
 
	/**
	 * 任务分组
	 */  
	@FieldName(name = "任务分组")  
	private String jobGroup;  
 
	/**
	 * 任务说明
	 */  
	@FieldName(name = "任务说明")  
	private String jobComment;  
 
	/**
	 * job类
	 */  
	@FieldName(name = "job类")  
	private String jobClassName;  
 
	/**
	 * 回调地址
	 */  
	@FieldName(name = "回调地址")  
	private String feedbackUrl;  
 
	/**
	 * 回调参数
	 */  
	@FieldName(name = "回调参数")  
	private String feedbackData;  
 
	/**
	 * 时间
	 */  
	@FieldName(name = "时间")  
	private String date;  
 
	/**
	 * 时间规则
	 */  
	@FieldName(name = "时间规则")  
	private String cronExpression;  
 
	/**
	 * 删除状态:0未删除 1删除
	 */  
	@FieldName(name = "删除状态")  
	private Integer delState;  
 
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
 
	/**
	 * 开始时间
	 */  
	@FieldName(name = "开始时间")  
	private String beginDate;  
 
	/**
	 * 结束时间
	 */  
	@FieldName(name = "结束时间")  
	private String endDate;  
 
	/**
	 * 执行间隔:单位分钟
	 */  
	@FieldName(name = "执行间隔")  
	private Integer eachTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }