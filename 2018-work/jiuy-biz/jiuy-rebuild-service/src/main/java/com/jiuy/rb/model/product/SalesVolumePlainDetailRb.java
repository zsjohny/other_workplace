package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 刷量明细表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月19日 下午 01:22:06
 */
@Data
@ModelName(name = "刷量明细表", tableName = "yjj_sales_volume_plain_detail")
public class SalesVolumePlainDetailRb extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 策略主键
	 */  
	@FieldName(name = "策略主键")  
	private Long planId;  
 
	/**
	 * 商品主键
	 */  
	@FieldName(name = "商品主键")  
	private Long productId;  
 
	/**
	 * 商品类型:1普通商品，2显示抢购商品
	 */  
	@FieldName(name = "商品类型")  
	private Integer productType;  
 
	/**
	 * 预计总的要刷多少
	 */  
	@FieldName(name = "预计总的要刷多少")  
	private Long exceptCount;  
 
	/**
	 * 平均值
	 */  
	@FieldName(name = "平均值")  
	private Long averageCount;  
 
	/**
	 * 剩余多少
	 */  
	@FieldName(name = "剩余多少")  
	private Long leftCount;  
 
	/**
	 * 最后一次刷了多少
	 */  
	@FieldName(name = "最后一次刷了多少")  
	private Long lastCount;  
 
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
	 * 总刷量
	 */  
	@FieldName(name = "总刷量")  
	private Long addedCount;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }