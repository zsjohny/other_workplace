package com.jiuy.rb.model.base;

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 门店文章表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author charlie
 * @version V1.0  
 * @date 2018年08月08日 下午 06:50:06
 */
@Data
@ModelName(name = "门店文章表", tableName = "store_article")
public class StoreArticleRb extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 商家id
	 */  
	@FieldName(name = "商家id")  
	private Long storeId;  
 
	/**
	 * 文章标题
	 */  
	@FieldName(name = "文章标题")  
	private String articleTitle;  
 
	/**
	 * 文章头图
	 */  
	@FieldName(name = "文章头图")  
	private String headImage;  
 
	/**
	 * 文章内容
	 */  
	@FieldName(name = "文章内容")  
	private String articleContext;  
 
	/**
	 * 状态 0：删除，1：正常
	 */  
	@FieldName(name = "状态")
	private Integer status;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Long updateTime;  
 
	/**
	 * 公开状态 1公开,2:私密
	 */  
	@FieldName(name = "公开状态")
	private Integer publicState;  
 
	/**
	 * 置顶 0:不置顶,1:置顶
	 */  
	@FieldName(name = "置顶")
	private Integer top;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }