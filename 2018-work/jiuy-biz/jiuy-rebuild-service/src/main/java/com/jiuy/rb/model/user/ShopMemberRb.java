package com.jiuy.rb.model.user; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 小程序会员表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月23日 上午 10:39:37
 */
@Data
@ModelName(name = "小程序会员表", tableName = "shop_member")
public class ShopMemberRb extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 商家ID
	 */  
	@FieldName(name = "商家ID")  
	private Long storeId;  
 
	/**
	 * 手机号码
	 */  
	@FieldName(name = "手机号码")  
	private String bindPhone;  
 
	/**
	 * 微信UId
	 */  
	@FieldName(name = "微信UId")  
	private String bindWeixin;  
 
	/**
	 * 用户昵称
	 */  
	@FieldName(name = "用户昵称")  
	private String userNickname;  
 
	/**
	 * 用户头像
	 */  
	@FieldName(name = "用户头像")  
	private String userIcon;  
 
	/**
	 * 用户二维码
	 */  
	@FieldName(name = "用户二维码")  
	private String minicodeUrl;  
 
	/**
	 * 最后会员发送消息时间
	 */  
	@FieldName(name = "最后会员发送消息时间")  
	private Long lastMessageTime;  
 
	/**
	 *  最后会员发送消息类型：消息类型：0文本text、1图片image、2链接link、3语音消息voice、4视频消息video、5小视频消息shortvideo、6地理位置消息location、9事件event
	 */  
	@FieldName(name = " 最后会员发送消息类型")  
	private Integer lastMessageType;  
 
	/**
	 *  最后会员发送消息显示内容
	 */  
	@FieldName(name = " 最后会员发送消息显示内容")  
	private String lastMessageContent;  
 
	/**
	 *  未读消息数量
	 */  
	@FieldName(name = " 未读消息数量")  
	private String notReadMessageCount;  
 
	/**
	 * 1,2,3
	 */  
	@FieldName(name = "1,2,3")  
	private String tagIds;  
 
	/**
	 * 状态 -1:删除 0:正常 1:取消关注
	 */  
	@FieldName(name = "状态 -1")  
	private Integer status;  
 
	/**
	 * 备注名称
	 */  
	@FieldName(name = "备注名称")  
	private String memoName;  
 
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
	 * 注册来源
	 */  
	@FieldName(name = "注册来源")  
	private String source;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }