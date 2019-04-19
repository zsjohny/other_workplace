package com.jiuy.timer.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 定时任务操作日志表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月01日 上午 10:39:48
 */
@Data
@ModelName(name = "定时任务操作日志表")
public class QrtzOptLog extends Model {  
 
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
	 * 任务描述
	 */  
	@FieldName(name = "任务描述")  
	private String jobComment;  
 
	/**
	 * 操作类型: 1 新增任务,2 修改任务,3 暂停任务,4 恢复任务, 5删除任务,6执行任务
	 */  
	@FieldName(name = "操作类型")  
	private Integer optType;  
 
	/**
	 * 开始操作时间
	 */  
	@FieldName(name = "开始操作时间")  
	private Date optTimeStart;  
 
	/**
	 * 操作结束时间
	 */  
	@FieldName(name = "操作结束时间")  
	private Date optTimeEnd;  
 
	/**
	 * 操作回执
	 */  
	@FieldName(name = "操作回执")  
	private String result;  
 
	/**
	 * 执行结果:1成功,2失败
	 */  
	@FieldName(name = "执行结果")  
	private Integer status;  
 
	/**
	 * 回调地址
	 */  
	@FieldName(name = "回调地址")  
	private String feedbackUrl;  
 
	/**
	 * 回掉参数
	 */  
	@FieldName(name = "回掉参数")  
	private String feedbackParam;  
 
	/**
	 * job json快照
	 */  
	@FieldName(name = "job json快照")  
	private String jobSnapshot;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }