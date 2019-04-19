package com.jiuy.rb.model.common; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 日志表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月13日 下午 07:18:36
 */
@Data
@ModelName(name = "日志表", tableName = "yjj_operator_logs")
public class OperatorLogsRb extends Model {  
 
	/**
	 * 主键:
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 模块编码:
	 */  
	@FieldName(name = "模块编码")  
	private String moduelCode;  
 
	/**
	 * 模块名称:
	 */  
	@FieldName(name = "模块名称")  
	private String moduleName;  
 
	/**
	 * 操作人id
	 */  
	@FieldName(name = "操作人id")  
	private String opertionUserId;  
 
	/**
	 * 操作人名称:
	 */  
	@FieldName(name = "操作人名称")  
	private String opertionUserName;  
 
	/**
	 * 操作详情:
	 */  
	@FieldName(name = "操作详情")  
	private String opertionDetail;  
 
	/**
	 * 创建时间:
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 操作的方法:
	 */  
	@FieldName(name = "操作的方法")  
	private String createMethod;  
 
	/**
	 * 数据id
	 */  
	@FieldName(name = "数据id")  
	private String dataId;  
 
	/**
	 * 表名
	 */  
	@FieldName(name = "表名")  
	private String tableName;  
 
	/**
	 * 表的描述
	 */  
	@FieldName(name = "表的描述")  
	private String tableComment;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }