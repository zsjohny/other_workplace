package com.jiuy.rb.model.account; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 玖币操作明细
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月12日 上午 10:21:57
 */
@Data
@ModelName(name = "玖币操作明细", tableName = "yjj_coins_log")
public class CoinsLog extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * coins表主键
	 */  
	@FieldName(name = "coins表主键")  
	private Long coinsId;  
 
	/**
	 * 详情表id
	 */  
	@FieldName(name = "详情表id")  
	private Long coinsDetailId;  
 
	/**
	 * 操作前总金额
	 */  
	@FieldName(name = "操作前总金额")  
	private Long beforeTotalCoins;  
 
	/**
	 * 操作前可用金额
	 */  
	@FieldName(name = "操作前可用金额")  
	private Long beforeAvliedCoins;  
 
	/**
	 * 操作后不可用
	 */  
	@FieldName(name = "操作前不可用")
	private Long beforeUnavliedCoins;  
 
	/**
	 * 操作后总金额
	 */  
	@FieldName(name = "操作后总金额")  
	private Long afterTotalCoins;  
 
	/**
	 * 操作后可用金额
	 */  
	@FieldName(name = "操作后可用金额")  
	private Long afterAvliedCoins;  
 
	/**
	 * 操作不可用总金额
	 */  
	@FieldName(name = "操作不可用总金额")  
	private Long afterUnavliedCoins;  
 
	/**
	 * 操作用户id
	 */  
	@FieldName(name = "操作用户id")  
	private Long optUserId;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 状态: -1 初始化账户, 0 待入账,1 已入账 ,2 已经失效,3已到期 
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
	/**
	 * 类型: -1 初始化账户, 1 分享进账,2 邀请者购买进账, 3 被邀请者购买进账 , 50 兑换出账, 51 提现出账
	 */  
	@FieldName(name = "类型")  
	private Integer type;  
 
	/**
	 * 金额
	 */  
	@FieldName(name = "金额")  
	private Long count;  
 
	/**
	 * 进账出账:-1 初始化账户,1 进账,0 出账
	 */  
	@FieldName(name = "进账出账")  
	private Integer inOut;  
 
	/**
	 * 备注
	 */  
	@FieldName(name = "备注")  
	private String detail;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }