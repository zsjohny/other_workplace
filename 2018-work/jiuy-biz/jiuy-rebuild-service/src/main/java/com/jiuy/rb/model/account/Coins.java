package com.jiuy.rb.model.account; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;
import java.util.Date;

/**
 * 玖币表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月18日 下午 05:24:57
 */
@Data
@ModelName(name = "玖币表", tableName = "yjj_coins")
public class Coins extends Model {  
 
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
	private Long userId;  
 
	/**
	 * 用户类型:1小程序用户，2门店用户
	 */  
	@FieldName(name = "用户类型")  
	private Integer userType;  
 
	/**
	 * 总金额
	 */  
	@FieldName(name = "总金额")  
	private Long totalCoins;  
 
	/**
	 * 可用金额
	 */  
	@FieldName(name = "可用金额")  
	private Long aliveCoins;  
 
	/**
	 * 不可用金额
	 */  
	@FieldName(name = "不可用金额")  
	private Long unalivedCoins;  
 
	/**
	 * 待入账金额
	 */  
	@FieldName(name = "待入账玖币")
	private Long waitCoins;  
 
	/**
	 * 失效金额
	 */  
	@FieldName(name = "失效玖币")
	private Long lostCoins;  
 
	/**
	 * 到期玖币
	 */  
	@FieldName(name = "到期玖币")  
	private Long expireCoins;  
 
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
	 * 版本号
	 */  
	@FieldName(name = "版本号")  
	private Long version;  
 
	/**
	 * 累计提现人民币
	 */  
	@FieldName(name = "累计提现人民币")  
	private BigDecimal cashOutRmb;  
 
	/**
	 * 累计提现玖币
	 */  
	@FieldName(name = "累计提现玖币")  
	private Long cashOutCoins;  
 
	/**
	 * 提现初始化月份
	 */  
	@FieldName(name = "提现初始化月份")  
	private Integer initMoth;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }