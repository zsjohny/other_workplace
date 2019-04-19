package com.jiuy.rb.model.common; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 数据字典
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月15日 上午 11:50:34
 */
@Data
@ModelName(name = "数据字典", tableName = "yjj_data_dictionary")
public class DataDictionaryRb extends Model {  
 
	/**
	 * 主键:
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 编码唯一
	 */  
	@FieldName(name = "编码唯一")  
	private String code;  
 
	/**
	 * 分组编码
	 */  
	@FieldName(name = "分组编码")  
	private String groupCode;  
 
	/**
	 * 值
	 */  
	@FieldName(name = "值")  
	private String val;  
 
	/**
	 * 中文名称
	 */  
	@FieldName(name = "中文名称")  
	private String name;  
 
	/**
	 * 描述
	 */  
	@FieldName(name = "描述")  
	private String comment;  
 
	/**
	 * 启用状态:0 禁用 1启用
	 */  
	@FieldName(name = "启用状态")  
	private Integer status;  
 
	/**
	 * 创建人:
	 */  
	@FieldName(name = "创建人")  
	private String createUserId;  
 
	/**
	 * 创建人姓名:
	 */  
	@FieldName(name = "创建人姓名")  
	private String createUserName;  
 
	/**
	 * 创建时间:
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 最后修改人id:
	 */  
	@FieldName(name = "最后修改人id")  
	private String lastUserId;  
 
	/**
	 * 最后修改人名:
	 */  
	@FieldName(name = "最后修改人名")  
	private String lastUserName;  
 
	/**
	 * 最后修改时间:
	 */  
	@FieldName(name = "最后修改时间")  
	private Date lastUpdateTime;  
 
	private Long parentId;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }