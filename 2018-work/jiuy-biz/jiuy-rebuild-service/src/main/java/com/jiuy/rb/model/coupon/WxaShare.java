package com.jiuy.rb.model.coupon; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 分享关系表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 05:13:44
 */
@Data
@ModelName(name = "分享关系表", tableName = "yjj_wxa_share")
public class WxaShare extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 分享者
	 */  
	@FieldName(name = "分享者")  
	private Long sourceUser;  
 
	/**
	 * 被邀请者
	 */  
	@FieldName(name = "被邀请者")  
	private Long targetUser;  
 
	/**
	 * 微信id
	 */  
	@FieldName(name = "微信id")  
	private String wxId;  
 
	/**
	 * 微信名称
	 */  
	@FieldName(name = "微信名称")  
	private String wxNickname;  
 
	/**
	 * 微信头像
	 */  
	@FieldName(name = "微信头像")  
	private String wxHeaderPortrait;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 邀请类型:1 商品分享,2活动分享,3优惠券分享
	 */  
	@FieldName(name = "邀请类型")  
	private Integer shareType;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }