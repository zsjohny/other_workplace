package com.jiuy.rb.model.account; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 玖币明细表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月27日 上午 09:56:42
 */
@Data
@ModelName(name = "玖币明细表", tableName = "yjj_coins_detail")
public class CoinsDetail extends Model {  
 
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
	 * 明细表
	 */  
	@FieldName(name = "明细表")  
	private Long coinsDetailId;  
 
	/**
	 * 进账出账:1 进账,0 出账
	 */  
	@FieldName(name = "进账出账")  
	private Integer inOut;  
 
	/**
	 * 类型: 1 分享进账,2 邀请者购买进账, 3 被邀请者购买进账 , 50 兑换出账, 51 提现出账
	 */  
	@FieldName(name = "类型")  
	private Integer type;  
 
	/**
	 * 订单号: 如果是购买进账
	 */  
	@FieldName(name = "订单号")  
	private String orderNo;  
 
	/**
	 * 单笔明细金额
	 */  
	@FieldName(name = "单笔明细金额")  
	private Long count;  
 
	/**
	 * 过期时间
	 */  
	@FieldName(name = "过期时间")  
	private Date deadline;  
 
	/**
	 * 状态: 0 待入账,1 已入账 ,2 已经失效,3已到期 ,4 提现中,5 提现成功，6提现失败
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
	 * 目标id:如被分享的商品，活动，订单号之类的
	 */  
	@FieldName(name = "目标id")  
	private Long targetId;  
 
	/**
	 * 描述
	 */  
	@FieldName(name = "描述")  
	private String description;  
 
	/**
	 * 备注
	 */  
	@FieldName(name = "备注")  
	private String note;  
 
	/**
	 * 此条数据是否失效:1失效,0有效
	 */  
	@FieldName(name = "此条数据是否失效")  
	private Integer isLost;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }