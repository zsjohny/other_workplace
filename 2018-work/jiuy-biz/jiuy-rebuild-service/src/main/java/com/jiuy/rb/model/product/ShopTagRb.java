package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 门店标签表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author charlie
 * @version V1.0  
 * @date 2018年09月03日 下午 07:42:01
 */
@Data
@ModelName(name = "门店标签表", tableName = "shop_tag")
public class ShopTagRb extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 门店id
	 */  
	@FieldName(name = "门店id")  
	private Long storeId;  
 
	/**
	 * 标签名称
	 */  
	@FieldName(name = "标签名称")  
	private String tagName;  
 
	/**
	 * 权重
	 */  
	@FieldName(name = "权重")  
	private Integer weight;  
 
	/**
	 *  状态  -1：删除   0：正常
	 */  
	@FieldName(name = " 状态  -1")  
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
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }