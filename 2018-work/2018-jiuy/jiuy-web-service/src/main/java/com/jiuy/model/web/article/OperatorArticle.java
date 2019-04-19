package com.jiuy.model.web.article; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 官网文章
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年05月24日 下午 03:56:35
 */
@Data
@ModelName(name = "官网文章")
public class OperatorArticle extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 标题
	 */  
	@FieldName(name = "标题")  
	private String title;  
 
	/**
	 * 预览图
	 */  
	@FieldName(name = "预览图")  
	private String previewImageUrl;  
 
	/**
	 * 摘要
	 */  
	@FieldName(name = "摘要")  
	private String abstracts;  
 
	/**
	 * 文章
	 */  
	@FieldName(name = "文章")  
	private String content;  
 
	/**
	 * 状态：0:正常 -1:删除
	 */  
	@FieldName(name = "状态：0")  
	private Byte status;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updateTime;  
 
	/**
	 * 删除时间
	 */  
	@FieldName(name = "删除时间")  
	private Long deleteTime;  
 
	/**
	 * 创建管理员ID
	 */  
	@FieldName(name = "创建管理员ID")  
	private Long createOperatorId;  
 
	/**
	 * 删除管理员ID
	 */  
	@FieldName(name = "删除管理员ID")  
	private Long deleteOperatorId;  
 
	/**
	 * seo标题
	 */  
	@FieldName(name = "seo标题")  
	private String seoTitle;  
 
	/**
	 * seo描述
	 */  
	@FieldName(name = "seo描述")  
	private String seoDescriptor;  
 
	/**
	 * seo关键词
	 */  
	@FieldName(name = "seo关键词")  
	private String seoKeywords;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }