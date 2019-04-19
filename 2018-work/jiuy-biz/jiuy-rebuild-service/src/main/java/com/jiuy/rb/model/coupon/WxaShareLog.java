package com.jiuy.rb.model.coupon; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 分享记录表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 05:37:31
 */
@Data
@ModelName(name = "分享记录表", tableName = "yjj_wxa_share_log")
public class WxaShareLog extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 分享者id
	 */  
	@FieldName(name = "分享者id")  
	private Long memberId;  
 
	/**
	 * 商品/活动id
	 */  
	@FieldName(name = "商品/活动id")  
	private Long targetId;  
 
	/**
	 * 邀请类型:1 商品分享,2活动分享,3优惠券分享
	 */  
	@FieldName(name = "邀请类型")  
	private Integer shareType;  
 
	/**
	 * 描述
	 */  
	@FieldName(name = "描述")  
	private String description;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }