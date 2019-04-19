package com.jiuy.rb.model.account; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现记录表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月23日 上午 10:15:35
 */
@Data
@ModelName(name = "提现记录表", tableName = "yjj_coins_cash_out")
public class CoinsCashOut extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 用户id
	 */  
	@FieldName(name = "用户id")  
	private Long memberId;  
 
	/**
	 * 账户id
	 */  
	@FieldName(name = "账户id")  
	private Long coinsId;  
 
	/**
	 * 提现玖币数量
	 */  
	@FieldName(name = "提现玖币数量")  
	private Long cashCount;  
 
	/**
	 * 折算成的人名币
	 */  
	@FieldName(name = "折算成的人名币")  
	private BigDecimal cashRmb;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Date updateTime;  
 
	/**
	 * 提现状态:1 提现中,2提现失败，3提现成功
	 */  
	@FieldName(name = "提现状态")  
	private Integer status;  
 
	/**
	 * 同步时间
	 */  
	@FieldName(name = "同步时间")  
	private Date syncTime;  
 
	/**
	 * 同步状态:0 未同步 1已同步
	 */  
	@FieldName(name = "同步状态")  
	private Integer syncStatus;  
 
	/**
	 * 当前剩余提现数量
	 */  
	@FieldName(name = "当前剩余提现数量")  
	private BigDecimal leftCashOutRmb;  
 
	/**
	 * 微信的返回结果
	 */  
	@FieldName(name = "微信的返回结果")  
	private String result;  
 
	/**
	 * 明细id
	 */  
	@FieldName(name = "明细id")  
	private Long coinsDetailId;  
 
	/**
	 * 发送给微信的单号
	 */  
	@FieldName(name = "发送给微信的单号")  
	private String wxaNo;  
 
	/**
	 * 微信返回的单号
	 */  
	@FieldName(name = "微信返回的单号")  
	private String paymentNo;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }